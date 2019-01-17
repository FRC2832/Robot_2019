/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.diagnostic;

import java.util.ArrayList;
import java.util.Map;

import edu.wpi.first.wpilibj.command.Subsystem;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import org.livoniawarriors.Robot2019.diagnostic.Diagnosable;

/**
 * A home for code used to diagnose the robot's problems
 * and determine what needs further investigation
 */
public class DiagnosticHandler {

  /**
   * Run a diagnostic on a subsystem (or command, I suppose?) by reading
   * an input value and comparing to the expected value as defined by
   * the annotation "Diagnosable" on a function.  If the the resultant
   * value is within the expected value range, the method passed.  If
   * not, the method failed and should be examined and tested further.
   * @param subsystem is the subsystem (or other class) you wish to diagnose
   * @param testVal is an array of values to be passed to test methods.
   */
	public void diagnoseSubsystem(Class<?> subsystem, Object... testVal) {
    	try {
    		Class<?> subsys = Class.forName(subsystem.toString());

    		//Get all methods in the Subsystem
    		Method methodList[] = subsys.getDeclaredMethods();

    		ArrayList<Object> results = new ArrayList();

    		//Iterate over every method
    		for(int i = 0; i < methodList.length; i++) {
        		Method func = methodList[i];
				Type returnType = func.getReturnType();
				Boolean success;

        		//List parameter types of func
        		Class<?>[] paramType = func.getParameterTypes();
        		Object[] obj = new Object[paramType.length];

        		//Check if method is Diagnosable
        		if(func.isAnnotationPresent(Diagnosable.class)) {
        			Annotation annotation = func.getAnnotation(Diagnosable.class);
        			Diagnosable diag = (Diagnosable) annotation;
          
        			//Make sure diagnostic is turned on
        			if(diag.diagOn()) {
            			//Get params and then run the test
            			for(int j = 0; j < paramType.length; j++) {
                			obj[j] = paramType[j].newInstance();
              			} 
            			//Access specific result by results.get(i)
            			results.add(func.invoke(func, obj));
        
						System.out.println(results.get(i));

						//If return value is numeric
						//TODO: figure out what is going on here
        				if(returnType == double.class || returnType == int.class || returnType == float.class) {
							double trialVal;
							if(results.get(i) instanceof double[]) {
								trialVal = (double) results.get(i);
							} else if(results.get(i) instanceof int[]) {
								trialVal = (int) results.get(i);
							} else if(results.get(i) instanceof float[]) {
								trialVal = (float) results.get(i);
							} else {
								trialVal = 0;
							}

						
        					if(trialVal > diag.expValL() && trialVal < diag.expValH()) {
								success = true;
          					} else {
								success = false;			
							}
						} else if(returnType == boolean.class) {
							boolean trialValB = (boolean) results.get(i);
							if(trialValB == diag.expValBool()) {
								success = true;
							} else {
								success = false;
							}
						} else {

						}
										
					} else {

					}
				} else {

				}
			}
		} catch(Throwable e) {
				//TODO: add proper logging
				System.err.println(e);
		}
	}
}







