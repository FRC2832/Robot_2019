package org.livoniawarriors.Robot2019.subsystems.gameplay;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.subsystems.diagnostic.IDiagnosable;

public class GamePlay implements ISubsystem, IDiagnosable {

	protected static Elevator elevator;
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
