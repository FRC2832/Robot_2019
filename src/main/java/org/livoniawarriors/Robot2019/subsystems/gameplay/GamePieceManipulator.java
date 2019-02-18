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
    private final static int FLOWER_IN = 2;
    private final static int FLOWER_OUT = 3;
    private final static int ACCORDION_IN = 6;
    private final static int ACCORDION_OUT = 7;

    private final static int ANALOG_INPUT_CHANNEL = 0;

    private DoubleSolenoid flower, tilter, accordion;
    private WPI_TalonSRX leftIntakeMotor, rightIntakeMotor;

    private AnalogInput ballSensor;

    private UserInput.Controller controller;

    boolean intakeDown;

    public GamePieceManipulator() {
        flower = new DoubleSolenoid(FLOWER_IN, FLOWER_OUT);
        tilter = new DoubleSolenoid(TILTER_DOWN, TILTER_UP);
        accordion = new DoubleSolenoid(ACCORDION_IN, ACCORDION_OUT);
        leftIntakeMotor = new WPI_TalonSRX(LEFT_INTAKE);
        rightIntakeMotor = new WPI_TalonSRX(RIGHT_INTAKE);
        rightIntakeMotor.follow(leftIntakeMotor);
        rightIntakeMotor.setInverted(true);

        ballSensor = new AnalogInput(ANALOG_INPUT_CHANNEL);
    
        controller = Robot.userInput.getController(1);
        
        intakeDown = false; //TODO: find out if intake starts up or down
    }

    public void update(boolean isEnabled) {
        if (!isEnabled) {
            return;
        }

        if (intakeDown) {

            //Intake
            if (controller.getTriggerAxis(Hand.kLeft) != 0 && !hasBall()) {    
                leftIntakeMotor.set(controller.getTriggerAxis(Hand.kLeft));
            } else {
                leftIntakeMotor.set(0);
            }

            //Expel
            if (controller.getTriggerAxis(Hand.kRight) != 0 && hasBall()) {
                leftIntakeMotor.set(-1 * controller.getTriggerAxis(Hand.kRight));
            } else {
                leftIntakeMotor.set(0);
            }

        }

        if (controller.getButton(Button.Y)) {
            moveIntakeUp();
        } else if (controller.getButton(Button.B)) {
            moveIntakeDown();
        } else if (controller.getButtonPressed(Button.A)) {
            moveFlower();
        } else if (controller.getButtonPressed(Button.X)) {
            moveAccordion();
        }
        

        //System.out.println(hasBall() ? "I have a ball!!" : "I don't have a ball");
    }

    public boolean hasBall() {
        return ballSensor.getVoltage() < 1.5;
    }

    private void moveFlower() {
        if (accordion.get() == Value.kReverse) {
            flower.set(flower.get() == Value.kReverse ? Value.kReverse : Value.kForward);
        }
    }

    private void moveAccordion() {
        if (!intakeDown) {
            accordion.set(accordion.get() == Value.kReverse ? Value.kForward : Value.kReverse);
        } else {
            moveIntakeUp();
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch(InterruptedException e) {
                        Robot.logger.error("Thread failed to sleep ", e);
                    }
                    accordion.set(accordion.get() == Value.kReverse ? Value.kForward : Value.kReverse);
                }
            }.start();
        }
    }

    public void moveIntakeUp() {
        if (flower.get() == Value.kReverse && accordion.get() == Value.kForward) {
            tilter.set(Value.kReverse);
            intakeDown = false;
        }
    }

    public void moveIntakeDown() {
        if (flower.get() == Value.kReverse && accordion.get() == Value.kForward) {
            tilter.set(Value.kForward);
            intakeDown = true;
        }
    }

}
