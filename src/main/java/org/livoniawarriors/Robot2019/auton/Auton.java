package org.livoniawarriors.Robot2019.auton;

import org.livoniawarriors.Robot2019.Robot;

import edu.wpi.first.wpilibj.Encoder;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

//We might want a new file to store all the paths and use this for the method
public class Auton {

	final double P = 1; //TODO: Figure out these values
	final double I = 1;
	final double D = 1;
	final double V = 1;
	final double A = 0;

	static Encoder encoderFL = new Encoder(0, 1); //TODO: I don't have
	static Encoder encoderFR = new Encoder(2, 3); //the faintest idea
	static Encoder encoderBL = new Encoder(4, 5); //what these values
	static Encoder encoderBR = new Encoder(6, 7); //are supposed to be

    //Waypoints for a path
    static Waypoint testPoints[] = new Waypoint[] {
        new Waypoint(1.699,  3.85, 0),
        new Waypoint(5.316, 3.85, 0)
    };

    static Trajectory.Config testConfig = new Trajectory.Config(
        FitMethod.HERMITE_CUBIC, //Fit method
        400,  //Number of samples
        15,   //Time between points
        4,    //Max velocity
        2,    //Max acceleration 
        2     //Max jerk 
        );

	static Trajectory testPath = Pathfinder.generate(testPoints, testConfig);
	
           			                                     //Wheel base
	static TankModifier mod = new TankModifier(testPath).modify(0.632333);
	
	//TODO: We only need one of each trajectory for every path, so these can be static... right?
	static Trajectory modLeft  = mod.getLeftTrajectory();
	static Trajectory modRight = mod.getRightTrajectory();

	//These will have a getter (?) in their home file
	public static EncoderFollower followerL = new EncoderFollower(modLeft);
	public static EncoderFollower followerR = new EncoderFollower(modRight);

	//Not sure where to put these or how to use them
//	followerL.EncoderFollower.configurePIDVA(P, I, D, V, A);
//	followerL.EncoderFollower.configurePIDVA(P, I, D, V, A);

	//TODO: Might be a good idea to keep all the path into in another file, as we will
	//need separate of everything and dont want to clutter this up
    public static void followPath(EncoderFollower followerL, EncoderFollower FollowerR) {
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