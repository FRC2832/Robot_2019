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


    private final static int ANALOG_INPUT_CHANNEL = 0;

    private DoubleSolenoid flower, tilter, leftExtender, rightExtender;
    private WPI_TalonSRX leftIntakeMotor, rightIntakeMotor;

    private AnalogInput ballSensor;

    private UserInput.Controller controller;

    private boolean intakeDown;

    private Thread extenderThread;

    public GamePieceManipulator() {
        flower = new DoubleSolenoid(FLOWER_IN, FLOWER_OUT);
        tilter = new DoubleSolenoid(TILTER_DOWN, TILTER_UP);
        leftExtender = new DoubleSolenoid(LEFT_EXTENDER_IN, LEFT_EXTENDER_OUT);
        rightExtender = new DoubleSolenoid(RIGHT_EXTENDER_IN, RIGHT_EXTENDER_OUT);
        leftIntakeMotor = new WPI_TalonSRX(LEFT_INTAKE);
        rightIntakeMotor = new WPI_TalonSRX(RIGHT_INTAKE);
        rightIntakeMotor.follow(leftIntakeMotor);
        rightIntakeMotor.setInverted(true);

        ballSensor = new AnalogInput(ANALOG_INPUT_CHANNEL);
    
        controller = Robot.userInput.getController(Controllers.XBOX);
        
        intakeDown = false; //TODO: find out if intake starts up or down
    }

    public void update(boolean isEnabled) {
        if (!isEnabled) {
            return;
        }

        //Intake
        if (controller.getOtherAxis(2) != 0 && !hasBall()) {    
            leftIntakeMotor.set(controller.getOtherAxis(2));
        } else {
            leftIntakeMotor.set(0);
        }

        //Expel
        if (controller.getOtherAxis(3) != 0 && hasBall()) {
            leftIntakeMotor.set(-1 * controller.getOtherAxis(3));
        } else {
            leftIntakeMotor.set(0);
        }

        //Variable Control
        //Commented out methods are discrete solinoid control
        if (flower.get() == Value.kReverse) {
            if (controller.getButton(Button.Y)) {
                //moveIntakeUp();
                tilter.set(Value.kReverse);
            } else if (controller.getButton(Button.B)) {
                //moveIntakeDown();
                tilter.set(Value.kForward);
            } else {
                tilter.set(Value.kOff);
            }
        } else {
            tilter.set(Value.kReverse);
        }
        
        if (controller.getButtonPressed(Button.A)) {
            moveFlower();
        } 
        
        Robot.userInput.createValue("John", "Do I have a ball?", 2, hasBall());
    }

    public boolean hasBall() {
        return ballSensor.getVoltage() < 1.5;
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
                    Thread.sleep(200);
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
