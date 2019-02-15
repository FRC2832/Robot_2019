/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.subsystems.gameplay;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * Add your docs here.
 */
public class ElevatorTest implements PIDOutput, PIDSource {

    CANSparkMax elevatorMotor;

    PIDController controller;
    PIDSourceType sourceType;

    public ElevatorTest() {
        elevatorMotor = new CANSparkMax(22, MotorType.kBrushless);

        controller = new PIDController(0.9, 0.01, 0.5, 0.0007, this, this, 0.01);

        controller.setAbsoluteTolerance(1);
        controller.setOutputRange(-0.7, 0.7);
        controller.setInputRange(Double.MIN_VALUE, Double.MAX_VALUE);
        controller.setContinuous(true);

        controller.setSetpoint(10);

        controller.enable();
    }

    public double getElevatorHeight() {
        return elevatorMotor.getEncoder().getPosition();
    }

    @Override
    public void pidWrite(double output) {
        elevatorMotor.set(output);
        System.out.println("Current motor output: " + output);
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        sourceType = pidSource;
        //System.out.println("SET THE SOURCE TYPE");
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return sourceType;
    }

    @Override
    public double pidGet() {
        return getElevatorHeight();
    }
}
