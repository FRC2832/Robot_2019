package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import java.util.ArrayList;
import java.util.List;

public class UserInput implements ISubsystem {

    public static float DEADZONE = 0.1f;

    private List<Controller> controllers;

    public Controller getController(int player) {
        return controllers.get(player);
    }

    @Override
    public void init() {
        controllers = new ArrayList<>();
        controllers.add(new Controller(0));
        controllers.add(new Controller(1));
        Shuffleboard.getTab("tab").add(new LogButton());
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
