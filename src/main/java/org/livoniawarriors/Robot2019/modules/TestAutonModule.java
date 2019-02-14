package org.livoniawarriors.Robot2019.modules;

import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.auton.Auton;
import org.livoniawarriors.Robot2019.auton.AutonPaths;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class TestAutonModule implements IControlModule {

	private static AutonPaths paths;
	private SendableChooser<String> autonChooser;

	@Override
	public void init() {
		paths = new AutonPaths();
		autonChooser = new SendableChooser<>();

		//Configure PIDVA values for each follower
		paths.testFollowerL.configurePIDVA(1, 0, 0, 1, 1); // PIDVA values need to be found
		paths.testFollowerR.configurePIDVA(1, 0, 0, 1, 1); // PIDVA values need to be found

		Robot.userInput.addOption(autonChooser, "TestPath", "testPath", true);

	}

	@Override
	public void start() {

	}

	@Override
	public void update() {
		//We may want to make this a switch when we have more available options
		if(Robot.userInput.getSelected(autonChooser).equals("testPath")) {
			Auton.followPath(paths.testFollowerL, paths.testFollowerR);
		}
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
