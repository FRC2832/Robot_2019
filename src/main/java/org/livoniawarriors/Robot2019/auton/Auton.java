package org.livoniawarriors.Robot2019.auton;

import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.auton.AutonPaths;

import edu.wpi.first.wpilibj.Encoder;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class Auton {

	static Encoder encoderFL = new Encoder(0, 1); //TODO: I don't have
	static Encoder encoderFR = new Encoder(2, 3); //the faintest idea
	static Encoder encoderBL = new Encoder(4, 5); //what these values
	static Encoder encoderBR = new Encoder(6, 7); //are supposed to be
	
    public static void followPath(EncoderFollower followerL, EncoderFollower followerR) {
		
		int leftPos = encoderFL.getRaw();
		int rightPos = encoderFR.getRaw();
		double heading = Robot.peripheralSubsystem.getYaw();

		double newHeading = followerL.getHeading() * (180/Math.PI);
		double headingDiff = Pathfinder.boundHalfDegrees(newHeading - heading);

		//TODO: Figure this out
		double turnVal = headingDiff * 0.2;

		double valueL = followerL.calculate(leftPos);
		double valueR = followerR.calculate(rightPos);

		double speedL = valueL + turnVal;
		double speedR = valueR - turnVal;

		Robot.driveTrain.tankDrive(speedL, speedR);

	}
}