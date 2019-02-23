package org.livoniawarriors.Robot2019.auton;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Notifier;

public class AutonDeadReckoning {

	private static final int ENCODER_FRONT_LEFT = 0;
	private static final int ENCODER_FRONT_RIGHT = 0;
	private static final int ENCODER_BACK_RIGHT = 0;
	private static final int ENCODER_BACK_LEFT = 0;

	private final double ENCODER_CONVERSION = 1;

	Encoder encoderFL;
	Encoder encoderFR;
	Encoder encoderBL;
	Encoder encoderBR;

	//private final Notifier timer;

	public void init() {
	//    timer = new Notifier(this::timer);
		encoderFL = new Encoder(0, 1);
		encoderFR = new Encoder(2, 3);
		encoderBL = new Encoder(4, 5);
		encoderBR = new Encoder(6, 7);

	}

	private void driveTime(double time) {

	}

	private void readEncoder(Encoder encoder) {
		
	}

	private void deadReckoning() {
		double pos = 0;
	}

	private void timer() {
		
	}

}