package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.GenericHID;

public class TestTeleopModule implements IControlModule {
    private UserInput.Controller controller;

    @Override
    public void init() {
        controller = Robot.userInput.getController(0);
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        Robot.driveTrain.tankDrive(controller.getY(GenericHID.Hand.kLeft), controller.getY(GenericHID.Hand.kRight));
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
