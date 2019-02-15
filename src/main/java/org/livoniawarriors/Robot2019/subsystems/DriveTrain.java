package org.livoniawarriors.Robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;

public class DriveTrain implements ISubsystem {

    private final static int DRIVE_MOTOR_FL = 10;
    private final static int DRIVE_MOTOR_FR = 24;
    private final static int DRIVE_MOTOR_BL = 11;
    private final static int DRIVE_MOTOR_BR = 25;

    private final static int TICKS_PER_ROTATION = 200; //TODO: Fix
    private final static double WHEEL_DIAMETER = 100; //TODO: Fix
    private final static double WHEEL_BASE_WIDTH = 0.5; // TODO: Fix

    private final static double P = 1; //TODO: Figure out these values
    private final static double I = 0;
    private final static double D = 0;
    private final static double MAX_VELOCITY = 1;
    private final static double ACCELERATION_GAIN = 0;

    private DifferentialDrive drive;
    private EncoderFollower leftFollower;
    private EncoderFollower rightFollower;
    private SensorCollection rightEncoder, leftEncoder;
    Trajectory.Config config;

    private Notifier followerMotifier;
    private boolean auto;

    public void setPath() {
        auto = true;
    }

    @Override
    public void csv(ICsvLogger csv) {

    }

    @Override
    public void diagnose() {

    }

    /**
     * Resumes an interrupted trajectory
     */
    public void resumeTrajectoy() {
        if(!isTrajectoryDone())
            auto = true;
    }


    public void startTrajectory(Trajectory trajectory) {
        auto = true;
        var modifier = new TankModifier(trajectory).modify(WHEEL_BASE_WIDTH);
        leftFollower.setTrajectory(modifier.getLeftTrajectory());
        rightFollower.setTrajectory(modifier.getRightTrajectory());
    }

    @Override
    public void init() {
        WPI_TalonSRX talonFL = new WPI_TalonSRX(DRIVE_MOTOR_FL);
        WPI_TalonSRX talonFR = new WPI_TalonSRX(DRIVE_MOTOR_FR);
        WPI_TalonSRX talonBL = new WPI_TalonSRX(DRIVE_MOTOR_BL);
        WPI_TalonSRX talonBR = new WPI_TalonSRX(DRIVE_MOTOR_BR);
        talonBL.follow(talonFL);
        //talonBL.setInverted(true);
        talonBR.follow(talonFR);
        drive = new DifferentialDrive(talonFL, talonFR);
        leftFollower = new EncoderFollower();
        leftFollower.configureEncoder(0 /* change */, TICKS_PER_ROTATION, WHEEL_DIAMETER);
        leftFollower.configurePIDVA(P, I, D, 1/ MAX_VELOCITY, ACCELERATION_GAIN);
        leftFollower.setTrajectory(new Trajectory(0));
        leftEncoder = talonBL.getSensorCollection();
        rightFollower = new EncoderFollower();
        rightFollower.configureEncoder(0 /* change */, TICKS_PER_ROTATION, WHEEL_DIAMETER);
        rightFollower.configurePIDVA(P, I, D, 1/ MAX_VELOCITY, ACCELERATION_GAIN);
        rightFollower.setTrajectory(new Trajectory(0));
        rightEncoder = talonBR.getSensorCollection();
        config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);

    }

    public void tankDrive(double left, double right) {
        tankDrive(left, right, true);
    }

    /**
     * Disables trajectory
     * @param left
     * @param right
     * @param squaredInputs
     */
    public void tankDrive(double left, double right, boolean squaredInputs) {
        drive.tankDrive(left, right, squaredInputs);
    }

    public boolean isTrajectoryDone() {
        return leftFollower.isFinished() && rightFollower.isFinished();
    }

    @Override
    public void update(boolean enabled) {
        if(enabled)
            return;

        double l = leftFollower.calculate(leftEncoder.getQuadraturePosition());
        double r = leftFollower.calculate(rightEncoder.getQuadraturePosition());

        double gyro_heading = Robot.peripheralSubsystem.getYaw();
        double desired_heading = Pathfinder.r2d(leftFollower.getHeading());  // Should also be in degrees

        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        double turn = 0.8 * (-1.0/80.0) * angleDifference;
        tankDrive(l + turn, r - turn);
    }

    @Override
    public void dispose() throws Exception {

    }
}
