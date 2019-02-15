package org.livoniawarriors.Robot2019.subsystems.gameplay;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;

public class GamePlay implements ISubsystem {

	private Elevator elevator;
	//private GamePieceManipulatorJake gamePieceManipulatorJake;
    //private Climber climber;
    
    private ElevatorTest elevatorTest;

    private boolean testElevator = true;

	@Override
	public void init() {
        if (!testElevator) {
            elevator = new Elevator();
        }
		//gamePieceManipulatorJake = new GamePieceManipulatorJake();
        //climber = new Climber();
        
	}

	@Override
	public void update(boolean enabled) {	
		//gamePieceManipulatorJake.update(enabled);
        //climber.update(enabled);
        if (testElevator && enabled && elevatorTest == null) {
            elevatorTest = new ElevatorTest();
        }
        if (!testElevator) {
            elevator.update(enabled);
        }
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
		//elevator.diagnose();
	}
}
