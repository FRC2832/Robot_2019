/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <b>ABANDON HOPE ALL YE WHO ENTER HERE</b><br><br>
 * Add your docs here.
 */
public class Logger {
    public static enum tag {NONE};
    private BufferedWriter writer;

    public Logger() {
        try {
            this.writer = new BufferedWriter(new FileWriter(new File("logData.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void log(String name, double data) {
        try {
            writer.write(formatData(name, tag.NONE, tag.NONE, data));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String name, tag t1, double data) {
        try {
            writer.write(formatData(name, t1, tag.NONE, data));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String name, tag t1, tag t2, double data) {
        try {
            writer.write(formatData(name, t1, t2, data));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String formatData(String name, tag t1, tag t2, double data) {
        String output = "Name:";
        output += name;
        output += t1.equals(tag.NONE) ? "" : "; Tags:" + t1;
        output += t2.equals(tag.NONE) ? "" : ", " + t2;
        output += "; Value: " + Double.toString(data);
        return output;
    }

 }
