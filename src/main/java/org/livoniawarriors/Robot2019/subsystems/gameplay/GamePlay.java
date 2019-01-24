package org.livoniawarriors.Robot2019.subsystems.gameplay;

import org.livoniawarriors.Robot2019.ISubsystem;

public class GamePlay implements ISubsystem {

    private Elevator elevator;

    @Override
    public void init() {
        elevator = new Elevator();
    }

    @Override
    public void update(boolean enabled) {
        elevator.update(enabled);

    }

    @Override
    public void dispose() throws Exception {

    }
}
