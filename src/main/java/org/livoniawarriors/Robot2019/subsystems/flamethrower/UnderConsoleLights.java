package org.livoniawarriors.Robot2019.subsystems.flamethrower;

import java.util.ArrayList;
import java.lang.Math;

public class UnderConsoleLights {
    private ArrayList<LightDrive> lightDrives = new ArrayList<>();
    private final int NUM_DRIVES = 1;

    //TODO: add appropriate number of drives

    public void init() {
        for(int i = 0; i < NUM_DRIVES; i++) { 
            lightDrives.add(new LightDrive());
        }
    }

    private void changeColor(Color color) {
        for(LightDrive ld : lightDrives) {
            ld.setColor(1, color, 0.8f);
            ld.setColor(2, color, 0.8f);
            ld.setColor(3, color, 0.8f);
            ld.setColor(4, color, 0.8f);
            ld.update();
        }
    }

    private void changeColorVar(Color color, int varFact) {
        short tempRed = color.getRed();
        short tempGreen = color.getGreen();
        short tempBlue = color.getBlue();

        for(LightDrive ld : lightDrives) {
            for(int i = 1; i <=4; i++) {
                ld.setColor(i, new Color((tempRed + (int)(Math.random() * varFact)), 
                    (tempGreen + (int)(Math.random() * varFact)), 
                    (tempBlue + (int)(Math.random() * varFact))), 0.8f);
            }
            ld.update();
        }
    }

    public void redLights() {
        changeColor(Color.RED);
    }

    public void blueLights() {
        changeColor(Color.BLUE);
    }

    public void greenLights() {
        changeColor(Color.GREEN);
    }

    public void whiteLights() {
        changeColor(Color.WHITE);
    }

    public void lightsOff() {
        changeColor(Color.OFF);
    }
}