package org.livoniawarriors.Robot2019.modules;

import org.apache.logging.log4j.Level;
import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.Robot;

public class OldFashionedAuton implements IControlModule {

    private int state;
    private boolean stateChanged;
    private double forward;

    @Override
    public void init() {

    }

    @Override
    public void start() {
        state = 0;
        stateChanged = true;
        forward = Robot.peripheralSubsystem.getYaw();
    }

    /**
     * Called to update state
     *
     * @param changed state this periodic cycle
     */
    private boolean updateState(boolean changed) {
        switch (state) {
            case 0:
                System.out.println();
                Robot.driveTrain.driveVelocity(-5, -5);
                return false;
                //return Robot.driveTrain.lazyDriveTime(1.5f, -10, changed);
            case 1:
                return Robot.driveTrain.face(forward + 90, 5, changed); // TODO: check if pigion is connected, lower tolerances, and change this to face the initial direction plus 90
            case 2:
                return Robot.driveTrain.lazyDriveDistance(20, -5, changed);
            case 3:
                return Robot.driveTrain.face(forward, 5, changed);
            case 4:
                return Robot.driveTrain.lazyDriveDistance(5, -5, changed);
            default:
                return false;
        }
    }

    @Override
    public void update() {
        if (updateState(stateChanged)) {
            stateChanged = true;
            state++;
        } else {
            stateChanged = false;
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isFinished() {
        return state >= 5;
    }
}
