/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.livoniawarriors.Robot2019.commands.Drive;
import org.livoniawarriors.Robot2019.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 * <b>ABANDON HOPE ALL YE WHO ENTER HERE</b><br><br>
 * Add your docs here.
 */
public class Logger {
    public static enum type {NUMBERS, STRINGS};
    public Map<Class<?>, Boolean> whitelist = Map.of(Drive.class, true);
    //                                     data
    //                        command      type   name       data
    public ArrayList<Map<Map<Class<?>, Map<type, String>>, String>> logData = new ArrayList<>();
    public void log(String a, String name, type t, Class<?> command) {
        if(whitelist.get(command)) {
            logData.add(Map.of(Map.of(command, Map.of(t, name)), a));
        }
    }

    public interface ILogInterface {
        public default void log(String a, String name, type t) {
            Robot.logger.log(a, name, t, this.getClass());
        }
    }

    public ArrayList<String> getData(type t) {
        ArrayList<String> data = new ArrayList<>();

        //isolate the type
        for(Map<Map<Class<?>, Map<type, String>>, String> element : logData) 
            for(Entry<Map<Class<?>, Map<type, String>>, String> element2 : element.entrySet()) 
                for(Entry<Class<?>, Map<type, String>> element3 : element2.getKey().entrySet()) 
                    for(Entry<type, String> element4 : element3.getValue().entrySet())
                        //check is the type is the desired one
                        if(element4.getKey().equals(t)) 
                            //add the data to the output array
                            data.add(element.get(element.get(Map.of(element2.getKey(), Map.of(element4.getKey(), element4.getValue())))));
        return data;
    }
    
    public ArrayList<String> getData(String name) {
        ArrayList<String> data = new ArrayList<>();

        //isolate the name
        for(Map<Map<Class<?>, Map<type, String>>, String> element : logData) 
            for(Entry<Map<Class<?>, Map<type, String>>, String> element2 : element.entrySet()) 
                for(Entry<Class<?>, Map<type, String>> element3 : element2.getKey().entrySet()) 
                    for(Entry<type, String> element4 : element3.getValue().entrySet())
                        //check if the name is the desired one
                        if(element4.getValue().equals(name)) 
                            //add the data to the output array
                            data.add(element.get(element.get(Map.of(element2.getKey(), Map.of(element4.getKey(), element4.getValue())))));
        return data;
    }

    public ArrayList<String> getData(Class<?> command) {
        ArrayList<String> data = new ArrayList<>();

        //isolate the command class
        for(Map<Map<Class<?>, Map<type, String>>, String> element : logData) 
            for(Entry<Map<Class<?>, Map<type, String>>, String> element2 : element.entrySet()) 
                for(Entry<Class<?>, Map<type, String>> element3 : element2.getKey().entrySet()) 
                    //check if the command class is the desired one
                    if(element3.getKey().equals(command))
                        //get all the way down to the lowest layers (type & name)
                        for(Entry<type, String> element4 : element3.getValue().entrySet())
                            //add the data to the output array 
                            data.add(element.get(element.get(Map.of(element2.getKey(), Map.of(element4.getKey(), element4.getValue())))));
        return data;
    }
 }
