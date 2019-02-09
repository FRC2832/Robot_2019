package org.livoniawarriors.Robot2019.subsystems.gameplay;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.livoniawarriors.Robot2019.subsystems.diagnostic.IDiagnosable;
import org.apache.logging.log4j.Level;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput.Button;

public class Climber implements IDiagnosable {

	private static final int CLIMB_MOTOR = 93; //TODO: Set to real number
	private final double STOP_TURN_VALUE = 9000; //TODO: Needs to be determined
	private CANSparkMax climbMotor; //NEO motor to control climber
	private CANEncoder climbEncoder;

	public Climber() {
		climbMotor = new CANSparkMax(CLIMB_MOTOR, MotorType.kBrushless);
		climbMotor.setIdleMode(IdleMode.kBrake);

		climbEncoder = climbMotor.getEncoder();
	}

	public void launchClimber() {
			climbMotor.set(0.4d); //The appropriate speed will need to be found
			//Prevent motor from going too far; there is a hard stop (I think?), but we want to be sure
			if(climbEncoder.getPosition() >= STOP_TURN_VALUE) {
				climbMotor.stopMotor();
			}
		
	}

	public void update(Boolean enabled) {
		//As much security as a nuclear launch; ABSOLUTELY NO accidental climber triggers
		if(Robot.userInput.getController(0).getButtonPressed(Button.BUMPER_R)
		  && Robot.userInput.getController(1).getButtonPressed(Button.BUMPER_R)) {
			launchClimber();
		}
	}

	public void diagnose() {
		//Check to make sure climb encoder isn't returning some ridiculous value
		if(Math.abs(climbEncoder.getPosition()) < 1600000d) { //TODO: find a real test value range
			Robot.logger.log(Level.DEBUG, "Climber appears OK");
		} else {
			Robot.logger.log(Level.DEBUG, "Climber is broken");
		}

	}

}