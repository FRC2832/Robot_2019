/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrain extends Subsystem {
    final static int DRIVE_MOTOR_FR = 69;
    final static int DRIVE_MOTOR_FL = 420;
    final static int DRIVE_MOTOR_BR = 666;
    final static int DRIVE_MOTOR_BL = 42;

    private Talon fr, fl, br, bl;
    public DriveTrain() {
        fr = new Talon(DRIVE_MOTOR_FR);
        fl = new Talon(DRIVE_MOTOR_FL);
        br = new Talon(DRIVE_MOTOR_BR);
        bl = new Talon(DRIVE_MOTOR_BL);
        
    }
    @Override
    protected void initDefaultCommand() {

    }

}
