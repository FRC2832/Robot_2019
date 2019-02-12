package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.livoniawarriors.Robot2019.modules.TestAutonModule;
import org.livoniawarriors.Robot2019.modules.TestTeleopModule;
import org.livoniawarriors.Robot2019.subsystems.*;
import org.livoniawarriors.Robot2019.subsystems.diagnostic.Diagnostic;
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
    private IControlModule fallbackModule; // The one switched to if a module finishes. If null, it defaults to the last registered module

    // Input handling
    public static UserInput userInput;

    // Subsystems
    //public static PeripheralSubsystem peripheralSubsystem;
    public static Diagnostic diagnostic;
    //public static DriveTrain driveTrain;
    public static FlameThrower flameThrower;
    public static GamePlay gamePlay;

    // Yup, this is a logger, use it!
    public static Logger logger;

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
        System.setProperty("log4j.configurationFile", Paths.get(Filesystem.getDeployDirectory().toString(), "log4j2.xml").toString());
        logger = LogManager.getLogger("LogLogger");
        csvLogger = LogManager.getLogger("CsvLogger");
        csvBufferWriter = csvBuffer::put;
        csvNotifier = new Notifier(this::logCSV);
        diagnosticNotifier = new Notifier(this::diagnose);
        logger.error("Hi");
    }

    /**
     * Registers stuff and sets default module, optionally
     */
    private void register() {
        //registerSubsystem(peripheralSubsystem = new PeripheralSubsystem());
        registerSubsystem(userInput = new UserInput());
        registerSubsystem(diagnostic = new Diagnostic());
        //registerSubsystem(driveTrain = new DriveTrain());
        registerSubsystem(flameThrower = new FlameThrower());
        registerSubsystem(gamePlay = new GamePlay());
        registerControlModule(new TestAutonModule());
        registerControlModule(new TestTeleopModule()); // This is the default one until manual setting default
        setDefaultModule(TestTeleopModule.class);
    }

    /**
     * Registers a control module
     * @param module to register
     */
    private void registerControlModule(IControlModule module) {
        if(modules.containsKey(module.getClass().getSimpleName()))
            System.err.println("Duplicate registration of module: " + module.getClass().getName());// @Todo logging
        modules.put(module.getClass().getSimpleName(), module);
    }

    /**
     * Registers the subsystem, doesn't do much at present
     * @param subsystem to register
     */
    private void registerSubsystem(ISubsystem subsystem) {
        if(subsystems.contains(subsystem))
            System.err.println("Duplicate registration of subsystem: " + subsystem.getClass().getName()); // @Todo logging
        subsystems.add(subsystem);
    }

    private void setDefaultModule(IControlModule module) {
        defaultModule = module;
    }

    /**
     * Sets the default module, go figure
     * @param module to set as, you guessed it, default
     */
    private void setDefaultModule(Class<? extends IControlModule> module) {
        if(!modules.containsKey(module.getSimpleName()))
            System.err.println("Module not registered: " + module.getSimpleName()); // @Todo logging
        setDefaultModule(modules.get(module.getSimpleName()));
    }

    /**
     * Switches the current module
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
        if(DriverStation.getInstance().isFMSAttached())
            return;
        for (var subsystem: subsystems) {
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

        // Register stuff
        register();

        // Set default to first registered if it isn't set
        if(defaultModule == null)
            setDefaultModule((IControlModule) modules.entrySet().toArray()[0]);

        // Initialize things
        logger.info("Initializing subsystems");
        subsystems.forEach(ISubsystem::init); // No need for try catch, if this fails, all is lost, might as well System.exit(69);
        logger.info("Initializing modules");
        modules.forEach((name, module) -> module.init());

        // Csv file header
        subsystems.forEach(s -> s.csv(csvBufferWriter));
        csvLogger.info("", csvBuffer.keySet().toArray());

        // Start csv logging
        csvNotifier.startPeriodic(CSV_UPDATE_PERIOD);

        // Start diagnosing
        diagnosticNotifier.startPeriodic(DIAGNOSTIC_PERIOD);
        diagnose();
    }

    @Override
    public void robotPeriodic() {
        subsystems.forEach(subsystem -> {
            try {
                subsystem.update(isEnabled());
            } catch (Throwable t) {
                logger.error(subsystem.getClass().getSimpleName(), t);
            }
        });
    }

    @Override
    public void disabledInit() {
        if(activeModule != null) {
            try {
                activeModule.stop();
            } catch (Throwable t) {
                logger.error(activeModule.getClass().getSimpleName(), t);
            }
            activeModule = null;
        }
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        if(activeModule != null) {
            try {
                activeModule.start();
            } catch (Throwable t) {
                logger.error(activeModule.getClass().getSimpleName(), t);
            }
        }
        else {
            activeModule = defaultModule;
            try {
                defaultModule.start();
            } catch (Throwable t) {
                logger.error(defaultModule.getClass().getSimpleName(), t);
            }
        }
    }

    @Override
    public void teleopPeriodic() {
        if(activeModule == null) {
            activeModule = defaultModule;
            try {
                activeModule.start();
            } catch (Throwable t) {
                logger.error(activeModule.getClass().getSimpleName(), t);
            }
        }

        try {
            activeModule.update();
        } catch (Throwable t) {
            logger.error(activeModule.getClass().getSimpleName(), t);
        }

        if(activeModule.isFinished()) {
            try {
                activeModule.stop();
            } catch (Throwable t) {
                logger.error(activeModule.getClass().getSimpleName(), t);
            }
            if(fallbackModule != null)
                activeModule = fallbackModule;
            else
                activeModule = defaultModule;
            try {
                activeModule.start();
            } catch (Throwable t) {
                logger.error(activeModule.getClass().getSimpleName(), t);
            }
        }
    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }

    /**
     * Disposes stuff. We don't care about errors here. Gets rid of needless catches for streams and such.
     */
    @Override
    protected void finalize() {
        super.finalize();
        try {
            for (ISubsystem subsystem: subsystems)
                subsystem.dispose();
        } catch (Exception e) {
            Robot.logger.error("Robot", e);
        }
    }
}