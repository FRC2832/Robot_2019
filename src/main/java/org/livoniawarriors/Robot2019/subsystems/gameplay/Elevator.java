/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.subsystems.gameplay;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.apache.logging.log4j.Level;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.subsystems.diagnostic.IDiagnosable;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * Elevator not-subsystem that includes a PID controller to control the elevator
 */
public class Elevator implements PIDSource, PIDOutput, IDiagnosable {

	private final static int ELEVATOR_MOTOR = 5; //TODO: set to real number
	CANSparkMax elevatorMotor; 

	private static final double TOLERANCE = 1; // +- how many inches is acceptable
	private static final double MAX_MOTOR_SPEED = 0.7;
	private static final double P = 1.0;
	private static final double I = 0.0;
	private static final double D = 0.5;

	private PIDController pidController;
	private PIDSourceType sourceType;


	public Elevator() {
		elevatorMotor = new CANSparkMax(ELEVATOR_MOTOR, MotorType.kBrushless);
		elevatorMotor.setIdleMode(IdleMode.kBrake);

		pidController = new PIDController(P, I, D, this, this, 0.01);

		pidController.setAbsoluteTolerance(TOLERANCE);
		pidController.setOutputRange(-MAX_MOTOR_SPEED, MAX_MOTOR_SPEED);
		pidController.setInputRange(Double.MIN_VALUE, Double.MAX_VALUE);
		pidController.setSetpoint(ElevatorHeights.LowHatch.getHeight());
		pidController.setContinuous(true);

	}

	/**
	 * Moves the elevator to the desired height
	 * @param height use the ElevatorHeights enum to specify the height the elevator needs to go to
	 */
	public void setElevatorHeight(ElevatorHeights height) {
		pidController.setSetpoint(height.getHeight());

	}

	/**
	 * @return the height of the elevator in inches
	 */
	public double getElevatorHeight() {
		return elevatorMotor.getEncoder().getPosition() * (2 * Math.PI) / 7;
		//Pulley has a 1 inch radius and 2 pi circumfrence
		//The gearbox has a ratio of 7:1
		//Therefore to go from encoders to elevator height we multiply by 2pi and divide by 7
	}

	public void update(boolean isEnabled) {
		if (isEnabled) {
			if (pidController.isEnabled()) {
				pidController.enable();
			}   
		} else {
			if (!pidController.isEnabled()) {
				pidController.disable();
			}
			elevatorMotor.set(0.0);
		}
	}

	@Override
	public void pidWrite(double output) {
		elevatorMotor.set(output);
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		sourceType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return sourceType;
	}

	@Override
	public double pidGet() {
		return getElevatorHeight();
	}
	
	
	/**
	 * An enum containing the heights of where the elevator needs to go, in inches
	 */
	public enum ElevatorHeights {
		
		LowHatch(0), LowPort(8.5),
		MidHatch(28), MidPort(36.5),
		TopHatch(56), TopPort(64.5),
		ShipPort(17);

		private final double height;

		ElevatorHeights(double height) {
			this.height = height;
		}

		public double getHeight() {
			return height;
		}

	}
	@Override
	public void diagnose() {
		double testElevHeight = getElevatorHeight();
		if(testElevHeight >= 0 && testElevHeight <= 100) {
			System.out.println("Method getElevatorHeight() is reported as a success with the value of " + testElevHeight);
			Robot.logger.log(Level.ERROR, 
			  String.format("Method getElevatorHeight() returned value {0} and did not detect failure", testElevHeight));
		} else {
			System.out.println("Method getElevatorHeight() is reported as a failure with the value of " + testElevHeight);
			Robot.logger.log(Level.ERROR, 
			  String.format("Method getElevatorHeight() returned value {0} and DETECTED FAILURE!!!", testElevHeight));
		}
		
	}
}
