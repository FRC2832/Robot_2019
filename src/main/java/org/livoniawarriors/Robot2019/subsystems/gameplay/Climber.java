package org.livoniawarriors.Robot2019.subsystems.gameplay;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput;
import org.livoniawarriors.Robot2019.UserInput.Button;
import org.livoniawarriors.Robot2019.UserInput.Controllers;
import org.livoniawarriors.Robot2019.UserInput.Joystick;



public class Climber {

    private static final int CLIMBER = 23;
    private static final int LEFT_PUSHER = 17; //TODO: Set to real numbers
    private static final int RIGHT_PUSHER = 18;
    private CANSparkMax climber;
    private WPI_TalonSRX leftPusher, rightPusher;
	private UserInput.Controller leftFlightStick, rightFlightStick;

	public Climber() {
        climber = new CANSparkMax(CLIMBER, MotorType.kBrushless);
        leftPusher = new WPI_TalonSRX(LEFT_PUSHER);
        rightPusher = new WPI_TalonSRX(RIGHT_PUSHER);
        rightPusher.setInverted(true);
        
	    leftFlightStick = Robot.userInput.getController(Controllers.L_FLIGHTSTICK);
        rightFlightStick = Robot.userInput.getController(Controllers.R_FLIGHTSTICK);
        
	}

	public void update(boolean enabled) {
		if(!enabled) {
            return;
        }
		
		if(rightFlightStick.getButton(Button.THREE)) {
			climber.set(-0.8);
        } else if (rightFlightStick.getButton(Button.FOUR)) {
            climber.set(0.8);
        } else {
            climber.set(0);
        }

        if (leftFlightStick.getButton(Button.ONE)) {
            leftPusher.set(0.7);
        } else if (leftFlightStick.getButton(Button.TWO)) { //Button #2
            leftPusher.set(-0.7);
        } else {
            leftPusher.set(0);
        }

        if (rightFlightStick.getButton(Button.ONE)) {
            rightPusher.set(0.7);
        } else if (rightFlightStick.getButton(Button.TWO)) { //Button #2d
            rightPusher.set(-0.7);
        } else {
            rightPusher.set(0);
        }

    }
    
}
