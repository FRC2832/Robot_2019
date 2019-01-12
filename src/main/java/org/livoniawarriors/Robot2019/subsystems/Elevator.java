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

import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {

    private final static int ELEVATOR_MOTOR = 5; //TODO: set to real number

    CANSparkMax elevatorMotor; 

    public Elevator() {
        elevatorMotor = new CANSparkMax(ELEVATOR_MOTOR, MotorType.kBrushless);
        elevatorMotor.setIdleMode(IdleMode.kBrake);
    }

    public double getElevatorHeight() {
        return elevatorMotor.getEncoder().getPosition();
    }

    public void setElevatorMotor(double speed) {
        elevatorMotor.set(speed);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public enum elevatorHeights {
    
        LowHatch(0), LowPort(8.5),
        MidHatch(28), MidPort(36.5),
        TopHatch(56), TopPort(64.5),
        ShipPort(17);

        private final double height;

        elevatorHeights(double height) {
            this.height = height;
        }

        public double getHeight() {
            return height;
        }

    }

}
