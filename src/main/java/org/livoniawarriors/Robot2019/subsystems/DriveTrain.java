package org.livoniawarriors.Robot2019.subsystems;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;

public class DriveTrain implements ISubsystem {

    private static final double TICKS_PER_ROTATION = 1024;
    private static final double WHEEL_DIAMETER = 8;

    private static final double ENCODER_RATIO = WHEEL_DIAMETER * Math.PI / TICKS_PER_ROTATION;

    public final static int DRIVE_MOTER_FL = 25;
    public final static int DRIVE_MOTER_FR = 10;
    public final static int DRIVE_MOTER_BL = 24;
    public final static int DRIVE_MOTER_BR = 11;

    private DifferentialDrive drive;
    private PowerDistributionPanel pdp;
    private SensorCollection rightEncoder, leftEncoder;

    private double prevPosLeft;
    private double prevPosRight;

    @Override
    public void csv(ICsvLogger csv) {
        csv.log("Voltage", pdp.getVoltage());
    }

    @Override
    public void diagnose() {

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
        pdp = new PowerDistributionPanel();
    }


    public void tankDrive(double left, double right) {
        tankDrive(left, right, true);
    }


    public void tankDrive(double left, double right, boolean squaredInputs) {
        drive.tankDrive(left, right, squaredInputs); 
    }

    @Override
    public void update(boolean enabled) {

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
