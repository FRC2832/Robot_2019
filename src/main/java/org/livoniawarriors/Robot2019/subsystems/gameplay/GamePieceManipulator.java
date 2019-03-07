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
import org.livoniawarriors.Robot2019.UserInput.Controllers;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * Game piece manipulator class
 */
public class GamePieceManipulator {

    private final static int RIGHT_INTAKE = 14;
    private final static int LEFT_INTAKE = 15;

    private final static int TILTER_UP = 2;
    private final static int TILTER_DOWN= 3;
    private final static int FLOWER_IN = 0;
    private final static int FLOWER_OUT = 1;
    private final static int EXTENDER = 4;


    private final static int ANALOG_INPUT_CHANNEL = 0;

    private DoubleSolenoid flower, tilter; 
    private Solenoid extender;
    private WPI_TalonSRX leftIntakeMotor, rightIntakeMotor;

    private AnalogInput ballSensor;

    private UserInput.Controller controller;

    private boolean intakeDown;

    private Thread extenderThread;

    public GamePieceManipulator() {
        flower = new DoubleSolenoid(FLOWER_IN, FLOWER_OUT);
        tilter = new DoubleSolenoid(TILTER_DOWN, TILTER_UP);
        extender = new Solenoid(EXTENDER);
        leftIntakeMotor = new WPI_TalonSRX(LEFT_INTAKE);
        rightIntakeMotor = new WPI_TalonSRX(RIGHT_INTAKE);
        leftIntakeMotor.setInverted(true);
        rightIntakeMotor.setInverted(true);
        ballSensor = new AnalogInput(ANALOG_INPUT_CHANNEL);
    
        controller = Robot.userInput.getController(Controllers.XBOX);
        
        intakeDown = false; //TODO: find out if intake starts up or down

        tilter.set(Value.kReverse);
        flower.set(Value.kReverse);
    }

    public void update(boolean isEnabled) {
        if (!isEnabled) {
            return;
        }
        
        //Move intake motors
        if (intakeDown) {
            //Move Left Intake Motor
            if (controller.getOtherAxis(1) != 0 && !hasBall()) {    
                leftIntakeMotor.set(controller.getOtherAxis(1)); 
            } else {
                leftIntakeMotor.set(0);

            }
            //Move Right Intake Motor
            if (controller.getOtherAxis(5) != 0 && hasBall()) {
                rightIntakeMotor.set(-1 * controller.getOtherAxis(5)); 
            } else {
                rightIntakeMotor.set(0);
            }
        }

        //Move Tilter
        if (controller.getPOV() == 0) {
            moveIntakeUp();
        } else if (controller.getPOV() == 180) {
            moveIntakeDown();
        }
        
        System.out.println(controller.getPOV());

        if (controller.getButtonPressed(Button.B)) {
            moveFlower();
        } 
        
        Robot.userInput.createValue("John", "Do I have a ball?", 2, hasBall());
    }

    public boolean doingBall() {
        return intakeDown;
    }

    public boolean hasBall() {
        return ballSensor.getVoltage() < 1.5;
    }

    private void moveFlower() {
        if (!intakeDown) {
            if (flower.get() == Value.kForward) {
                flower.set(Value.kReverse);
                pushHatch();
            } else {
                flower.set(Value.kForward);
            }
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
        if (extenderThread != null) {
            extenderThread.interrupt();
        }
        extenderThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                    extender.set(true);
                    Thread.sleep(1000);
                    extender.set(false);
                    extenderThread = null;
                } catch (InterruptedException e) {
                    Robot.logger.warn("Extender Thread interrupted", e);
                }
            }
        };
        extenderThread.start();
    }

}
