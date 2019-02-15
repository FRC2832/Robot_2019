package org.livoniawarriors.Robot2019.subsystems.peripherals;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.subsystems.DriveTrain;
import java.io.IOException;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import com.ctre.phoenix.sensors.PigeonIMU;

public class PeripheralSubsystem implements ISubsystem {

    private Lidar lidar;
    private Ultrasonic proxSensor;
    private PigeonIMU pigeon = new PigeonIMU(24);
    private double[] yawPitchRoll = new double[0];

    @Override
    public void init() {
        lidar = new Lidar();
        proxSensor = new Ultrasonic(0,1); //TODO: change output port and input port
        proxSensor.setEnabled(false);
        proxSensor.setAutomaticMode(false);
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
}
