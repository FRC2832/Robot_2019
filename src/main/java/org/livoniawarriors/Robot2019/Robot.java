package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.*;

public class Robot extends TimedRobot {

    public static Lidar lidar;

    @Override
    public void robotInit() {
        lidar = new Lidar();
    }

    @Override
    public void robotPeriodic() {
        lidar.update();
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
