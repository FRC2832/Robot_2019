package org.livoniawarriors.Robot2019.modules;

import java.util.HashMap;
import java.util.Map;

import org.livoniawarriors.Robot2019.IControlModule;
import org.livoniawarriors.Robot2019.Robot;

import edu.wpi.first.wpilibj.DriverStation;

public class TestAutonModule implements IControlModule {

	private HashMap<String, String> autonOptions;
	String selected;

	@Override
	public void init() {
		autonOptions = new HashMap<String, String>();
		autonOptions.put("Straight line test", "test");
		autonOptions.put("Useless value", "test1"); //Reminding me how this works
		Robot.userInput.createSendableChooser("Auton", autonOptions, "Auton", "test");
	}

	@Override
	public void start() {

	}

	@Override
	public void update() {
		selected = Robot.userInput.querySendableChooser("Auton");
		switch(selected) {
		//Drive forward 100 inches
		case "test": 
			Robot.driveTrain.driveForwardLazy(100, true);
			break;
		//Do nothing
		case "test1": 
			Robot.driveTrain.tankDrive(0, 0);
			break;
		}
	
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isFinished() {
		return !DriverStation.getInstance().isAutonomous();
	}

}
