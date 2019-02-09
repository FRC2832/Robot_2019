package org.livoniawarriors.Robot2019.subsystems.gameplay;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;

public class GamePlay implements ISubsystem {

	private Elevator elevator;
	private GamePieceManipulatorJake gamePieceManipulatorJake;
	private Climber climber;

	@Override
	public void init() {
		elevator = new Elevator();
		gamePieceManipulatorJake = new GamePieceManipulatorJake();
		climber = new Climber();
	}

	@Override
	public void update(boolean enabled) {
		elevator.update(enabled);
		gamePieceManipulatorJake.update(enabled);
		climber.update(enabled);
	}
	
	public double getElevatorHeight() {
		return elevator.getElevatorHeight();
	}

	@Override
	public void dispose() throws Exception {

    }

    @Override
    public void csv(ICsvLogger csv) {

    }

	@Override
	public void diagnose() {
		elevator.diagnose();
	}
}
