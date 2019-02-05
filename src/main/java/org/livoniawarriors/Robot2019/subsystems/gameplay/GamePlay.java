package org.livoniawarriors.Robot2019.subsystems.gameplay;

import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;

import edu.wpi.first.wpilibj.DriverStation;

public class GamePlay implements ISubsystem {

	private Elevator elevator;
	private int timer;

	@Override
	public void init() {
		elevator = new Elevator();
		timer = 0;
	}

	@Override
	public void update(boolean enabled) {
		elevator.update(enabled);

		//Diagnostic
		if(!Robot.getInstance().driverStation.isFMSAttached())
			timer++;
			//Every 5 seconds
			if(timer % 15000 == 0) {
				elevator.diagnose();
			}


	}

	@Override
	public void dispose() throws Exception {

	}
}
