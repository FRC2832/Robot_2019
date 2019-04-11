package org.livoniawarriors.Robot2019.subsystems.gameplay;

import com.ctre.phoenix.motorcontrol.NeutralMode;
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

    private static final int CLIMBER = 12;
    private static final int LEFT_PUSHER = 17;
    private static final int RIGHT_PUSHER = 18;
    private static final double PUSHER_SPEED = 0.95;
    private static final double CLIMBER_SPEED = 0.7;
    private CANSparkMax climber;
    private WPI_TalonSRX leftPusher, rightPusher;
	private UserInput.Controller leftFlightStick, rightFlightStick;

	public Climber() {
        climber = new CANSparkMax(CLIMBER, MotorType.kBrushless);
        climber.setIdleMode(IdleMode.kBrake);
        leftPusher = new WPI_TalonSRX(LEFT_PUSHER);
        rightPusher = new WPI_TalonSRX(RIGHT_PUSHER);
        leftPusher.setNeutralMode(NeutralMode.Brake);
        rightPusher.setNeutralMode(NeutralMode.Brake);
        rightPusher.setInverted(true);
        
	    leftFlightStick = Robot.userInput.getController(Controllers.L_FLIGHTSTICK);
        rightFlightStick = Robot.userInput.getController(Controllers.R_FLIGHTSTICK);
        
	}

	public void update(boolean enabled) {
		if(!enabled) {
            return;
        }
        
		if(rightFlightStick.getButton(Button.FOUR)) {
            climber.set(-CLIMBER_SPEED);
        } else if (rightFlightStick.getButton(Button.FIVE)) {
            climber.set(CLIMBER_SPEED);
        } else {
            climber.set(0);
        }

        if (leftFlightStick.getButton(Button.THREE)) {
            leftPusher.set(PUSHER_SPEED);
        } else if (leftFlightStick.getButton(Button.TWO)) {
            leftPusher.set(-PUSHER_SPEED);
        } else {
            leftPusher.set(0);
        }

        if (rightFlightStick.getButton(Button.THREE)) {
            rightPusher.set(PUSHER_SPEED);
        } else if (rightFlightStick.getButton(Button.TWO)) {
            rightPusher.set(-PUSHER_SPEED);
        } else {
            rightPusher.set(0);
        }

    }
    
}
