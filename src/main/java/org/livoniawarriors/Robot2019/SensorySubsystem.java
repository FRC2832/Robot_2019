package org.livoniawarriors.Robot2019;

import java.io.IOException;

public class SensorySubsystem implements Subsystem {

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
