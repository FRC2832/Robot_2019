/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.commands;

import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.subsystems.Elevator;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

public class MoveElevatorPID extends Command implements PIDOutput, PIDSource {


    private final double P = 1;
	private final double I = 0.0;
	private final double D = 0.5;
    private final double F = 0.0;
    
    private final double TOLERANCE = 2; // +- how many inches

    private PIDSourceType sourceType;
    private PIDController controller;

    private double targetHeight;

    public MoveElevatorPID(Elevator.elevatorHeights height) {
        sourceType = PIDSourceType.kDisplacement;
        requires(Robot.elevator);
        controller = new PIDController(P, I, D, F, this, this, 0.02);
        controller.setOutputRange(-0.7, 0.7);
		controller.setInputRange(-Double.MAX_VALUE, Double.MAX_VALUE);
		controller.setContinuous(true);
		controller.setAbsoluteTolerance(TOLERANCE);
        controller.disable();
        
        targetHeight = height.getHeight();
        
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        controller.reset();
        controller.setSetpoint(targetHeight);
        controller.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (Math.abs(Robot.elevator.getElevatorHeight() - targetHeight) < TOLERANCE) {
            //Log successfully reaching target height
            return true;
        } else {
            return false;
        }
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        controller.disable();
        Robot.elevator.setElevatorMotor(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {

    }

    @Override
	public void pidWrite(double output) {
        //This is the output of the PID
        Robot.elevator.setElevatorMotor(output);
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
        return Robot.elevator.getElevatorHeight();
    }
    
}
