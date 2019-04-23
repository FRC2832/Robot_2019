package org.livoniawarriors.Robot2019.modules;

import org.apache.logging.log4j.Level;
import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput;
import org.livoniawarriors.Robot2019.UserInput.Button;
import org.livoniawarriors.Robot2019.UserInput.Controllers;
import org.livoniawarriors.Robot2019.UserInput.Joystick;
import org.livoniawarriors.Robot2019.UserInput.controlMode;

public class TestTeleopModule implements IControlModule {
    private UserInput.Controller flightstickLeft;
    private UserInput.Controller flightstickRight;
    private UserInput.Controller driverXbox;
    private double lStick;
    private double rStick;
    private double slider;
    

    @Override
    public void init() {
        flightstickLeft = Robot.userInput.getController(Controllers.L_FLIGHTSTICK);
        flightstickRight = Robot.userInput.getController(Controllers.R_FLIGHTSTICK);
        driverXbox = Robot.userInput.getController(Controllers.TEST_XBOX);
        //Robot.userInput.putValue("tab", "Joysticko Mode", true);
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
       updateControllers();
       Robot.userInput.putValue("John", "left Stick", lStick);
       Robot.userInput.putValue("John", "right Stick", rStick);
       Robot.driveTrain.tankDrive(lStick, rStick, false);       
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
    public void updateControllers() {
        if (Robot.userInput.getCurrentControllerMode() == 0) {
            lStick = flightstickLeft.getJoystickY(Joystick.FLIGHTSTICK);
            rStick = flightstickRight.getJoystickY(Joystick.FLIGHTSTICK);
            if (flightstickLeft.getButton(Button.TRIGGER)) {
                lStick = lStick * .5;
                }
            if (flightstickRight.getButton(Button.TRIGGER)) {
                rStick = rStick * .5;
                }
        }
        else if (Robot.userInput.getCurrentControllerMode() == 1) {
            lStick = driverXbox.getJoystickY(Joystick.LEFT);
            rStick = driverXbox.getJoystickY(Joystick.RIGHT);
        }
    }

 
    



}
