package org.livoniawarriors.Robot2019.subsystems.gameplay;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.apache.logging.log4j.Level;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput;
import org.livoniawarriors.Robot2019.UserInput.Button;
import org.livoniawarriors.Robot2019.UserInput.Controllers;
import org.livoniawarriors.Robot2019.UserInput.Joystick;



public class Climber {

	private static final int CLIMBER_FRONT = 23;
	private static final int CLIMBER_BACK = 12;
	private CANSparkMax climberFront;
	private WPI_TalonSRX climberBack;
	private UserInput.Controller controller;

	public Climber() {
		climberFront = new CANSparkMax(CLIMBER_FRONT, MotorType.kBrushless);
		climberBack = new WPI_TalonSRX(CLIMBER_BACK);
		controller = Robot.userInput.getController(Controllers.XBOX);
	}

	public void update(boolean enabled) {
		if(!enabled) 
			return;
		
		moveFrontClimber();
		moveBackClimber();
		
	}

	private void moveFrontClimber() {
		//Move climber arm down
		if(controller.getButton(Button.BUMPER_R)) {
			climberFront.set(-0.5);
		}
		//Move climber arm up
		else if(controller.getButton(Button.BUMPER_L)) {
			climberFront.set(0.5);

		} else {
			climberFront.set(0.0);	
		}
	}

	private void moveBackClimber() {
		//Spin climber in a direction that causes the robot to go down
		if(controller.getButton(Button.Y)) {
			climberBack.set(0.5);
		
		//Spin climber in direction that causes the robot to go up
		} else if(controller.getButton(Button.X)) {
			climberBack.set(-1);

		} else {
			climberBack.set(0.0);
		}
	}
}
