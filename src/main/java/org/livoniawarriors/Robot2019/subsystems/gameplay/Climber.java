package org.livoniawarriors.Robot2019.subsystems.gameplay;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.apache.logging.log4j.Level;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput.Button;
import org.livoniawarriors.Robot2019.UserInput.Controllers;
import org.livoniawarriors.Robot2019.UserInput.Joystick;

import edu.wpi.first.wpilibj.Spark;

public class Climber {

	private static final int CLIMB_LEFT = 0;
	private static final int CLIMB_RIGHT = 1;
	private static final double POWER = 0.7d;
	private Spark climbLeft, climbRight;

	public Climber() {
		climbLeft = new Spark(CLIMB_LEFT);
		climbLeft.setInverted(true);
		climbRight = new Spark(CLIMB_RIGHT);
	}

	public void update(boolean enabled) {
		if(!enabled) 
			return;	
		
		if(Robot.userInput.getController(Controllers.XBOX).getButton(Button.START))
			setMotors(POWER);
		else if(Robot.userInput.getController(Controllers.XBOX).getButton(Button.BACK))
			setMotors(-POWER);
		else
			setMotors(0);
	}

	private void setMotors(double power) {
		climbLeft.set(power);
		climbRight.set(power);
	}
}
