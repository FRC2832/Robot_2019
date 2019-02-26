package org.livoniawarriors.Robot2019.auton;

import org.livoniawarriors.Robot2019.Robot;

public class DeadReckoning {
	
	/**
	 * Calculate velocity in inches per second
	 * @param startTime The time to measure from (via System.nanoTime)
	 * @return velocity
	 */
	private double calculateVelocity(long startTime) {
		double accel = Robot.peripheralSubsystem.getAcc();
		double time = (System.nanoTime() - startTime) / 1000000000d;
		return accel * time;
	}

	/**
	 * Calculate position in inches
	 * @param startTime Time to measure from
	 * @return position
	 */
	public double calculatePosition(long startTime) {
		double velocity = calculateVelocity(startTime);
		double time = (System.nanoTime() - startTime) / 1000000000d;
		return velocity * time;
	}


}