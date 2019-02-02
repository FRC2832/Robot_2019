/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.subsystems.gameplay;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * Add your docs here.
 */
public class GamePieceManipulatorJake {

    private final static int RIGHT_INTAKE = 14;
    private final static int LEFT_INTAKE = 15;
    private final static int TILTER_UP = 4;
    private final static int TILTER_DOWN = 5;
    private final static int FLOWER_IN = 1; //TODO: Set to real value
    private final static int FLOWER_OUT = 2;

    private DoubleSolenoid flower, tilter;
    private WPI_TalonSRX leftIntakeMotor, rightIntakeMotor;

    public GamePieceManipulatorJake() {
        flower = new DoubleSolenoid(FLOWER_IN, FLOWER_OUT);
        tilter = new DoubleSolenoid(TILTER_DOWN, TILTER_UP);
        leftIntakeMotor = new WPI_TalonSRX(LEFT_INTAKE);
        rightIntakeMotor = new WPI_TalonSRX(RIGHT_INTAKE);
    }
}
