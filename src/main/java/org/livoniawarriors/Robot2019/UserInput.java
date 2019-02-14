package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.SendableBase;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class UserInput implements ISubsystem {

    public static float DEADZONE = 0.1f;

    private List<Controller> controllers;
    private NetworkTableInstance inst;
    private NetworkTable table;
    private SendableChooser<String> autonChooser;
    private HashMap<String, Integer> tableEntries;
    
    public void createValue(String selectedTab, String title, int handle, Object value) {
        if (tableEntries.containsKey(title)){
            if (null != selectedTab && null != title && null != value){
                ShuffleboardTab currentTab = Shuffleboard.getTab(selectedTab);
                NetworkTableEntry currentEntry = new NetworkTableEntry(inst, handle);
                currentEntry.setValue(value);
                currentEntry = table.getEntry(title);
                currentEntry = currentTab.add(title, value).getEntry();
                tableEntries.put(title, handle);
            }
            else {
                Robot.logger.log(Level.DEBUG, "Null value added to shuffleboard. ID10T error.");
            }
        }
        else {
            if (null != title && null != value) {
                NetworkTableEntry selectedEntry = table.getEntry(title);
                selectedEntry.setValue(value);
            }
            else {
                Robot.logger.log(Level.DEBUG, "Can't update a null value to shuffleboard :who:");
            }
        }
    }
    /** Add an option to a Sendable Chooser
     * @param chooser The sendable chooser
     * @param name The name that appears on the dashboard
     * @param option What is returned if the option is selected
     * @param defaultOption Whether or not it is the default option
    */
    public void addOption(SendableChooser<String> chooser, String name, String option, boolean defaultOption) {
        if (defaultOption) {
            chooser.setDefaultOption(name, option);
        }
        else {
            chooser.addOption(name, option);
        }
    }
    /**
     * Get option from sendable chooser
     * @param chooser The sendable chooser
     * @return The option selected on the chooser
     */
    public String getSelected(SendableChooser<String> chooser) {
        return chooser.getSelected();
    }

    public Controller getController(int player) {
        return controllers.get(player);
    }

    @Override
    public void init() {
        controllers = new ArrayList<>();
        controllers.add(new Controller(0));
        controllers.add(new Controller(1));
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("datatable");
        Shuffleboard.getTab("tab").add(new LogButton());
        autonChooser = new SendableChooser<>();
        tableEntries = new HashMap<String, Integer>();
    }

    @Override
    public void update(boolean enabled) {
        controllers.forEach(Controller::update);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void diagnose() {

    }

    @Override
    public void csv(ICsvLogger csv) {

    }

    public class Controller extends GenericHID {

        private double startTime;
        private float duration;

        private Controller(int port) {
            super(port);
        }

        /**
         * Checks if button is currently pressed
         * @param button to check
         * @return if button is pressed
         */
        public boolean getButton(Button button) {
            return getRawButton(button.value);
        }

        /**
         * Checks if button was pressed in the last frame
         * @param button to check
         * @return whether button was just pressed
         */
        public boolean getButtonPressed(Button button) {
            return getRawButtonPressed(button.value);
        }

        /**
         * Checks if button was released in the last frame
         * @param button to check
         * @return whether button was just released
         */
        public boolean getButtonReleased(Button button) {
            return getRawButtonReleased(button.value);
        }

        private void setRumble(double intensity) {
            setRumble(RumbleType.kLeftRumble, intensity);
            setRumble(RumbleType.kRightRumble, intensity);
        }

        /**
         * Sets motors to vibrate for a duration of time
         * @param duration in seconds
         * @param intensity in units of vibration
         */
        public void vibrate(float duration, double intensity) {
            this.duration = duration;
            startTime = Timer.getFPGATimestamp();
            setRumble(intensity);
        }

        private void update() {
            if (duration > 0 && startTime + duration > Timer.getFPGATimestamp()) {
                setRumble(0);
                duration = -1;
            }
        }

        @Override
        public double getRawAxis(int axis) {
            if(Math.abs(super.getRawAxis(axis)) < DEADZONE)
                return 0;
            return (Math.abs(super.getRawAxis(axis)) - DEADZONE) * Math.signum(super.getRawAxis(axis));
        }

        /**
         * Get the X axis value of the controller.
         *
         * @param hand Side of controller whose value should be returned.
         * @return The X axis value of the controller.
         */
        @Override
        public double getX(Hand hand) {
            if (hand.equals(Hand.kLeft)) {
                return getRawAxis(0);
            } else {
                return getRawAxis(4);
            }
        }

        /**
         * Get the Y axis value of the controller.
         *
         * @param hand Side of controller whose value should be returned.
         * @return The Y axis value of the controller.
         */
        @Override
        public double getY(Hand hand) {
            if (hand.equals(Hand.kLeft)) {
                return getRawAxis(1);
            } else {
                return getRawAxis(5);
            }
        }

        /**
         * Get the trigger axis value of the controller.
         *
         * @param hand Side of controller whose value should be returned.
         * @return The trigger axis value of the controller.
         */
        public double getTriggerAxis(Hand hand) {
            if (hand.equals(Hand.kLeft)) {
                return getRawAxis(2);
            } else {
                return getRawAxis(3);
            }
        }
    }

    public enum Button {
        BUMPER_L(5),
        BUMPER_R(6),
        JOYSTICK_L(9),
        JOYSTICK_R(10),
        A(1),
        B(2),
        X(3),
        Y(4),
        BACK(7),
        START(8);

        private final int value;
        Button(int value) {
            this.value = value;
        }
    }
}
