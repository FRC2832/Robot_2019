package org.livoniawarriors.Robot2019.subsystems.gameplay;

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

	private static final int CLIMBER = 23;
	private CANSparkMax climber;
	private UserInput.Controller controller;

	public Climber() {
		climber = new CANSparkMax(CLIMBER, MotorType.kBrushless);
		controller = Robot.userInput.getController(Controllers.XBOX);
	}

	public void update(boolean enabled) {
		if(!enabled) 
			return;
		
		if(controller.getButton(Button.BUMPER_R)) {
			climber.set(-0.5);
		}

		else if(controller.getButton(Button.BUMPER_L)) {
			climber.set(0.5);
			
		} else {
			climber.set(0.0);
		}
	}
}
