package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.*;

import java.util.*;

public class Robot extends TimedRobot {

    private List<Subsystem> subsystems;
    private Map<String, IControlModule> modules;
    private IControlModule activeModule;
    public IControlModule defaultModule;
    private IControlModule fallbackModule; // The one switched to if a module finishes. If null, it defaults to the first registered module

    public SensorySubsystem sensorySubsystem;
    public InputHandler inputHandler;

    /**
     * Registers stuff and sets default module, optionally
     */
    private void register() {
        registerSubsystem(sensorySubsystem = new SensorySubsystem());
        registerSubsystem(inputHandler = new InputHandler());
        registerControlModule(new TestAutonModule()); // This is the default one for now
        registerControlModule(new TestTeleopModule());
        setDefaultModule("TestTeleopModule");
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
    private void registerSubsystem(Subsystem subsystem) {
        if(subsystems.contains(subsystem))
            System.err.println("Duplicate registration of subsystem: " + subsystem.getClass().getName()); // @Todo logging
        subsystems.add(subsystem);
    }

    private void setDefaultModule(IControlModule module) {
        defaultModule = module;
    }

    private void setDefaultModule(String module) {
        if(!modules.containsKey(module))
            System.err.println("Module not registered: " + module); // @Todo logging
        setDefaultModule(modules.get(module));
    }

    /**
     * Switches the current module
     * @param name of the module's class
     */
    private void setActiveModule(String name, IControlModule fallbackModule) {
        activeModule.stop();
        this.fallbackModule = fallbackModule;
        activeModule = modules.get(name);
        activeModule.start();
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

        if(defaultModule == null) { // Set default to first registered if it isn't set
            modules.forEach((name, module) -> setDefaultModule(module));
        }

        // Initialize things
        subsystems.forEach(Subsystem::init);
        modules.forEach((name, module) -> module.init());
    }

    @Override
    public void robotPeriodic() {
        subsystems.forEach(subsystem -> subsystem.update(isEnabled()));
    }

    @Override
    public void disabledInit() {
        activeModule.stop();
        activeModule = null;
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
        if(activeModule != null)
            activeModule.start();
        else {
            activeModule = defaultModule;
            defaultModule.start();
        }
    }

    @Override
    public void teleopPeriodic() {
        // This statement is just an example of usage
        if(inputHandler.getController(0).getButtonReleased(ControlMapping.testButton)) {

        }

        if(activeModule == null) {
            activeModule = defaultModule;
            activeModule.init();
        }

        activeModule.update();

        if(activeModule.isFinished()) {
            activeModule.stop();
            if(fallbackModule != null)
                activeModule = fallbackModule;
            else
                activeModule = defaultModule;
            activeModule.start();
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
            for (Subsystem subsystem: subsystems)
                subsystem.dispose();
        } catch (Exception e) {
            e.printStackTrace();// @Todo logging
        }
    }
}
