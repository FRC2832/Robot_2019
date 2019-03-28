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

import edu.wpi.first.wpilibj.Spark;

public class Climber {

	private static final int CLIMBER = 13;
	private Spark climber;
	private UserInput.Controller controller;

	public Climber() {
		climber = new Spark(CLIMBER);
		controller = Robot.userInput.getController(Controllers.XBOX);
	}

	public void update(boolean enabled) {
		if(!enabled) 
			return;	
		
		if(controller.getButton(Button.BUMPER_R)) {
			climber.set(0.8);
		}
	}
}
