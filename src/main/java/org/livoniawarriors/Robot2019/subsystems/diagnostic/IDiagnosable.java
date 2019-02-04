package org.livoniawarriors.Robot2019.subsystems.diagnostic;

import java.lang.reflect.Method;

public interface IDiagnosable {
    
    /**
     * In diagnose(), run a method.
     * Then, test the method by inputing arbitrary values
     * and comparing the result to an expected value.
     * If the result is outside of the expected range, we will know something
     * is wrong and in need of further investigation
     */
    void diagnose();
    
}
