package org.livoniawarriors.Robot2019.subsystems.gameplay;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput.Button;

public class Climber {

	private static final int CLIMB_MOTOR = 13;
	private final double STOP_TURN_VALUE = 9000; //TODO: Needs to be determined
	private CANSparkMax climbMotor; //NEO motor to control climber
	private CANEncoder climbEncoder;

	public double climbMotorSpeed;

	public Climber() {
		climbMotor = new CANSparkMax(CLIMB_MOTOR, MotorType.kBrushless);
		climbMotor.setIdleMode(IdleMode.kBrake);

		climbEncoder = climbMotor.getEncoder();
	}
	public void init() {
		climbMotorSpeed = 0.2; //TODO: set to value retrieved from dashboard
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
		if(Robot.userInput.getController(0).getButton(Button.BUMPER_R)
			&& Robot.userInput.getController(1).getButton(Button.BUMPER_R)) {
			if(GamePlay.elevator.getElevatorHeight() == 0) {
				launchClimber();
			} else {
				System.out.println("CLIMBER will NOT run: elevator is up!");
			}
		}
		
	}

}
