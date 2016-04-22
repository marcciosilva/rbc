package behaviors;

import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.NXTCam;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class CerrarPinza implements Behavior {
	public static NXTCam camera = THBall.camera;
	public static UltrasonicSensor ultrasonicSensor = THBall.ultrasonicSensor;

	@Override
	public boolean takeControl() {

		if (ultrasonicSensor.getDistance() < 10) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void action() {

	}

	@Override
	public void suppress() {

	}

}
