package org.livoniawarriors.Robot2019.modules;

import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.Robot;

public class DriveForwardAuton implements IControlModule {

    boolean init;

    @Override
    public void init() {

    }

    @Override
    public void start() {
        init = true;
    }

    @Override
    public void update() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isFinished() {
        boolean done = Robot.driveTrain.lazyDriveTime(1.5f, -0.7, init);
        if(init)
            init = false;
        return done;
    }
}
