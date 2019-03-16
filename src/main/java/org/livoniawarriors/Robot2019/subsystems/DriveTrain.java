package org.livoniawarriors.Robot2019.subsystems;

import java.util.Arrays;
import java.util.List;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;

public class DriveTrain implements ISubsystem {

    private final static int TICKS_PER_ROTATION = 2540;
    private final static double WHEEL_DIAMETER = 8;
    private final static double WHEEL_BASE_WIDTH = 2;

    private final static double P = 1;
    private final static double I = 0;
    private final static double D = 0;
    private final static double MAX_VELOCITY = 1;
    private final static double ACCELERATION_GAIN = 0;
    public final static int DRIVE_MOTER_FL = 25;
    public final static int DRIVE_MOTER_FR = 10;
    public final static int DRIVE_MOTER_BL = 24;
    public final static int DRIVE_MOTER_BR = 11;
    private final static double ENCODER_POLL_RATE = 0.1d;
    private final static double TALON_EXPERIATION_TIME = 0.1;

    private DifferentialDrive drive;
    private EncoderFollower leftFollower;
    private EncoderFollower rightFollower;
    private int encoderOffsetRight, encoderOffsetLeft;
    private SensorCollection rightEncoder, leftEncoder;
    Trajectory.Config pathConfig;
    private boolean auto;
    private double startTime;
    private int encoderLastLeft, encoderLastRight;
    private Notifier encoderPoller;
    private List<WPI_TalonSRX> talons;

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

    public void startTrajectory(Trajectory trajectoryLeft, Trajectory trajectoryRight) {
        auto = true;
        leftFollower.setTrajectory(trajectoryLeft);
        rightFollower.setTrajectory(trajectoryRight);
    }

    public Trajectory generateTrajectory(Waypoint[] waypoints) {
        return Pathfinder.generate(waypoints, pathConfig);
    }

    @Override
    public void init() {
        WPI_TalonSRX talonFrontLeft = new WPI_TalonSRX(DRIVE_MOTER_FL);
        WPI_TalonSRX talonFrontRight = new WPI_TalonSRX(DRIVE_MOTER_FR);
        WPI_TalonSRX talonBackLeft = new WPI_TalonSRX(DRIVE_MOTER_BL);
        WPI_TalonSRX talonBackRight = new WPI_TalonSRX(DRIVE_MOTER_BR);
        talons = Arrays.asList(new WPI_TalonSRX[]{talonFrontLeft, talonFrontRight, talonBackLeft, talonBackRight});
        talonBackLeft.follow(talonFrontLeft);
        talonBackRight.follow(talonFrontRight);
        talons.forEach(talon -> {
            talon.setInverted(true);
            talon.setExpiration(TALON_EXPERIATION_TIME);
        });
        encoderPoller = new Notifier(this::pollEncoders);
        encoderPoller.startPeriodic(ENCODER_POLL_RATE);
        drive = new DifferentialDrive(talonFrontLeft, talonFrontRight);
        leftFollower = new EncoderFollower();
        leftEncoder = talonFrontLeft.getSensorCollection();
        leftFollower.configureEncoder(leftEncoder.getQuadraturePosition(), TICKS_PER_ROTATION, WHEEL_DIAMETER);
        leftFollower.configurePIDVA(P, I, D, 1/ MAX_VELOCITY, ACCELERATION_GAIN);
        rightFollower = new EncoderFollower();
        rightEncoder = talonFrontRight.getSensorCollection();
        rightFollower.configureEncoder(rightEncoder.getQuadraturePosition(), TICKS_PER_ROTATION, WHEEL_DIAMETER);
        rightFollower.configurePIDVA(P, I, D, 1/ MAX_VELOCITY, ACCELERATION_GAIN);
        leftFollower.setTrajectory(new Trajectory(0));
        rightFollower.setTrajectory(new Trajectory(0));
        encoderOffsetLeft = leftEncoder.getQuadraturePosition();
        encoderOffsetRight = rightEncoder.getQuadraturePosition();
        pathConfig = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, 0.05, 1.7, 2.0, 60.0); // Might need to convert to imperial
    }

    private void pollEncoders() {
        encoderLastLeft = -leftEncoder.getQuadraturePosition();
        encoderLastRight = rightEncoder.getQuadraturePosition();
    }

    /**
     *
     * @param left side power
     * @param right side power
     */
    public void tankDrive(double left, double right) {
        tankDrive(left, right, true);
    }

    /**
     * Disables trajectory
     * @param left side power
     * @param right side power
     * @param squaredInputs or not square inputs
     */
    public void tankDrive(double left, double right, boolean squaredInputs) {
        drive.tankDrive(left, right, squaredInputs);
        auto = false;
    }

    /**
     * Drive forward for a time without anything fancy at all
     * @param time to drive
     * @param speed to drive
     * @param reset encoder zero point
     * @return completion
     */
    public boolean lazyDriveTime(float time, double speed, boolean reset) {
        auto = false;
        if(reset)
            startTime = Timer.getFPGATimestamp();
        if(startTime + time < Timer.getFPGATimestamp()) {
            drive.tankDrive(0, 0);
            return true;
        }
        else
            drive.tankDrive(speed, speed);
        return false;
    }

    public boolean isTrajectoryDone() {
        return leftFollower.isFinished() && rightFollower.isFinished();
    }

    @Override
    public void update(boolean enabled) {
        talons.forEach(talon -> talon.setNeutralMode(false ? NeutralMode.Brake : NeutralMode.Coast));
        Robot.userInput.putValue("tab", "encoderL", getEncoderPos(false));
        Robot.userInput.putValue("tab", "encoderR", getEncoderPos(true));

        if(!enabled)
            return;

        if(auto && !isTrajectoryDone()) {
            double l = leftFollower.calculate(getEncoderRaw(false));
            double r = leftFollower.calculate(getEncoderRaw(true));

            double gyro_heading = Robot.peripheralSubsystem.getYaw();
            double desired_heading = Pathfinder.r2d(leftFollower.getHeading());  // Should also be in degrees

            double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
            double turn = 0.8 * (-1.0 / 80.0) * angleDifference;

            drive.tankDrive(l + turn, r - turn);
        }
    }

    private int getEncoderRaw(boolean right) {
        return right ? encoderLastRight : encoderLastLeft - (right ? encoderOffsetRight : encoderOffsetLeft);
    }

    private double getEncoderPos(boolean right) {
        return getEncoderRaw(right) / (double)TICKS_PER_ROTATION * Math.PI * WHEEL_DIAMETER;
    }

    @Override
    public void dispose() throws Exception {

    }
}
