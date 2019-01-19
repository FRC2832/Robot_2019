package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.livoniawarriors.Robot2019.commands.*;
import org.livoniawarriors.Robot2019.subsystems.*;

public class Robot extends TimedRobot {
    public static Logger logger;
    public static DriveTrain driveTrain;
    @Override
    public void robotInit() {
        logger = new Logger();
        logger.log("yeet", 100);
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
