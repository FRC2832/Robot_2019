/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import java.util.ArrayList;

import org.livoniawarriors.Robot2019.diagnostic.DiagnosticHandler;
import org.livoniawarriors.Robot2019.diagnostic.Diagnosable;
/**
 * Add your docs here.
 */
public class ExampleDiag extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  // A method that gives some value
  @Diagnosable(expValH = 10, expValL = -10)
  public double giveValue(double value) {

    return value * value;
  }

  

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
