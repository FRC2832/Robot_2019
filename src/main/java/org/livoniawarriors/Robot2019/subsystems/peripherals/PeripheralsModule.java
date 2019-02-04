package org.livoniawarriors.Robot2019.subsystems.peripherals;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

public class PeripheralsModule
{   
    private Ultrasonic proxSensor;
    
    public void init()
    {
        proxSensor = new Ultrasonic(-1,-1); //TODO: change output port and input port
        proxSensor.setEnabled(false);
        proxSensor.setAutomaticMode(false);
    }

    public void resetEncoder(Encoder e)
    {
        e.reset();
    }

    public double encoderDist(Encoder e, double radius)
    {
        return e.getRaw() * 2*Math.PI*radius;
    }

    public double proxSensorDistance()
    {
        proxSensor.setEnabled(true);
        proxSensor.ping();
        return proxSensor.getRangeMM();
    }
    
}