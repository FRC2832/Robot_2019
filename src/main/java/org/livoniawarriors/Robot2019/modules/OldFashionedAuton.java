package org.livoniawarriors.Robot2019.modules;

import org.apache.logging.log4j.Level;
import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.Robot;

public class OldFashionedAuton implements IControlModule {

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
                if(Robot.driveTrain.lazyDriveTime(1, -0.8, changed))
                    incrementState();
                break;
            case 1:
                if(Robot.driveTrain.turn(-90, 0.7, changed))
                    incrementState();
                break;
            case 2:
                if(Robot.driveTrain.lazyDriveDistance(30, 0.8, changed))
                    incrementState();
                break;
            case 3:
                if(Robot.driveTrain.turn(90, 0.7, changed))
                    incrementState();
                break;
            case 4:
                if(Robot.driveTrain.lazyDriveDistance(30, 0.8, changed))
                    incrementState();
                break;
        }
    }

    private void incrementState() {
        state++;
        stateChanged = true;
        Robot.logger.log(Level.INFO, "Switching to state " + state);
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
        return state >= 5;
    }
}
