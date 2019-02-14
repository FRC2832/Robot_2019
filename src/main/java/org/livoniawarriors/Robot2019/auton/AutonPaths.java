package org.livoniawarriors.Robot2019.auton;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class AutonPaths {

	//A simple test path, in a straight line
	Waypoint testPoints[] = new Waypoint[] {
		new Waypoint(1.699,  3.85, 0),
		new Waypoint(5.316, 3.85, 0)
	};
	Trajectory.Config testConfig = new Trajectory.Config(
	FitMethod.HERMITE_CUBIC, //Fit method
	400,  //Number of samples
	15,   //Time between points
	4,    //Max velocity
	2,    //Max acceleration 
	2     //Max jerk 
	);
	Trajectory testPath = Pathfinder.generate(testPoints, testConfig);
	TankModifier mod = new TankModifier(testPath).modify(0.632333);
	Trajectory testModLeft  = mod.getLeftTrajectory();
	Trajectory testModRight = mod.getRightTrajectory();
	public EncoderFollower testFollowerL = new EncoderFollower(testModLeft);
	public EncoderFollower testFollowerR = new EncoderFollower(testModRight);
	

}