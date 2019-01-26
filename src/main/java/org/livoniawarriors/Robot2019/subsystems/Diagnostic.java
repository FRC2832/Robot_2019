package org.livoniawarriors.Robot2019.subsystems;

import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.subsystems.diagnostic.DiagnosticHandler;
import org.livoniawarriors.Robot2019.subsystems.gameplay.Elevator;

public class Diagnostic implements ISubsystem {

    private DiagnosticHandler diag;

    @Override
    public void init() {
        diag = new DiagnosticHandler();
    
    }

    @Override
    public void update(boolean enabled) {
        /* Elevator */
        diag.diagnoseMethod(Elevator.class, "getElevatorHeight");
        diag.diagnoseMethod(Elevator.class, "pidGet");


    }

    @Override
    public void dispose() throws Exception {

    }
}
