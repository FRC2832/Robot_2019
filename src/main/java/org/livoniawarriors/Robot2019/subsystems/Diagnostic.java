package org.livoniawarriors.Robot2019.subsystems;

import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.subsystems.diagnostic.DiagnosticHandler;
import org.livoniawarriors.Robot2019.subsystems.gameplay.Elevator;

public class Diagnostic implements ISubsystem, Runnable {

    private DiagnosticHandler diag;
    private Thread diagThread;

    
    /* Set the methods to diagnose */ 
    public void run() {
        try {
            /* Elevator */
            diag.diagnoseMethod(Elevator.class, "getElevatorHeight");
            diag.diagnoseMethod(Elevator.class, "pidGet");
            Thread.sleep(15000);

        } catch(InterruptedException e) {
            System.err.println("Diagnosing thread was interrupted: " + e.getMessage());
            System.err.println("I think you know what the problem is just as well as I do");
        }
    }
    private void start() {
        System.out.println("Running a diagnostic");
        diagThread = new Thread();
        diagThread.start();
    }

    @Override
    public void init() {
        diag = new DiagnosticHandler();
        start();
    }

    @Override
    public void update(boolean enabled) {


    }

    @Override
    public void dispose() throws Exception {

    }
}
