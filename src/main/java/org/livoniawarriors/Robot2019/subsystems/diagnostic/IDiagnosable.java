package org.livoniawarriors.Robot2019.subsystems.diagnostic;

import java.lang.reflect.Method;

public interface IDiagnosable {
    
    /**
     * HOW TO USE THIS TOOL: <p>
     * Example 1: <p>
     * 	double result = methodInDoubt(testValue); <p>
	 * 	if(result > lowEndOfRange && result < highEndOfRange) { <p>
	 * 		log("methodInDoubt was a success with the value " + result); <p>
	 * 	} else { <p>
	 * 		log("methodInDoubt was a failue with the value " + result); <p>
	 * 	}
    */
    void diagnose();
    
}
