package org.livoniawarriors.Robot2019;

import org.livoniawarriors.Robot2019.subsystems.ElevatorPID;

import edu.wpi.first.wpilibj.*;

public class Robot extends TimedRobot {

    public static ElevatorPID elevator;
    public static XboxController xboxController;

    @Override
    public void robotInit() {
        elevator = new ElevatorPID();
        xboxController = new XboxController(0);
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

        if (xboxController.getAButtonPressed()) {
            elevator.setElevatorHeight(ElevatorPID.ElevatorHeights.LowHatch);
        }
        if (xboxController.getXButtonPressed()) {
            elevator.setElevatorHeight(ElevatorPID.ElevatorHeights.MidHatch);
        }
        if (xboxController.getYButtonPressed()) {
            elevator.setElevatorHeight(ElevatorPID.ElevatorHeights.TopHatch);
        } 

    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }
}
