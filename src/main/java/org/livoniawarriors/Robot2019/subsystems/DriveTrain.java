package org.livoniawarriors.Robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
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

    private static final double TICKS_PER_ROTATION = 1024;
    private static final double WHEEL_DIAMETER = 8;

    private static final double ENCODER_RATIO = WHEEL_DIAMETER * Math.PI / TICKS_PER_ROTATION;
    private final static double WHEEL_BASE_WIDTH = 2; // TODO: Fix

    public final static int DRIVE_MOTER_FL = 25;
    public final static int DRIVE_MOTER_FR = 10;
    public final static int DRIVE_MOTER_BL = 24;
    public final static int DRIVE_MOTER_BR = 11;

    private DifferentialDrive drive;
    private SensorCollection rightEncoder, leftEncoder;

    private double prevPosLeft;
    private double prevPosRight;
    Trajectory.Config pathConfig;
    private boolean auto;
    private double startTime;

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

        talonBackLeft.follow(talonFrontLeft);
        talonBackRight.follow(talonFrontRight);

        talonFrontLeft.setInverted(true);
        talonFrontRight.setInverted(true);
        talonBackLeft.setInverted(true);
        talonBackRight.setInverted(true);

        drive = new DifferentialDrive(talonFrontLeft, talonFrontRight);
        leftFollower = new EncoderFollower();
        leftEncoder = talonBackLeft.getSensorCollection();
        leftFollower.configureEncoder(leftEncoder.getQuadraturePosition(), TICKS_PER_ROTATION, WHEEL_DIAMETER);
        leftFollower.configurePIDVA(P, I, D, 1/ MAX_VELOCITY, ACCELERATION_GAIN);
        rightFollower = new EncoderFollower();
        rightEncoder = talonBackRight.getSensorCollection();
        rightFollower.configureEncoder(rightEncoder.getQuadraturePosition(), TICKS_PER_ROTATION, WHEEL_DIAMETER);
        rightFollower.configurePIDVA(P, I, D, 1/ MAX_VELOCITY, ACCELERATION_GAIN);
        leftFollower.setTrajectory(new Trajectory(0));
        rightFollower.setTrajectory(new Trajectory(0));
        pathConfig = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, 0.05, 1.7, 2.0, 60.0); // Might need to convert to imperial
    }


    public void tankDrive(double left, double right) {
        tankDrive(left, right, true);
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
        Robot.userInput.putValue("tab", "encoderL", leftEncoder.getQuadraturePosition());
        Robot.userInput.putValue("tab", "encoderR", rightEncoder.getQuadraturePosition());

        if(!enabled)
            return;

        if(auto && !isTrajectoryDone()) {
            double l = leftFollower.calculate(leftEncoder.getQuadraturePosition());
            double r = leftFollower.calculate(rightEncoder.getQuadraturePosition());

            double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
            double turn = 0.8 * (-1.0 / 80.0) * angleDifference;

            drive.tankDrive(l + turn, r - turn);
        }

    }

    private double getEncoderInInches(SensorCollection encoder) {
        double raw = encoder.getQuadraturePosition();
        return raw * ENCODER_RATIO;
    }

    /**
     * Drive forward a distance using encoders, defaulting to left encoder
     * @param distance the target distance
     * @param reset whether or not to reset postion to measure from
     * @return true is it has driven the target distance
     */
    public boolean driveForwardLazy(double distance, boolean reset) {
        double enc = getEncoderInInches(leftEncoder);
        if(reset) {
            prevPosLeft = getEncoderInInches(leftEncoder);
            prevPosRight = getEncoderInInches(rightEncoder);
        }
        //Using left encoder
        if(enc - prevPosLeft >= distance) {
            tankDrive(0,0);
            return true;
        } else {
            tankDrive(0.8, 0.8);
            return false;
        }
    }

    /**
     * Turns to a given angle by comparing position to starting angle
     * @param angle the turn angle desired
     * @return true when turning is complete
     */
    public boolean turnLazy(double angle) {
        double start = Robot.peripheralSubsystem.getYaw();
        if(angle < 0) { //We want to turn right
            if(Math.abs(start) < Math.abs(angle)) {
                tankDrive(0.6, -0.6); //Turn right
                return false;
            } else {
                return true; //Done turning
            }
        } else { //We want to turn left
            if(Math.abs(start) < Math.abs(angle)) {
                tankDrive(-0.6, 0.6); //Turn left
                return false;
            } else {
                return true; //Done turning
            }
        }
    }

    /**
     * Get the current position in inches based on change in velocity over given time, retrieved from encoders
     * @param startPos the starting position, from which to count
     * @param startTime the time from which to start measuring
     * @return position in inches
     */
    public double deadReckon(double startPos, long startTime) {
        double velocity = leftEncoder.getQuadratureVelocity() * 10;
        double velocityINpS = velocity * ENCODER_RATIO;
        double changeTime = System.nanoTime() - startTime / 1000000;
        return velocityINpS * changeTime;
    }

    @Override
    public void dispose() throws Exception {

    }
}
