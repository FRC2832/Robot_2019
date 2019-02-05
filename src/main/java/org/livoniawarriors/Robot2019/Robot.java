package org.livoniawarriors.Robot2019;

import edu.wpi.first.hal.can.CANJNI;
import edu.wpi.first.wpilibj.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.livoniawarriors.Robot2019.subsystems.*;
import org.livoniawarriors.Robot2019.subsystems.flamethrower.FlameThrower;
import org.livoniawarriors.Robot2019.subsystems.gameplay.*;
import org.livoniawarriors.Robot2019.subsystems.peripherals.PeripheralSubsystem;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class Robot extends TimedRobot {

    private static Robot instance;

    private List<ISubsystem> subsystems;
    private Map<String, IControlModule> modules;
    private IControlModule activeModule;
    private IControlModule defaultModule;
    private IControlModule fallbackModule; // The one switched to if a module finishes. If null, it defaults to the last registered module

    public PeripheralSubsystem peripheralSubsystem;
    public UserInput userInput;
    public Diagnostic diagnostic;
    public DriveTrain driveTrain;
    public FlameThrower flameThrower;
    public GamePlay gamePlay;

    public final Logger logger;

    public static Robot getInstance() {
        return instance;
    }

    public Robot() {
        instance = this;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd-HH-mm-ss");
        System.setProperty("current.date", dateFormat.format(new Date()));
        System.setProperty("log4j.configurationFile", Paths.get(Filesystem.getDeployDirectory().toString(), "log4j2.xml").toString());
        logger = LogManager.getLogger(Robot.class);
        logger.error("Hi");
    }

    /**
     * Registers stuff and sets default module, optionally
     */
    private void register() {
        registerSubsystem(peripheralSubsystem = new PeripheralSubsystem());
        registerSubsystem(userInput = new UserInput());
        registerSubsystem(diagnostic = new Diagnostic());
        registerSubsystem(driveTrain = new DriveTrain());
        registerSubsystem(flameThrower = new FlameThrower());
        registerSubsystem(gamePlay = new GamePlay());
        registerControlModule(new TestAutonModule()); // This is the default one for now
        registerControlModule(new TestTeleopModule());
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

    @Override
    public void robotInit() {
        subsystems = new ArrayList<>();
        modules = new LinkedHashMap<>();

        register();

        if(defaultModule == null) { // Set default to last registered if it isn't set
            modules.forEach((name, module) -> setDefaultModule(module));
        }

        // Initialize things
        subsystems.forEach(ISubsystem::init); // No need for try catch, if this fails, all is lost
        modules.forEach((name, module) -> module.init());
    }

    @Override
    public void robotPeriodic() {
        subsystems.forEach(subsystem -> {
            try {
                subsystem.update(isEnabled());
            } catch (Throwable t) {
                logger.error(activeModule.getClass().getSimpleName(), t);
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
                logger.error(activeModule.getClass().getSimpleName(), t);
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
            e.printStackTrace();// @Todo logging
        }
    }
}