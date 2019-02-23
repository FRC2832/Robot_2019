package org.livoniawarriors.Robot2019.auton;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.auton.DeadReckoning;

public class AutonDeadReckoning implements ISubsystem {

	String selected;
	DeadReckoning deadReckoning;
	long startTime;
	long position;

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
		if(selected == "test") {
			long desiredPosition = 0; //TODO: remove variable, determine desired position after we figure out what units this is in
			position = deadReckoning.calculatePosition(startTime);
			Robot.driveTrain.tankDrive(0.4, 0.4); //TODO: figure out ideal speed
			//Stop driving when position 1 is reached
			if(position >= desiredPosition) { //TODO: replace with actual desired position
				Robot.driveTrain.tankDrive(0, 0);
				//Put turn code here, then drive some more
				//Test again for next position
				//Turn & drive again until next position, repeat until final position reached
				
			}
			
		} //Repeat for every path we want as an option

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