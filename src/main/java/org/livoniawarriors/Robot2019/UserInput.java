package org.livoniawarriors.Robot2019;

import edu.wpi.first.networktables.NetworkTableValue;
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
import org.livoniawarriors.Robot2019.ControlMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class UserInput implements ISubsystem {

    private static float DEADZONE = 0.1f;
    public static int FLIPPER_AXIS = 3;
    public static int R_TRIGGER = 3;
    public static int L_TRIGGER = 2;

    private List<Controller> controllers;
    private NetworkTableInstance inst;
    private NetworkTable table;
    private SendableChooser<String> chooser;
    private HashMap<String, Integer> tableEntries;


    public Object getSelected() {
        return chooser.getSelected();
    }

    public Controller getController(Controllers controller) {
        return controllers.get(controller.value);
    }

    @Override
    public void init() {
        controllers = new ArrayList<>();
        controllers.add(new Controller(Controllers.XBOX.value));
        controllers.add(new Controller(Controllers.L_FLIGHTSTICK.value));
        controllers.add(new Controller(Controllers.R_FLIGHTSTICK.value));
        controllers.add(new Controller(Controllers.TEST_XBOX.value));
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("datatable");
        Shuffleboard.getTab("tab").add(new LogButton());
        chooser = new SendableChooser<>();
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

    public void putValue(String selectedTab, String title, int handle, Object value) {
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

    public NetworkTableValue getNetworkTableValue(String title) {
        return table.getEntry(title).getValue();
    }

    public void addOption(String name, String option, boolean defaultOption) {
        if (defaultOption) {
            chooser.setDefaultOption(name, option);
        }
        else {
            chooser.addOption(name, option);
        }
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

        public double getJoystickX(Joystick joystick) {
                return getRawAxis((int)joystick.value.getX());
        }

        public double getJoystickY(Joystick joystick) {
            return getRawAxis((int)joystick.value.getY());
        }

        /**
         * Don't use me!
         */
        @Deprecated
        public double getX(Hand hand){
            return 0;
        }

        /**
         * Don't use me!
         */
        @Deprecated
        public double getY(Hand hand){
            return 0;
        }

        public double getOtherAxis(int selector) {
            return getRawAxis(selector);
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
        START(8),
        TRIGGER(1),
        THUMB(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        ELEVEN(11),
        TWELVE_ONLYRIGHT(12);

        private final int value;
        Button(int value) {
            this.value = value;
        }
    }

    public enum FlightStickButton {
        TRIGGER(1),
        THUMB(2),
        THREE(2),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        ELEVEN(11);

        private final int value;
        FlightStickButton(int value) {
            this.value = value;
        }
    }

    /**
     * Holds a joystick mapping
     */
    public static class JoystickMapping {
        private int x, y;

        private JoystickMapping(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public enum Joystick {
        LEFT(new JoystickMapping(0,1)),
        RIGHT(new JoystickMapping(4,5)),
        FLIGHTSTICK(new JoystickMapping(0,1));

        private final JoystickMapping value;
        Joystick(JoystickMapping value) {
            this.value = value;
        }
    }

    public enum Controllers {
        XBOX(0), L_FLIGHTSTICK(1), R_FLIGHTSTICK(2), TEST_XBOX(3);

        private final int value;
        Controllers(int value) {
            this.value = value;
        }
    }
}
