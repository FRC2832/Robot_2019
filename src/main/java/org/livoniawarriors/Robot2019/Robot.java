package org.livoniawarriors.Robot2019;

import org.livoniawarriors.Robot2019.subsystems.Elevator;

import edu.wpi.first.wpilibj.*;

public class Robot extends TimedRobot {

    public static Elevator elevator;
    public static Joystick joystick;

    @Override
    public void robotInit() {
        elevator = new Elevator();
        joystick = new Joystick(0);
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledInit() {
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
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }
}
