package org.livoniawarriors.Robot2019.modules;

import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.auton.Auton;
import org.livoniawarriors.Robot2019.auton.AutonPaths;

public class TestAutonModule implements IControlModule {

	private static AutonPaths paths;

	@Override
	public void init() {
		paths = new AutonPaths();

	}

	@Override
	public void start() {

	}

	@Override
	public void update() {
		Auton.followPath(paths.testFollowerL, paths.testFollowerR);
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
