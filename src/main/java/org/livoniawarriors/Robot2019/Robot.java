package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.livoniawarriors.Robot2019.UserInput.Button;
import org.livoniawarriors.Robot2019.UserInput.Controllers;
import org.livoniawarriors.Robot2019.modules.*;
import org.livoniawarriors.Robot2019.subsystems.*;
import org.livoniawarriors.Robot2019.subsystems.flamethrower.FlameThrower;
import org.livoniawarriors.Robot2019.subsystems.gameplay.*;
import org.livoniawarriors.Robot2019.subsystems.peripherals.PeripheralSubsystem;
import java.nio.file.Paths;
import java.util.*;

public class Robot extends TimedRobot {

    private static Robot instance;

    private List<ISubsystem> subsystems;
    private Map<String, IControlModule> modules;
    private IControlModule activeModule;
    private IControlModule defaultModule;
    private IControlModule fallbackModule; // The one switched to if a module finishes. If null, it defaults to the last
                                           // registered module

    // Input handling
    public static UserInput userInput;

    // Subsystems
    public static PeripheralSubsystem peripheralSubsystem;
    public static DriveTrain driveTrain;
    public static FlameThrower flameThrower;
    public static GamePlay gamePlay;

    // Yup, this is a logger, use it!
    public static Logger logger;

    private List<Notifier> subsytemNotifiers;
    private final Logger csvLogger;
    private final Level CSV = Level.getLevel("CSV");
    private final ICsvLogger csvBufferWriter;
    private final Map<String, Object> csvBuffer;
    private final Notifier csvNotifier, diagnosticNotifier;
    private final double CSV_UPDATE_PERIOD = 0.04;
    private final double DIAGNOSTIC_PERIOD = 4;

    // Get the robot
    public static Robot getInstance() {
        return instance;
    }

    Robot() {
        instance = this;
        csvBuffer = new HashMap<>();
        System.setProperty("log4j.configurationFile",
                Paths.get(Filesystem.getDeployDirectory().toString(), "log4j2.xml").toString());
        logger = LogManager.getLogger("LogLogger");
        csvLogger = LogManager.getLogger("CsvLogger");
        csvBufferWriter = csvBuffer::put;
        csvNotifier = new Notifier(this::logCSV);
        diagnosticNotifier = new Notifier(this::diagnose);
        logger.error("Hi");
        subsytemNotifiers = new ArrayList<>();
    }

    /**
     * Registers stuff and sets default module, optionally
     */
    private void register() {
        peripheralSubsystem = new PeripheralSubsystem();
        registerSubsystem(userInput = new UserInput());
        registerSubsystem(driveTrain = new DriveTrain());
        registerSubsystem(peripheralSubsystem); // Must happen after drive train
        //registerSubsystem(flameThrower = new FlameThrower());
        registerSubsystem(gamePlay = new GamePlay());
        registerControlModule(new DriveForwardAuton());
        registerControlModule(new FullyAutonModule());
        registerControlModule(new TestAutonModule());
        registerControlModule(new OldFashionedAuton());
        registerControlModule(new TestTeleopModule()); // This is the default one until manual setting default
        setDefaultModule(TestTeleopModule.class);
    }

    /**
     * Registers a control module
     * 
     * @param module to register
     */
    private void registerControlModule(IControlModule module) {
        if (modules.containsKey(module.getClass().getSimpleName()))
            logger.error("Duplicate registration of module: " + module.getClass().getName());//
        modules.put(module.getClass().getSimpleName(), module);
    }

    /**
     * Registers the subsystem, doesn't do much at present
     * 
     * @param subsystem to register
     */
    private void registerSubsystem(ISubsystem subsystem) {
        if (subsystems.contains(subsystem))
            logger.error("Duplicate registration of subsystem: " + subsystem.getClass().getName()); //
        subsystems.add(subsystem);
        Notifier notifier = new Notifier(() -> {
            try {
                var startTime = System.currentTimeMillis();
                subsystem.update(isEnabled());
                var length = System.currentTimeMillis() - startTime;
                if (length > 19)
                    Robot.logger.warn(
                            "Subsystem " + subsystem.getClass().getSimpleName() + " took too many millis: " + length);
            } catch (Throwable t) {
                logger.error(subsystem.getClass().getSimpleName(), t);
            }
        });
        subsytemNotifiers.add(notifier);
    }

    private void setDefaultModule(IControlModule module) {
        defaultModule = module;
    }

    /**
     * Sets the default module, go figure
     * 
     * @param module to set as, you guessed it, default
     */
    private void setDefaultModule(Class<? extends IControlModule> module) {
        if (!modules.containsKey(module.getSimpleName())) {
            logger.error("Module not registered: " + module.getSimpleName()); //
            return;
        }
        setDefaultModule(modules.get(module.getSimpleName()));
    }

    /**
     * Switches the current module
     * 
     * @param name of the module's class
     */
    private void setActiveModule(String name, IControlModule fallbackModule) {
        try {
            activeModule.stop();
        } catch (Throwable t) {
            logger.error(activeModule.getClass().getSimpleName(), t);
        }
        this.fallbackModule = fallbackModule;
        activeModule = modules.get(name);
        try {
            activeModule.start();
        } catch (Throwable t) {
            logger.error(activeModule.getClass().getSimpleName(), t);
        }
    }

