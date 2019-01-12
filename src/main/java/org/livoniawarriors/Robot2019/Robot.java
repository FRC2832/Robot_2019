package org.livoniawarriors.Robot2019;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends TimedRobot {

    final static int DRIVE_MOTER_FL = 1;
    final static int DRIVE_MOTER_FR = 2;
    final static int DRIVE_MOTER_BL = 3;
    final static int DRIVE_MOTER_BR = 4;

    final static int LIMITER_JUMPER = 0;
    final static double DEADZONE = 0.1d;
    final static double LIMITER = 0.4d;

    private DifferentialDrive drive;
    private XboxController controller;
    private DigitalInput limiterJumper;

    @Override
    public void robotInit() {
        PWMVictorSPX talonFL = new PWMVictorSPX(DRIVE_MOTER_FL);
        PWMVictorSPX talonFR = new PWMVictorSPX(DRIVE_MOTER_FR);
        PWMVictorSPX talonBL = new PWMVictorSPX(DRIVE_MOTER_BL);
        PWMVictorSPX talonBR = new PWMVictorSPX(DRIVE_MOTER_BR);
        controller = new XboxController(0);
        //talonBL.follow(talonFL);
        //talonBR.follow(talonFR);
        drive = new DifferentialDrive(new SpeedControllerGroup(talonFL, talonBL), new SpeedControllerGroup(talonFR, talonBR));
        limiterJumper = new DigitalInput(LIMITER_JUMPER);
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        double left = 0, right = 0;
        double limit = 1;
        if (limiterJumper.get())
            limit = LIMITER;
        if (Math.abs(controller.getRawAxis(1)) > DEADZONE)
            left = Math.min(Math.abs(controller.getRawAxis(1)), limit);
        if (Math.abs(controller.getRawAxis(5)) > DEADZONE)
            right = Math.min(Math.abs(controller.getRawAxis(5)), limit);
        drive.tankDrive(left, right, false);
    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }
}
