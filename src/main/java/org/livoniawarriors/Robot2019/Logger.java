/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Add your docs here.
 */
public class Logger {
    public Map<Class<?>, Boolean> whitelist = Map.of(Drive.class, true);
    public void log(String a, Class<?> command) {
        if(whitelist.get(command)) {
            System.out.println(a);
        }
    }

    public interface ILogInterface {
        public default void log(String a) {
            Robot.logger.log(a, this.getClass());
        }
    }
 }