    /**
     * Switches the current module
     * 
     * @param name of the module's class
     */
    private void setActiveModule(String name) {
        setActiveModule(name, activeModule);
    }

    /**
     * Logs next CSV file entry
     */
    private void logCSV() {
        subsystems.forEach(s -> s.csv(csvBufferWriter));
        csvLogger.log(Level.getLevel("CSV"), "", csvBuffer.values().toArray());
    }

    /**
     * Called periodically to diagnose subsystems
     */
    private void diagnose() {
        if (DriverStation.getInstance().isFMSAttached())
            return;
        for (var subsystem : subsystems) {
            try {
                subsystem.diagnose();
            } catch (Throwable t) {
                Robot.logger.error("Failed to diagnose " + subsystem.getClass().getSimpleName(), t);
            }
        }
    }

    @Override
    public void robotInit() {
        // Initialize module stuffs
        subsystems = new ArrayList<>();
        modules = new LinkedHashMap<>();
        CameraServer.getInstance().startAutomaticCapture();

        // Register stuff
        register();

        // Set default to first registered if it isn't set
        if (defaultModule == null)
            setDefaultModule((IControlModule) modules.entrySet().toArray()[0]);

        // Initialize things
        logger.info("Initializing subsystems");
        subsystems.forEach(ISubsystem::init); // No need for try catch, if this fails, all is lost, might as well
                                              // System.exit(69);
        logger.info("Initializing modules");
        modules.forEach((name, module) -> module.init());

        // Csv file header
        subsystems.forEach(s -> s.csv(csvBufferWriter));
        csvLogger.info("", csvBuffer.keySet().toArray());

        // Start csv logging
        csvNotifier.startPeriodic(CSV_UPDATE_PERIOD);

        // Start updating subsystems
        subsytemNotifiers.forEach(notifier -> notifier.startPeriodic(0.05));

        // Start diagnosing
        diagnosticNotifier.startPeriodic(DIAGNOSTIC_PERIOD);
        diagnose();
        Shuffleboard.getTab("tab").add(new SendableButton() {
            @Override
            protected void onEnable() {
                startAuton();
            }

            @Override
            protected void onDisable() {
                activeModule = defaultModule;
                startModule(activeModule);
            }
        });
    }

    @Override
    public void robotPeriodic() {
        if (activeModule != null)
            userInput.putValue("tab", "Active Module", activeModule.getClass().getSimpleName());
    }

    private void startAuton() {
        if(peripheralSubsystem.pigeonWorking())
            activeModule = modules.get(OldFashionedAuton.class.getSimpleName());
        else
            activeModule = modules.get(DriveForwardAuton.class.getSimpleName());
        startModule(activeModule);
    }

    @Override
    public void disabledInit() {
        Shuffleboard.stopRecording();
        if (activeModule != null) {
            stopModule(activeModule);
            activeModule = null;
        }
    }

    @Override
    public void disabledPeriodic() {
    }

    private void startModule(IControlModule module) {
        logger.log(Level.INFO, "Starting module: " + module.getClass().getSimpleName());
        try {
            module.start();
        } catch (Throwable t) {
            logger.error(activeModule.getClass().getSimpleName(), t);
        }
    }

    private void stopModule(IControlModule module) {
        logger.log(Level.INFO, "Stopping module: " + activeModule.getClass().getSimpleName());
        try {
            module.stop();
        } catch (Throwable t) {
            logger.error(activeModule.getClass().getSimpleName(), t);
        }

    }

    private void periodic() {
        if(userInput.getController(Controllers.XBOX).getButton(Button.Y))
            startAuton();
        if(userInput.getController(Controllers.XBOX).getButton(Button.A)) {
            activeModule = defaultModule;
            startModule(defaultModule);
        }

        if (activeModule == null) {
            activeModule = defaultModule;
            startModule(activeModule);
        }

        try {
            var startTime = System.currentTimeMillis();
            activeModule.update();
            var length = System.currentTimeMillis() - startTime;
            if (length > 4)
                Robot.logger.warn("");
        } catch (Throwable t) {
            logger.error(activeModule.getClass().getSimpleName(), t);
        }

        if (activeModule.isFinished()) {
            stopModule(activeModule);
            if (fallbackModule != null)
                activeModule = fallbackModule;
            else
                activeModule = defaultModule;
            startModule(activeModule);
        }
    }

    @Override
    public void autonomousPeriodic() {
        periodic();
        if (userInput.getController(Controllers.L_FLIGHTSTICK).getButtonPressed(Button.SIX)
                || userInput.getController(Controllers.L_FLIGHTSTICK).getButtonPressed(Button.SEVEN)) {
            setActiveModule(TestTeleopModule.class.getSimpleName());
        }
    }

    @Override
    public void autonomousInit() {
        Shuffleboard.startRecording();
    }

    @Override
    public void teleopInit() {
        Shuffleboard.startRecording();
        if (activeModule != defaultModule) {
            if (activeModule != null)
                stopModule(activeModule);
            activeModule = defaultModule;
            startModule(activeModule);
        }
    }

    @Override
    public void teleopPeriodic() {
        periodic();
    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }

    /**
     * Disposes stuff. We don't care about errors here. Gets rid of needless catches
     * for streams and such.
     */
    @Override
    protected void finalize() {
        super.finalize();
        try {
            for (ISubsystem subsystem : subsystems)
                subsystem.dispose();
        } catch (Exception e) {
            Robot.logger.error("Robot", e);
        }
    }
}