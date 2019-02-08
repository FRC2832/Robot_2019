package org.livoniawarriors.Robot2019.subsystems.peripherals;

import org.livoniawarriors.Robot2019.ISubsystem;
import org.livoniawarriors.Robot2019.Robot;
import org.livoniawarriors.Robot2019.UserInput;
import org.livoniawarriors.Robot2019.subsystems.DriveTrain;
import java.io.IOException;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import com.ctre.phoenix.sensors.PigeonIMU;

public class PeripheralSubsystem implements ISubsystem {

    private Lidar lidar;
    private Ultrasonic proxSensor;
    private PigeonIMU pigeon = new PigeonIMU(DriveTrain.DRIVE_MOTER_FR);
    private double[] yawPitchRoll = new double[0];
    public ShuffleboardTab tab;
    private Double currentYaw;

    @Override
    public void init() {
        lidar = new Lidar();
        proxSensor = new Ultrasonic(-1,-1); //TODO: change output port and input port
        proxSensor.setEnabled(false);
        proxSensor.setAutomaticMode(false);
        //Robot.userInput.createValue(tab, "yaw", 69, currentYaw);
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
}
