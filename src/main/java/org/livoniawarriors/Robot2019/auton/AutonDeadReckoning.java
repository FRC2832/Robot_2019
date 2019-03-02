package org.livoniawarriors.Robot2019.auton;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.auton.DeadReckoning;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutonDeadReckoning {

	private String selected;
	private DeadReckoning deadReckoning;
	private long startTime;
	private double position;
	private SendableChooser<String> selectAuton;

	public void init() {
		deadReckoning = new DeadReckoning();
		startTime = System.nanoTime();
		selectAuton = new SendableChooser<String>();
		Robot.userInput.addOption(selectAuton, "Straight Line", "test", true); //The only one we actually have
		Robot.userInput.addOption(selectAuton, "Dummy value: DO NOT SELECT", "test1", false); //Reminding me how to make new ones
	}

	public void update(boolean enabled) {
		if(!enabled) {
			return;
		}
		selected = selectAuton.getSelected();
		/* Write out some paths */
		if(selected.equals("test")) { //Drive straight 100 inches
			position = deadReckoning.calculatePosition(startTime);
			Robot.driveTrain.tankDrive(0.8, 0.8); //TODO: figure out ideal speed
			//Stop driving when position 1 is reached
			if(position >= 100) { //Desired position in inches
				Robot.driveTrain.tankDrive(0, 0);
			}
			
		/* I added this to remind me how to make chooser buttons */	
		} else if(selected.equals("test1")) {
			System.out.println("Dummy value selected; doing nothing");

		/* None of the paths are selected: something is probably wrong, so we do nothing */
		} else {
			Robot.driveTrain.tankDrive(0, 0);
		}

	}

	
}