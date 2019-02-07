package org.livoniawarriors.Robot2019.subsystems.gameplay;

import org.livoniawarriors.Robot2019.ISubsystem;

public class GamePlay implements ISubsystem {

    private Elevator elevator;
    private GamePieceManipulatorJake gamePieceManipulatorJake;

    @Override
    public void init() {
        elevator = new Elevator();
        gamePieceManipulatorJake = new GamePieceManipulatorJake();
    }

    @Override
    public void update(boolean enabled) {
        elevator.update(enabled);
        gamePieceManipulatorJake.update(enabled);
    }

    @Override
    public void dispose() throws Exception {

    }
}
