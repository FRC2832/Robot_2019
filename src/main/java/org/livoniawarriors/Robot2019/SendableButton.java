package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import org.apache.logging.log4j.Level;

public abstract class SendableButton implements Sendable {

    protected boolean running;

    private boolean isRunning() {
        return running;
    }

    @Override
    public void setName(String subsystem, String name) {

    }

    @Override
    public String getName() {
        return "Button";
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

    protected abstract void onEnable();

    protected abstract void onDisable();

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Command");
        builder.addStringProperty(".name", this::getName, this::setName);
        builder.addBooleanProperty("running", this::isRunning, value -> {
            if (value) {
                if(!running)
                    onEnable();
                running = true;
            } else {
                if(running)
                    onDisable();
                running = false;
            }
        });
        builder.addBooleanProperty(".isParented", () -> false, (parented) -> {});
    }
}
