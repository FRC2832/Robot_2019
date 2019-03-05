package org.livoniawarriors.Robot2019.modules;

import jaci.jniloader.JNILoader;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.Robot;

public class FullyAutonModule implements IControlModule {

    private int state;
    private boolean stateChanged;

    @Override
    public void init() {

    }

    @Override
    public void start() {
        state = 0;
        stateChanged = true;
    }

    /**
     * Called to update state
     *
     * @param changed state this periodic cycle
     */
    private void updateState(boolean changed) {
        switch (state) {
            case 0:
                if(Robot.driveTrain.lazyDriveTime(2, 0.7, changed))
                    incrementState();
                break;
            case 1:
                if(changed) {
                    Trajectory trajectory = Robot.driveTrain.generateTrajectory(new Waypoint[] {
                            new Waypoint(-4, -1, Pathfinder.d2r(0.1)),
                            new Waypoint(-2, -2, 0),
                            new Waypoint(0, 0, 0)
                    });
                    Robot.driveTrain.startTrajectory(trajectory);
                }
                if (Robot.driveTrain.isTrajectoryDone())
                    incrementState();
                break;
            case 2:

                break;
            case 3:

                break;
        }
    }

    private void incrementState() {
        state++;
        stateChanged = true;
    }

    @Override
    public void update() {
        updateState(stateChanged);
        stateChanged = false;
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
