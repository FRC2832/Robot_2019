package org.livoniawarriors.Robot2019.subsystems.gameplay;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;

public class GamePlay implements ISubsystem {

	private Elevator elevator;
	private GamePieceManipulator gamePieceManipulator;
    private Climber climber;

	@Override
	public void init() {
        elevator = new Elevator();
		gamePieceManipulator = new GamePieceManipulator();
        climber = new Climber();
	}

	@Override
	public void update(boolean enabled) {	
		gamePieceManipulator.update(enabled);
        climber.update(enabled);
        elevator.update(enabled);
	}
	
	public double getElevatorHeight() {
		return elevator.getElevatorHeight();
	}

	@Override
	public void dispose() throws Exception {

    }

    @Override
    public void csv(ICsvLogger csv) {
		elevator.csv(csv);
    }

	@Override
	public void diagnose() {
		elevator.diagnose();
	}
}
