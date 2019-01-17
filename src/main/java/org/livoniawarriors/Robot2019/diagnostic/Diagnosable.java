/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.livoniawarriors.Robot2019.diagnostic;

import java.lang.annotation.Documented;

/**
 * Annotation to flag methods
 * as diagnosable, i.e. able to be
 * checked by the Diagnostic Handler
 */
@Documented
public @interface Diagnosable {

    double expValH() default 0;
    double expValL() default 0;
    boolean expValBool() default false;
    boolean diagOn() default true;
}
