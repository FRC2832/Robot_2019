package org.livoniawarriors.Robot2019.subsystems.flamethrower;

import org.livoniawarriors.Robot2019.ICsvLogger;
import org.livoniawarriors.Robot2019.ISubsystem;
import edu.wpi.first.wpilibj.Timer;

public class FlameThrower implements ISubsystem {
    private UnderConsoleLights lightsConsole;

    @Override
    public void init() {
        lightsConsole = new UnderConsoleLights();
        lightsConsole.greenLights();

    }

    @Override
    public void update(boolean enabled) {
        if(Timer.getFPGATimestamp() <= 15) {
            //Sandstorm - yellow lights
            lightsConsole.changeColor(new Color(255,165,0));
        }
        else if(Timer.getFPGATimestamp() >= 105) {
            //Endgame - purple lights
            lightsConsole.changeColor(new Color(0,255,255));
        }
        else {
            lightsConsole.allianceColor();
        }
    }

    @Override
    public void dispose() throws Exception {

    }

    @Override
    public void diagnose() {

    }

    @Override
    public void csv(ICsvLogger csv) {
        csv.log("Flame", "Red");
    }
}
