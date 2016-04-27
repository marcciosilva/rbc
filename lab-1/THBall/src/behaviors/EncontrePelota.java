package behaviors;

import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.NXTCam;
import lejos.nxt.remote.RemoteMotor;
import lejos.robotics.Color;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class EncontrePelota implements Behavior {
	private static NXTCam camera = THBall.camera;
	private static UltrasonicSensor ultrasonicSensor = THBall.ultrasonicSensor;
	private static RemoteMotor pinzaIzq = THBall.pinzaIzq;
	private static RemoteMotor pinzaDer = THBall.pinzaDer;
	private int distancia = 13;

	@Override
	public boolean takeControl() {
		camera.enableTracking(true);
		boolean hayPelota = false;
		for (int i = 0; i < camera.getNumberOfObjects(); i++) {
			if (camera.getObjectColor(i) == Color.ORANGE) {
				hayPelota = true;
				break;
			}
		}
		camera.enableTracking(false);
		if (!hayPelota) {
			return false;
		} else {
			if (ultrasonicSensor.getDistance() < distancia) {
				return true;
			} else {
				return true;
			}
		}
	}

	@Override
	public void action() {
		THBall.girarPinzas(-50);
	}

	@Override
	public void suppress() {

	}

}
