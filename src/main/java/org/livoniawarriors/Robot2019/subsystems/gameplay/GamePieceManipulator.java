/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.subsystems.gameplay;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput;
import org.livoniawarriors.Robot2019.UserInput.Button;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * Add your docs here.
 */
public class GamePieceManipulator {

    private final static int RIGHT_INTAKE = 14;
    private final static int LEFT_INTAKE = 15;

    private final static int TILTER_UP = 4;
    private final static int TILTER_DOWN = 5;
    private final static int FLOWER_IN = 0;
    private final static int FLOWER_OUT = 1;
    private final static int LEFT_EXTENDER_IN = 2;
    private final static int LEFT_EXTENDER_OUT = 3;
    private final static int RIGHT_EXTENDER_IN = 6;
    private final static int RIGHT_EXTENDER_OUT = 7;


    private final static int DIGITAL_INPUT_CHANNEL = 0;

    private DoubleSolenoid flower, tilter, leftExtender, rightExtender;
    private WPI_TalonSRX leftIntakeMotor, rightIntakeMotor;

    private DigitalInput ballSensor;
    private DigitalOutput maybeBallSensor;

    private UserInput.Controller controller;

    private boolean intakeDown;

    private Thread extenderThread;

    private boolean usingBallSensor = false;

    public GamePieceManipulator() {
        flower = new DoubleSolenoid(FLOWER_IN, FLOWER_OUT);
        tilter = new DoubleSolenoid(TILTER_DOWN, TILTER_UP);
        leftExtender = new DoubleSolenoid(LEFT_EXTENDER_IN, LEFT_EXTENDER_OUT);
        rightExtender = new DoubleSolenoid(RIGHT_EXTENDER_IN, RIGHT_EXTENDER_OUT);
        leftIntakeMotor = new WPI_TalonSRX(LEFT_INTAKE);
        rightIntakeMotor = new WPI_TalonSRX(RIGHT_INTAKE);
        rightIntakeMotor.follow(leftIntakeMotor);
        rightIntakeMotor.setInverted(true);

        ballSensor = new DigitalInput(DIGITAL_INPUT_CHANNEL);
        maybeBallSensor = new DigitalOutput(DIGITAL_INPUT_CHANNEL);
    
        controller = Robot.userInput.getController(1);
        
        intakeDown = false; //TODO: find out if intake starts up or down
    }

    public void update(boolean isEnabled) {
        if (!isEnabled) {
            return;
        }

        //Code to move intake motors
        //if using the proximity sensor, only let the intake motors move in if there isn't a ball
        //and out if there is a ball
        //if there isn't a proximity sensor, just move the motors based on controls
        //only need to set the left motor because the right is set to follow it and is inverted
        if (usingBallSensor) {
            if (controller.getTriggerAxis(Hand.kLeft) != 0 && !hasBall()) {   
                //Intake 
                leftIntakeMotor.set(controller.getTriggerAxis(Hand.kLeft));

            } else if (controller.getTriggerAxis(Hand.kRight) != 0 && hasBall()) {
                //Expel
                leftIntakeMotor.set(-1 * controller.getTriggerAxis(Hand.kRight));
            
            } else {
                leftIntakeMotor.set(0);
            }
        } else {
            if (controller.getTriggerAxis(Hand.kLeft) != 0) {   
                //Intake 
                leftIntakeMotor.set(controller.getTriggerAxis(Hand.kLeft));

            } else if (controller.getTriggerAxis(Hand.kRight)!= 0) {
                //Expel
                leftIntakeMotor.set(-1 * controller.getTriggerAxis(Hand.kRight));
            
            } else {
                leftIntakeMotor.set(0);
            }
        }


        //descrete Control
        //Commented out is variable control
        //if (flower.get() == Value.kReverse) {
            if (controller.getButton(Button.Y)) {
                moveIntakeUp();
                //tilter.set(Value.kReverse);
            } else if (controller.getButton(Button.B)) {
                moveIntakeDown();
                //tilter.set(Value.kForward);
            } else {
                //tilter.set(Value.kOff);
            }
        //} else {
        //    tilter.set(Value.kReverse);
        //}
        
        if (controller.getButtonPressed(Button.A) && tilter.get() == Value.kReverse) {
            moveFlower();
        } 
        
        Robot.userInput.createValue("John", "Do I have a ball?", 7, hasBall());
    }

    public boolean hasBall() {
        return ballSensor.get();
        //return false;
    }

    private void moveFlower() {
        if (flower.get() == Value.kForward) {
            flower.set(Value.kReverse);
            pushHatch();
        } else {
            flower.set(Value.kForward);
        }
    }

    public void moveIntakeUp() {
        if (flower.get() == Value.kReverse) {
            tilter.set(Value.kReverse);
            intakeDown = false;
        }
    }

    public void moveIntakeDown() {
        if (flower.get() == Value.kReverse) {
            tilter.set(Value.kForward);
            intakeDown = true;
        }
    }

    public void pushHatch() {
        leftExtender.set(Value.kForward);
        rightExtender.set(Value.kForward);

        if (extenderThread != null) {
            extenderThread.interrupt();
        }
        extenderThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200); //Amount of time between pushing a retracting the extenders
                    leftExtender.set(Value.kReverse);
                    rightExtender.set(Value.kReverse);
                    extenderThread = null;
                } catch (InterruptedException e) {
                    Robot.logger.error("Extender Thread interrupted", e);
                }
            }
        };
        extenderThread.start();
    }

}
