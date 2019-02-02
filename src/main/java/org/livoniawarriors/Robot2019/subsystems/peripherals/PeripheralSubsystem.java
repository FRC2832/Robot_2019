package org.livoniawarriors.Robot2019.subsystems.peripherals;

import org.livoniawarriors.Robot2019.ISubsystem;

import java.io.IOException;

public class PeripheralSubsystem implements ISubsystem {

    private Lidar lidar;

    @Override
    public void init() {
        lidar = new Lidar();
    }

    @Override
    public void update(boolean enabled) {
        lidar.update();
    }

    @Override
    public void dispose() throws IOException {
        lidar.dispose();
    }
}
