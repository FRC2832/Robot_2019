package org.livoniawarriors.Robot2019.modules;

import edu.wpi.first.wpilibj.GenericHID;
import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput;
import org.livoniawarriors.Robot2019.UserInput.Button;
import org.livoniawarriors.Robot2019.UserInput.Controllers;
import org.livoniawarriors.Robot2019.UserInput.Joystick;
import org.livoniawarriors.Robot2019.ControlMapping;

public class TestTeleopModule implements IControlModule {
    private UserInput.Controller flightstickLeft;
    private UserInput.Controller flightstickRight;
    private UserInput.Controller driverXbox;
    private double lStick;
    private double rStick;
    private double slider;
    private boolean flightstickoMode;


    @Override
    public void init() {
        flightstickLeft = Robot.userInput.getController(Controllers.L_FLIGHTSTICK);
        flightstickRight = Robot.userInput.getController(Controllers.R_FLIGHTSTICK);
        driverXbox = Robot.userInput.getController(Controllers.TEST_XBOX);
        flightstickoMode = true;
        Robot.userInput.createValue("tab", "Controller Mode", 7, flightstickoMode);
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
       updateControllers();
       Robot.driveTrain.tankDrive(lStick, rStick);
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public void updateControllers() {
        if (flightstickoMode) {
            lStick = flightstickLeft.getJoystickY(Joystick.FLIGHTSTICK);
            rStick = flightstickRight.getJoystickY(Joystick.FLIGHTSTICK);
            if (flightstickRight.getButtonPressed(Button.THUMB)){
                slider = flightstickRight.getOtherAxis(Robot.userInput.FLIPPER_AXIS);
                lStick = lStick * slider;
                rStick = rStick * slider;
            }
            else {
                if (flightstickLeft.getButtonPressed(Button.TRIGGER)) {
                    lStick = lStick * .5;
                }
                if (flightstickRight.getButtonPressed(Button.TRIGGER)) {
                    rStick = rStick * .5;
                }
            }
        }
        else {
            lStick = driverXbox.getJoystickY(Joystick.LEFT);
            rStick = driverXbox.getJoystickX(Joystick.RIGHT);
        }
    }
}
