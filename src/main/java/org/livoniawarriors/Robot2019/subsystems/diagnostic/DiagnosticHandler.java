package org.livoniawarriors.Robot2019.subsystems.diagnostic;

import java.util.Arrays;
import java.util.Map;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import org.livoniawarriors.Robot2019.subsystems.diagnostic.Diagnosable;
/**
 * A home for code used to diagnose the robot's problems
 * and determine what needs further investigation.
 * Depends on methods being annotated @Diagnosable
 */
public class DiagnosticHandler {

	/**
	 * Diagnose an individual method by testing specific values and seeing
	 * if they fall with in an acceptable range of expected values, as defined
	 * in an @Diagnosable annotation on the method
	 * @param subsystem The class where the method located
	 * @param name The name of the method to diagnose
	 * @param values The values to be tested
	 */
	public void diagnoseMethod(Class<?> subsystem, String name, Object... values) {
		try {
			boolean succeeded = false;
			Object s = subsystem.newInstance();
			System.out.println("Class: " + s.getClass().getName()); //Check
			Method allMethods[] = subsystem.getDeclaredMethods();
			System.out.println(Arrays.toString(allMethods));
			for(Method m : allMethods) {
				Type returnType = m.getReturnType();
				System.out.println("Return type: " + returnType); //Check return type
				String mName = m.getName();
				System.out.println("Attempting to test: " + mName); //Checking for correct name output
				if(name.equals(mName)) {
					System.out.println("Test target located: " + mName);
					if(m.isAnnotationPresent(Diagnosable.class)) {
						try {
							System.out.println(name + " is annotated: proceeding");
							Annotation annotation = m.getAnnotation(Diagnosable.class);
							Diagnosable diagnosable = (Diagnosable) annotation;
							Object result = m.invoke(s, values);
							System.out.println("Result: " + result.toString()); //Check result
							double range = diagnosable.range();
							double exp = diagnosable.exp();
							if(returnType.equals(double.class) || returnType.equals(float.class)
							|| returnType.equals(int.class)) {
								double dResult = (double) result;
								if(diagnosable.nonNegative() && dResult < 0) {
									succeeded = false;
								} else {
									succeeded = ((Math.abs(exp - dResult) < Math.abs(exp + range)) && 
								  	  (Math.abs(exp - dResult) > Math.abs(exp - range)));
								}
							} else {
								System.out.println("Could not diagnose: unknown return type.");
							}
						} catch(InvocationTargetException e) {
							System.err.println("An error had occured:");
							System.err.println(e.getMessage());
							System.err.println("Dave, this conversation can serve no purpose anymore.");
						}
					} else {
						System.out.println("Annotation is not present.");
					}
				} else {
					System.out.println(mName + " is not the target.");
					continue;
				}
				if(succeeded) {
					System.out.println("The method " + mName + " passed the diagnostic!");
				} else {
					System.out.println("The method " + mName + " failed the diagnostic!");
				}
			}
		//TODO: add better error handling
		} catch(IllegalAccessException | InstantiationException e) {
			System.err.println("An error had occured:");
			System.err.println(e.getMessage());
			System.err.println("Dave, this conversation can serve no purpose anymore.");
		}
	}
/*	public void DiagnposeSubsystem(Class<?> subsystem, ) {
//		try {
			Object[] value;
//			Object s = subsystem.newInstance();
			Method allMethods[] = subsystem.getDeclaredMethods();
			for(Method m : allMethods) {
				String name = m.getName();
				if(test.containsKey(name)) {
					value = test.get(name);
					diagnoseMethod(subsystem, name, );
				} else {
					continue;
				}
								
			}

//		} catch(InstantiationException | IllegalAccessException e) {

//		}
	}
*/
}
