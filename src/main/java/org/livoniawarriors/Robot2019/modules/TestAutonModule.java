package org.livoniawarriors.Robot2019.modules;

import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.auton.Auton;

public class TestAutonModule implements IControlModule {

	@Override
	public void init() {

	}

	@Override
	public void start() {

	}

	@Override
	public void update() {
		Auton.followPath(Auton.followerL, Auton.followerR);
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
