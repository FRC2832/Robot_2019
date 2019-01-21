/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 * Elevator subsystem that includes a PID controller to control the elevator
 */
public class ElevatorPID extends PIDSubsystem {

    private final static int ELEVATOR_MOTOR = 5; //TODO: set to real number
    CANSparkMax elevatorMotor; 

    public static final double TOLERANCE = 1;

    public ElevatorPID() {
        super("ElevatorPID", 1, 0, 0.5);  //PID Values
        elevatorMotor = new CANSparkMax(ELEVATOR_MOTOR, MotorType.kBrushless);
        elevatorMotor.setIdleMode(IdleMode.kBrake);
        
        setAbsoluteTolerance(TOLERANCE);
        setOutputRange(-1, 1);
        setInputRange(Double.MIN_VALUE, Double.MAX_VALUE);
        setSetpoint(ElevatorHeights.LowHatch.getHeight());

        enable();

    }

    /**
     * Moves the elevator to the desired height
     * @param height use the ElevatorHeights enum to specify the height the elevator needs to go to
     */

    public void setElevatorHeight(ElevatorHeights height) {
        setSetpoint(height.getHeight());

    }

    /**
     * @return the height of the elevator in inches
     */

    public double getElevatorHeight() {
        return elevatorMotor.getEncoder().getPosition();
        //TODO: add math to convert to inch position of elevator
    }

    @Override
    public void initDefaultCommand() {
        // setDefaultCommand(new Command());
    }

    @Override
    protected double returnPIDInput() {
        return getElevatorHeight();
    }

    @Override
    protected void usePIDOutput(double output) {
        elevatorMotor.set(output);
    }

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

}
