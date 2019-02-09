package org.livoniawarriors.Robot2019.auton;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.modifiers.TankModifier;

public class Auton {

    //Waypoints for a path
    Waypoint testPoints[] = new Waypoint[] {
        new Waypoint(59.328,  15.470, 0),
        new Waypoint(59.328, 151.413, 0)
    };

    Trajectory.Config testConfig = new Trajectory.Config(
        FitMethod.HERMITE_CUBIC, //Fit method
        400,  //Number of samples
        15, //Time between points
        4,  //Max velocity
        2,  //Max acceleration 
        2   //Max jerk 
        );

    Trajectory testPath = Pathfinder.generate(testPoints, testConfig);
                                                //Wheel base
    TankModifier mod = new TankModifier(testPath).modify(.632333);

    public void followPath(Trajectory path) {
        for(int i = 0; i < path.length(); i++) {
            Trajectory.Segment segment = path.get(i);
        }
    }
}