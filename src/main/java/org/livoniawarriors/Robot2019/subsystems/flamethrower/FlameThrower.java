package org.livoniawarriors.Robot2019.subsystems.flamethrower;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;

public class FlameThrower implements ISubsystem {

    @Override
    public void init() {

    }

    @Override
    public void update(boolean enabled) {

    }

    @Override
    public void dispose() throws Exception {

    }

    @Override
    public void diagnose() {

    }

    @Override
    public void csv(ICsvLogger csv) {
        csv.log("Flame", "Red");
    }
}
