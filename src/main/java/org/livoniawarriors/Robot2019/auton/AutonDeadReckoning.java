package org.livoniawarriors.Robot2019.auton;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.auton.DeadReckoning;

public class AutonDeadReckoning implements ISubsystem {

	private String selected;
	private DeadReckoning deadReckoning;
	private long startTime;
	private double position;

	@Override
	public void init() {
		deadReckoning = new DeadReckoning();
		startTime = System.nanoTime();
	}

	@Override
	public void update(boolean enabled) {
		if(!enabled) {
			return;
		}
		selected = "test"; //TODO: Set to retrieve selected auton option from dashboard
		/* Write out some paths */
		if(selected.equals("test")) { //Drive straight 100 inches
			position = deadReckoning.calculatePosition(startTime);
			Robot.driveTrain.tankDrive(0.4, 0.4); //TODO: figure out ideal speed
			//Stop driving when position 1 is reached
			if(position >= 100) { //Desired position in inches
				Robot.driveTrain.tankDrive(0, 0);
			}
			
		}

	}

	@Override
	public void dispose() throws Exception {

	}

	@Override
	public void csv(ICsvLogger csv) {

	}

	@Override
	public void diagnose() {

	}


	
}