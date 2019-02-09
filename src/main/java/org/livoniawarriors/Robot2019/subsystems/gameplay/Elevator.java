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
import org.livoniawarriors.Robot2019.UserInput;
import org.livoniawarriors.Robot2019.UserInput.Button;
import org.livoniawarriors.Robot2019.subsystems.diagnostic.IDiagnosable;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * Elevator not-subsystem that includes a PID controller to control the elevator
 */
public class Elevator implements PIDSource, PIDOutput, IDiagnosable {

	private final static int ELEVATOR_MOTOR = 22;
	CANSparkMax elevatorMotor; 

	private static final double TOLERANCE = 1; // +- how many inches is acceptable
	private static final double MAX_MOTOR_SPEED = 0.7;
    
    private static final double movingP = 10.0;
	private static final double movingI = 0.0;
    private static final double movingD = 0.5;
    private static final double movingF = 0.7;

    private static final double maintainerP = 1.0;
	private static final double maintainerI = 0.0;
    private static final double maintainerD = 0.5;
    private static final double maintainerF = 0.2;

	private PIDController pidController;
    private PIDSourceType sourceType;
    
    private UserInput.Controller controller;

    private boolean manual = true;
    private boolean movingPID;
    private ElevatorHeights currentSetHeight;

	public Elevator() {
		elevatorMotor = new CANSparkMax(ELEVATOR_MOTOR, MotorType.kBrushless);
		elevatorMotor.setIdleMode(IdleMode.kBrake);

        pidController = new PIDController(movingP, movingI, movingD, movingF, this, this, 0.01);
 
		pidController.setAbsoluteTolerance(TOLERANCE);
		pidController.setOutputRange(-MAX_MOTOR_SPEED, MAX_MOTOR_SPEED);
		pidController.setInputRange(Double.MIN_VALUE, Double.MAX_VALUE);
        pidController.setContinuous(true);

        controller = Robot.userInput.getController(0);
        
        currentSetHeight = ElevatorHeights.LowHatch;

        setElevatorHeight(ElevatorHeights.LowHatch);
	}

	/**
	 * Moves the elevator to the desired height
	 * @param height use the ElevatorHeights enum to specify the height the elevator needs to go to
	 */
	public void setElevatorHeight(ElevatorHeights height) {
        pidController.setSetpoint(height.getHeight());
        pidController.reset();
        currentSetHeight = height;
        setToMovingPID();
        pidController.enable();
    }
    
    private void setToMovingPID() {
        pidController.setPID(movingP, movingI, movingD, movingF);
        movingPID = true;
    }
    
    private void setToMaintiningPID() {
        pidController.setPID(maintainerP, maintainerI, maintainerD, maintainerF);
        movingPID = false;
    }

	/**
	 * @return the height of the elevator in inches
     * TODO: update with new mechanical information
	 */
	public double getElevatorHeight() {
		return elevatorMotor.getEncoder().getPosition() * (2 * Math.PI) / 21;
		//Pulley has a 1 inch radius and 2 pi circumfrence
		//The gearbox has a ratio of 21:1
		//Therefore to go from encoders to elevator height we multiply by 2pi and divide by 21
	}

	public void update(boolean isEnabled) {
        if (!manual) {
            if (isEnabled) {
                if (!pidController.isEnabled()) {
                    if (pidController.onTarget()) {
                        setToMaintiningPID();
                    }
                }

                if (controller.getButtonPressed(Button.A)) {
                    setElevatorHeight(ElevatorHeights.LowHatch);
                    System.out.println("Setting elevator to Low Hatch");
                } else if (controller.getButtonPressed(Button.X)) {
                    setElevatorHeight(ElevatorHeights.MidHatch);
                    System.out.println("Setting elevator to Mid Hatch");
                } else if (controller.getButtonPressed(Button.Y)) {
                    setElevatorHeight(ElevatorHeights.TopHatch);
                    System.out.println("Setting elevator to Top Hatch");
                }
                
            } else {
                if (pidController.isEnabled()) {
                    pidController.disable();
                }
                elevatorMotor.set(0.0);
            }

        } else {
            if (controller.getTriggerAxis(Hand.kRight) != 0) {
                elevatorMotor.set(controller.getTriggerAxis(Hand.kRight) * MAX_MOTOR_SPEED);
            } else if (controller.getTriggerAxis(Hand.kLeft) != 0) {
                elevatorMotor.set(-1 * controller.getTriggerAxis(Hand.kLeft) * MAX_MOTOR_SPEED);
                System.out.println("Moving motor up " + controller.getTriggerAxis(Hand.kLeft));
            } else {
                elevatorMotor.set(0);
            }
        }

        System.out.println("Current Elevator Height: " + getElevatorHeight());
        
	}

	@Override
	public void pidWrite(double output) {
        if (!manual) {
            elevatorMotor.set(output);
        }
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
