package org.livoniawarriors.Robot2019.subsystems.peripherals;

import edu.wpi.first.cameraserver.CameraServer;
import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput;
import org.livoniawarriors.Robot2019.subsystems.DriveTrain;
import java.io.IOException;
import java.util.Random;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Notifier;

import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import com.ctre.phoenix.sensors.PigeonIMU;
import org.apache.logging.log4j.Level;

public class PeripheralSubsystem implements ISubsystem {

    private Lidar lidar;
    private Ultrasonic proxSensor;
    private PigeonIMU pigeon;
    private double[] yawPitchRoll = new double[3];

    private static final int PRESSURE_SENSOR_PORT = 1;
    private AnalogInput pressureSensor;

    private Compressor compressor;

    private Notifier notifier;
    private final static double REV_ROBOTICS_DIGIT_MXP_DISPLAY_UPDATE_PERIOD = .25;

    private REVDigitBoard digitBoard;

    @Override
    public void init() {
        lidar = new Lidar();
        proxSensor = new Ultrasonic(0,1); //TODO: change output port and input port
        proxSensor.setEnabled(false);
        proxSensor.setAutomaticMode(false);
        pigeon = new PigeonIMU(DriveTrain.DRIVE_MOTER_FR);
        pressureSensor = new AnalogInput(PRESSURE_SENSOR_PORT);
        compressor = new Compressor();
        compressor.start();
        digitBoard = new REVDigitBoard();
        notifier = new Notifier(() -> digitBoard.display(Integer.toString((int)getPressure())));
        notifier.startPeriodic(REV_ROBOTICS_DIGIT_MXP_DISPLAY_UPDATE_PERIOD);
    }

    @Override
    public void update(boolean enabled) {
        lidar.update();


    }

    @Override
    public void dispose() throws IOException {
        lidar.dispose();
    }
    @Override
    public void csv(ICsvLogger csv) {

    }

    @Override
    public void diagnose() {

    }
    public void resetEncoder(Encoder e){
        e.reset();
    }

    public double encoderDist(Encoder e, double radius){
        return e.getRaw() * 2*Math.PI*radius;
    }

    public double proxSensorDistance(){
        proxSensor.setEnabled(true);
        proxSensor.ping();
        return proxSensor.getRangeMM();
    }

    public double getYaw() {
        //returns the yaw; copy method and change array element to get pitch or roll
        pigeon.getYawPitchRoll(yawPitchRoll);
        return yawPitchRoll[0];
    }



    public double getPressure() {
        return 250d * pressureSensor.getAverageVoltage() / 5d - 25d;
        
    }
}
