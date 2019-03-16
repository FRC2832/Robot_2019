package org.livoniawarriors.Robot2019;

import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class LogButton extends SendableButton {

    private Thread thread;

    private final static String USB_PATH = "U";

    @Override
    public String getName() {
        return "LogButton";
    }

    @Override
    protected void onEnable() {
        if(thread != null) {
            thread.interrupt();
            thread = null;
        }
        thread = new Thread(() -> {
            Paths.get("");
            Robot.logger.log(Level.INFO, "Pulling logs...");
            Path copyLogDir = Paths.get(USB_PATH + "/logging/logs");
            Path copyCSVDir = Paths.get(USB_PATH + "/logging/csv");
            copyCSVDir.toFile().mkdir();
            copyLogDir.toFile().mkdir();
            copyFolder(Paths.get("/home/lvuser/logging/logs"), copyLogDir);
            copyFolder(Paths.get("/home/lvuser/logging/csv"), copyCSVDir);
            running = false;
        });
        thread.start();
    }

    private void copyFolder(Path src, Path dest) {
        try {
            try (Stream<Path> stream = Files.walk(src)) {
                stream.forEachOrdered(sourcePath -> {
                    try {
                        Files.copy(sourcePath, dest);
                    } catch (Exception e) {
                        Robot.logger.error("Pull logs", e);
                    }
                });
            }
        } catch (IOException e) {
            Robot.logger.error("Pull logs", e);
        }
    }

    @Override
    protected void onDisable() {
        if(thread != null) {
            thread.interrupt();
            thread = null;
        }
    }
}
