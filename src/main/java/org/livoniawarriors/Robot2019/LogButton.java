package org.livoniawarriors.Robot2019;

import org.apache.logging.log4j.Level;

public class LogButton extends SendableButton {

    private Thread thread;

    @Override
    public String getName() {
        return "LogButton";
    }

    @Override
    protected void onEnable() {
        thread = new Thread(() -> {
            // Copy files here
            Robot.logger.log(Level.INFO, "Pulling logs...");
            running = false;
        });
        thread.start();
    }

    @Override
    protected void onDisable() {
        if(thread != null) {
            thread.interrupt();
            thread = null;
        }
    }
}
