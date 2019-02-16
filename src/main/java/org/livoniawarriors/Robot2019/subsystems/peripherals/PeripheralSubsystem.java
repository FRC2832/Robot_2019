package org.livoniawarriors.Robot2019.subsystems.peripherals;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.subsystems.DriveTrain;
import java.io.IOException;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import com.ctre.phoenix.sensors.PigeonIMU;

public class PeripheralSubsystem implements ISubsystem {

    private Lidar lidar;
    private Ultrasonic proxSensor;
    private PigeonIMU pigeon = new PigeonIMU(DriveTrain.DRIVE_MOTER_FR);
    private double[] yawPitchRoll = new double[0];

    private static final int PRESSURE_SENSOR_PORT = 1;
    private AnalogInput pressureSensor;

    private REVDigitBoard digitBoard;

    @Override
    public void init() {
        lidar = new Lidar();
        proxSensor = new Ultrasonic(0,1); //TODO: change output port and input port
        proxSensor.setEnabled(false);
        proxSensor.setAutomaticMode(false);
        pressureSensor = new AnalogInput(PRESSURE_SENSOR_PORT);
        digitBoard = new REVDigitBoard();

    }

    @Override
    public void update(boolean enabled) {
        lidar.update();
        digitBoard.display(Integer.toString((int)getPressure()));

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
        return 250d * pressureSensor.getVoltage() / 5d - 25d;
    }
}
