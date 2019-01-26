package org.livoniawarriors.Robot2019;

public class PeripheralsModule
{   
    private DigitalInput limSwitch;
    private Ultrasonic proxSensor;
    
    public void init()
    {
        limSwitch = new DigitalInput(-1); //TODO: change port
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

    public boolean limSwitchStatus()
    {
        return limSwitch.get();
    }

    public double proxSensorDistance()
    {
        setEnabled(true);
        proxSensor.ping();
        return proxSensor.getRangeMM();
    }
}