package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import org.apache.logging.log4j.Level;

public class LogPutton implements Sendable {

    private boolean running;
    private Thread thread;

    private boolean isRunning() {
        return running;
    }

    @Override
    public void setName(String subsystem, String name) {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getSubsystem() {
        return "";
    }

    @Override
    public void setSubsystem(String subsystem) {

    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Command");
        builder.addStringProperty(".name", this::getName, this::setName);
        builder.addBooleanProperty("running", this::isRunning, value -> {
            if (value) {
                if(!running) {
                    running = true;
                    thread = new Thread(() -> {
                        // Copy files here
                        Robot.logger.log(Level.INFO, "Pulling logs...");
                        running = false;
                    });
                }
            } else {
                running = false;
                if(thread != null) {
                    thread.interrupt();
                    thread = null;
                }
            }
        });
        builder.addBooleanProperty(".isParented", () -> false, (parented) -> {});
    }
}
