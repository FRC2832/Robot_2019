package org.livoniawarriors.Robot2019.modules;

import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.auton.Auton;
import org.livoniawarriors.Robot2019.auton.AutonDeadReckoning;
import org.livoniawarriors.Robot2019.auton.AutonPaths;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class TestAutonModule implements IControlModule {
/*
	private static AutonPaths paths;
	private SendableChooser<String> autonChooser;
*/

	AutonDeadReckoning deadReckoning;

	@Override
	public void init() {

		deadReckoning = new AutonDeadReckoning();
		deadReckoning.init();
	}

	@Override
	public void start() {

	}

	@Override
	public void update() {
	
		deadReckoning.update(true);
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isFinished() {
		return !DriverStation.getInstance().isAutonomous();
	}

}
