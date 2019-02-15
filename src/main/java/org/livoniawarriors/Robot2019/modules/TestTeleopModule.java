package org.livoniawarriors.Robot2019.modules;

import edu.wpi.first.wpilibj.GenericHID;
import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput;
import org.livoniawarriors.Robot2019.UserInput.Controllers;
import org.livoniawarriors.Robot2019.UserInput.Joystick;

public class TestTeleopModule implements IControlModule {
    private UserInput.Controller flightstickLeft;
    private UserInput.Controller flightstickRight;
    private UserInput.Controller xbox;


    @Override
    public void init() {
        flightstickLeft = Robot.userInput.getController(Controllers.L_FLIGHTSTICK);
        flightstickRight = Robot.userInput.getController(Controllers.R_FLIGHTSTICK);
        xbox = Robot.userInput.getController(Controllers.XBOX);
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        Robot.driveTrain.tankDrive(flightstickLeft.getJoystickY(Joystick.FLIGHTSTICK), flightstickRight.getJoystickY(Joystick.FLIGHTSTICK));
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
