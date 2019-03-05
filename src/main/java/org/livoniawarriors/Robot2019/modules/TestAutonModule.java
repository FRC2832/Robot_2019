package org.livoniawarriors.Robot2019.modules;

import org.livoniawarriors.Robot2019.IControlModule;
import edu.wpi.first.wpilibj.DriverStation;

public class TestAutonModule implements IControlModule {
/*
	private static AutonPaths paths;
	private SendableChooser<String> autonChooser;
*/


	@Override
	public void init() {

	}

	@Override
	public void start() {

	}

	@Override
	public void update() {
	
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isFinished() {
		return !DriverStation.getInstance().isAutonomous();
	}

}
