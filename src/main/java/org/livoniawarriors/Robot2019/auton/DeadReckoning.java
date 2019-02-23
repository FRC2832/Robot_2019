package org.livoniawarriors.Robot2019.auton;

import org.livoniawarriors.Robot2019.Robot;

public class DeadReckoning {
	
	/**
	 * Calculate velocity in units per nanosecond //TODO: figure out units from acceleration
	 * @param startTime The time to measure from (via System.nanoTime)
	 * @return velocity
	 */
	public long calculateVelocity(long startTime) {
		short accel = Robot.peripheralSubsystem.getAcc();
		long time = System.nanoTime() - startTime;
		return accel * time;
	}

	/**
	 * Calculate position in units //TODO: figure out units, and maybe convert to something useful
	 * @param startTime Time to measure from
	 * @return position
	 */
	public long calculatePosition(long startTime) {
		long velocity = calculateVelocity(startTime);
		long time = System.nanoTime() - startTime;
		return velocity * time;
	}

	public void deadReckon() {
		long startPosition = calculatePosition(System.nanoTime());

	}

}