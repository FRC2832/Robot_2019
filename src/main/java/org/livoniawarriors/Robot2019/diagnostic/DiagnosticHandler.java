/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.diagnostic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    		//Get all methods in the Subsystem
			Method methods[] = subsystem.getDeclaredMethods();
			Object s = subsystem.newInstance();
			//Keep track of which test values are used: true if they have
			//been used, false if they haven't
			Map<Object, Boolean> values = new HashMap<>();
			//Populate map
			for(Object o : testVal) {
				values.put(o, false);
			}

    		//Iterate over every method
    		for(Method m : methods) {
				Type returnType = m.getReturnType();
				String name = m.getName();
				List<Object> methodVals = new ArrayList<Object>();
				Boolean success = false;
				String report = "failed";

        		//List parameter types of func
        		Class<?>[] paramType = m.getParameterTypes();
        		//Check if method is Diagnosable
        		if(m.isAnnotationPresent(Diagnosable.class)) {
        			Annotation annotation = m.getAnnotation(Diagnosable.class);
        			Diagnosable diagnosable = (Diagnosable) annotation;
          
        			//Make sure diagnostic is turned on
        			if(diagnosable.diagOn()) {
            			//Get params and then run the test
            			for(int i = 0; i < paramType.length; i++) {
							if(values.get(testVal[i]).equals(false)) {
								methodVals.add(testVal[i]);
								values.put(testVal[i], true);
							}
							
						} 
						methodVals.toArray(new Object[0]);
						//Invoke method with array of values
						Object result = m.invoke(s, methodVals);
						//Log results
						System.out.println(result);

						//Test the values
						if(returnType.equals(double.class) || returnType.equals(int.class)) {
							Double r = (Double) result;
							success = (r <= diagnosable.expValH() && r >= diagnosable.expValL());
							report = Double.toString(r);
						} else if(returnType.equals(float.class)) {
							//Convert to allow comparing float result and double exp values
						} else if(returnType.equals(boolean.class)) {
							Boolean r = (Boolean) result;
							if(diagnosable.expValBool() == r) {
								success = true;
							} else {
								success = false;
							}
							report = Boolean.toString(r);
						} else {
							success = false;
							System.out.println("Failed");
						}

					}
				}
				if(success) {
					System.out.println(name + "ran successfully");
					System.out.println("The resultant value was " + report);
				} else {
					System.out.println(name + "failed with the value" + report);
				}
			}
		} catch(IllegalAccessException e) {
			//TODO: add proper logging
			System.out.println(e);
		} catch(InstantiationException e) {
			System.out.println(e);
		} catch(InvocationTargetException e) {
			System.out.println(e);
		}
	}
}







