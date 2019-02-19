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

public class Climber {

	private static final int CLIMB_MOTOR = 13;
	private final double STOP_TURN_VALUE = 370; //*Should* be right, mathematically speaking
	private CANSparkMax climbMotor; //NEO motor to control climber
	private CANEncoder climbEncoder;

	public double climbMotorSpeed;

	public Climber() {
		climbMotor = new CANSparkMax(CLIMB_MOTOR, MotorType.kBrushless);
		climbMotor.setIdleMode(IdleMode.kBrake);

		climbEncoder = climbMotor.getEncoder();
	}
	public void init() {
		climbMotorSpeed = 0.8;
	}

	public void launchClimber() {
			
			//Prevent motor from going too far; there is a hard stop (I think?), but we want to be sure
			if(climbEncoder.getPosition() < STOP_TURN_VALUE) {
				climbMotor.set(climbMotorSpeed); //Appropriate speed will need to be found
			} else {
				climbMotor.set(0.0);
			}
		
	}

	public void update(boolean enabled) {
		if(!enabled) {
			return;	
		}
		//As much security as a nuclear launch; ABSOLUTELY NO accidental climber triggers
		//Maybe have a SmartDashboard button to "arm" the climber?
		//TODO: Set flightstick buttons
		if(Robot.userInput.getController(Controllers.XBOX).getButton(Button.BUMPER_R)
			&& Robot.userInput.getController(Controllers.XBOX).getButton(Button.BUMPER_L)) {
			if(Robot.gamePlay.getElevatorHeight() < 0.1) {
				launchClimber();
				System.out.println("Climber launch initiated; self-destructing");
			} else {
				System.out.println("CLIMBER will NOT run: elevator is up!");
				Robot.logger.log(Level.DEBUG, "Attempted climber run with elevator up");
			}
		}
		
	}

}
