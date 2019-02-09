package org.livoniawarriors.Robot2019.subsystems.peripherals;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput;
import org.livoniawarriors.Robot2019.subsystems.DriveTrain;
import java.io.IOException;
import java.util.Random;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import com.ctre.phoenix.sensors.PigeonIMU;
import org.apache.logging.log4j.Level;

public class PeripheralSubsystem implements ISubsystem {

    private Lidar lidar;
    private Ultrasonic proxSensor;
    private PigeonIMU pigeon;
    private double[] yawPitchRoll = new double[0];
    private Double currentYaw;

    @Override
    public void init() {
        lidar = new Lidar();
        proxSensor = new Ultrasonic(0,1); //TODO: change output port and input port
        proxSensor.setEnabled(false);
        proxSensor.setAutomaticMode(false);
        pigeon = new PigeonIMU(DriveTrain.DRIVE_MOTER_FR);
    }

    @Override
    public void update(boolean enabled) {
        lidar.update();
        currentYaw = getYaw();
    }

    @Override
    public void dispose() throws IOException {
        lidar.dispose();
    }
    @Override
    public void csv(ICsvLogger csv) {

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

    public Double getYaw() {
        //returns the yaw; copy method and change array element to get pitch or roll
        pigeon.getYawPitchRoll(yawPitchRoll);
        if (yawPitchRoll.length != 0) {
            return yawPitchRoll[0];
        }
        else {
            Robot.logger.log(Level.DEBUG, "No Pigeon, seems like a layer 8 error.");
            return 0d;
        }
    }
}
