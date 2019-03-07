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
import org.livoniawarriors.Robot2019.UserInput.Controllers;
import org.livoniawarriors.Robot2019.UserInput;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * Elevator not-subsystem that includes a PID controller to control the elevator
 */
public class Elevator implements PIDSource, PIDOutput {

    private final static int ELEVATOR_MOTOR = 22;
    CANSparkMax elevatorMotor;

    private static final double TOLERANCE = 1; // +- how many inches is acceptable
    private static final double MAX_MOTOR_SPEED = 0.7;

    private static final double movingP = 0.9;
    private static final double movingI = 0.0;
    private static final double movingD = 0.5;
    private static final double movingF = 0.7;

    private static final double maintainerP = 0.5;
    private static final double maintainerI = 0.0;
    private static final double maintainerD = 0.5;
    private static final double maintainerF = 0.2;

    private PIDController pidController;
    private PIDSourceType sourceType;

    private UserInput.Controller controller;

    private boolean manual = false;
    private boolean movingPID;
    private ElevatorHeights currentSetHeight;

    public Elevator() {
        elevatorMotor = new CANSparkMax(ELEVATOR_MOTOR, MotorType.kBrushless);
        elevatorMotor.setIdleMode(IdleMode.kBrake);
        elevatorMotor.setOpenLoopRampRate(0.7);

        pidController = new PIDController(movingP, movingI, movingD, movingF, this, this, 0.01);

        setPIDSourceType(PIDSourceType.kDisplacement);

        pidController.setAbsoluteTolerance(TOLERANCE);
        pidController.setOutputRange(-MAX_MOTOR_SPEED / 4, MAX_MOTOR_SPEED);
        pidController.setInputRange(-Double.MAX_VALUE, Double.MAX_VALUE);
        pidController.setContinuous(true);


        controller = Robot.userInput.getController(Controllers.XBOX);

        currentSetHeight = ElevatorHeights.LowHatch;

        setElevatorHeight(ElevatorHeights.LowHatch);
    }

    /**
     * Moves the elevator to the desired height
     * 
     * @param height use the ElevatorHeights enum to specify the height the elevator
     *               needs to go to
     */
    public void setElevatorHeight(ElevatorHeights height) {
        pidController.disable();
        pidController.setSetpoint(height.getHeight());
        currentSetHeight = height;
        setToMovingPID();
    }

    private void setToMovingPID() {
        pidController.setPID(movingP, movingI, movingD, movingF);
        pidController.reset();
        //System.out.println("PIDController is " + (pidController.isEnabled() ? "Enabled" : "Disabled"));
        pidController.enable();
        //System.out.println("PIDController is " + (pidController.isEnabled() ? "Enabled" : "Disabled"));
        movingPID = true;
    }

    private void setToMaintiningPID() {
        pidController.disable();
        pidController.setPID(maintainerP, maintainerI, maintainerD, maintainerF);
        pidController.reset();
        pidController.enable();
        movingPID = false;
    }

    /**
     * @return the height of the elevator in inches TODO: update with new mechanical
     *         information
     */
    public double getElevatorHeight() {
        return elevatorMotor.getEncoder().getPosition() * (2 * Math.PI) / 49;
        // Pulley has a 1 inch radius and 2 pi circumfrence
        // The gearbox has a ratio of 49:1
        // Therefore to go from encoders to elevator height we multiply by 2pi and
        // divide by 49
    }

    public void update(boolean isEnabled) {
        if (!isEnabled) {
            if (pidController.isEnabled()) {
                pidController.disable();
            }
            elevatorMotor.set(0.0);
            return;
        }

        if (!manual) {
            
            //PID Mode
            if (!pidController.isEnabled()) {
                pidController.enable();
            }
            if (pidController.onTarget() && movingPID) {
                setToMaintiningPID();
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
            } else if (controller.getButtonPressed(Button.B)) {
                manual = !manual;
            }
        } else {

            //Manual Mode
            if (controller.getOtherAxis(Robot.userInput.R_TRIGGER) != 0) {
                elevatorMotor.set(controller.getOtherAxis(Robot.userInput.R_TRIGGER) * 1);
                System.out.println("Moving motor up forwards");
            } else if (controller.getOtherAxis(Robot.userInput.L_TRIGGER) != 0) {
                elevatorMotor.set(-1 * controller.getOtherAxis(Robot.userInput.L_TRIGGER) * 1);
                System.out.println("Moving motor up backwards");
            } else {
                elevatorMotor.set(0);
            }
        }

        //System.out.println("Current Elevator Height: " + getElevatorHeight());
        Robot.userInput.createValue("John", "Elevator Height", getElevatorHeight());
        Robot.userInput.createValue("John", "Set Height", currentSetHeight);
        Robot.userInput.createValue("John", "PID", movingPID);

    }

    @Override
    public void pidWrite(double output) {
        if (!manual) {
            elevatorMotor.set(output);
            //System.out.println("Current motor output: " + output);
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

    public void diagnose() {
        double testElevHeight = getElevatorHeight();
        if (testElevHeight >= 0 && testElevHeight <= 100) {
            System.out.println("Method getElevatorHeight() is reported as a success with the value of " + testElevHeight);
            Robot.logger.log(Level.ERROR, String.format("Method getElevatorHeight() returned value {0} and did not detect failure", testElevHeight));
        } else {
            System.out.println("Method getElevatorHeight() is reported as a failure with the value of " + testElevHeight);
            Robot.logger.log(Level.ERROR, String.format("Method getElevatorHeight() returned value {0} and DETECTED FAILURE!!!", testElevHeight));
        }

    }
}
