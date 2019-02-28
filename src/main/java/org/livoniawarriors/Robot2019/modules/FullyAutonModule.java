package org.livoniawarriors.Robot2019.modules;

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
                if(changed) {
                    Trajectory trajectory = Robot.driveTrain.generateTrajectory(new Waypoint[]{new Waypoint(0, 0, 0), new Waypoint(0, 10, 0)});
                    Robot.driveTrain.startTrajectory(trajectory);
                }
                if (Robot.driveTrain.isTrajectoryDone())
                    state++;
                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
        }
    }

    @Override
    public void update() {
        Robot.logger.error("HIIIIIIIIIIIIIIIIII");
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
