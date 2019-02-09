package org.livoniawarriors.Robot2019.subsystems;

import org.livoniawarriors.Robot2019.Lidar;

import edu.wpi.first.wpilibj.AnalogInput;
import org.livoniawarriors.Robot2019.ISubsystem;

import java.io.IOException;

public class PeripheralSubsystem implements ISubsystem {
    private static final int PRESSURE_SENSOR_PORT = 1;
    private Lidar lidar;
    private AnalogInput pressureSensor;

    @Override
    public void init() {
        lidar = new Lidar();
        pressureSensor = new AnalogInput(PRESSURE_SENSOR_PORT);
    }

    @Override
    public void update(boolean enabled) {
        lidar.update();
    }

    @Override
    public void dispose() throws IOException {
        lidar.dispose();
    }

    public double getPressure() {
        return 250d * (double)pressureSensor.getVoltage() / 5d - 25d;
    }
}
