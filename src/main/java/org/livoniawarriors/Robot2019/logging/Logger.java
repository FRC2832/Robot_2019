/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * <b>ABANDON HOPE ALL YE WHO ENTER HERE</b><br><br>
 * Add your docs here.
 */
public class Logger {
    private BufferedWriter writer;

    public Logger() {
        try {
            this.writer = new BufferedWriter(new FileWriter(new File("logData.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void log(String name, double data, SeverityLevel s, Tag...t) {
        try {
            writer.write(formatData(name, data, s, t));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String formatData(String name, double data, SeverityLevel s, Tag...t) {
        String output = "Name:";
        output += name;
        output += "; Severity: " + s.name();
        if(!t[0].equals(null)) {
            output += "; Tags: " + t[0];
            for(Tag t0 : Arrays.asList(t).subList(1, t.length)) {
                output += ", " + t0.name();
            }
        }
        output += "; Value: " + Double.toString(data);
        return output;
    }

 }
