package behaviors;

import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Dispersion implements Behavior {

	static OpticalDistanceSensor largaDistancia = THBall.largaDistancia;
	static OpticalDistanceSensor cortaDistancia = THBall.cortaDistancia;
	static boolean suppressed = false;

	@Override
	public boolean takeControl() {
		// int numObj = cam.getNumberOfObjects();
		// if (numObj > 0) {
		// java.awt.Rectangle rect = cam.getRectangle(0);
		// return (THBall.inRange(rect.x + rect.width / 2.0f, 176 / 2.0f,
		// 30.0f));
		// // && (THBall.getSharpDistance(largaDistancia) <= 350.0f);
		// } else
		return false;
		// medidaLarga = THBall.getSharpDistance(largaDistancia);
		// medidaCorta = THBall.getSharpDistance(cortaDistancia);
		// int medidaLarga = largaDistancia.getDistance();
		// int medidaCorta = cortaDistancia.getDistance();
		// RConsole.println("D - Larga - " + Integer.toString(medidaLarga));
		// RConsole.println("D - Corta - " + Integer.toString(medidaCorta));
		// return ((medidaLarga <= 350.0f || medidaCorta <= 350.0f)
		// // && (Math.abs(medidaLarga-medidaCorta) < 150)
		// && (THBall.inRange(Math.abs(medidaLarga - medidaCorta), 200, 20))
		// && (!THBall.inRange(Math.abs(medidaLarga - medidaCorta), 50, 20)));
		// && (THBall.inRange(medidaLarga, medidaCorta, 150.0f))
		// && (!THBall.inRange(medidaLarga, medidaCorta, 50.0f)));

		// && (((float) (Math.abs(medidaLarga - medidaCorta)) >= 50.0f)));

		// return ((THBall.inRange(medidaLarga, 300.0f, 10.0f))
		// && (THBall.inRange(medidaLarga, medidaCorta, 50.0f)))
		// || ((THBall.inRange(medidaCorta, 300.0f, 10.0f))
		// && !(THBall.inRange(medidaLarga, medidaCorta, 50.0f)));
	}

	@Override
	public void action() {
		suppressed = false;
		THBall.setSpeed(THBall.SPEED_DRIVE);
		THBall.avanzar();
		while (!suppressed) {
			Thread.yield();
		}
		// int numObj;
		// java.awt.Rectangle rect = null;
		// do {
		// numObj = cam.getNumberOfObjects();
		// if (numObj > 0) {
		// rect = cam.getRectangle(0);
		// }
		// } while (!suppressed && rect != null
		// && (THBall.inRange(rect.x + rect.width / 2.0f, 176 / 2.0f, 30.0f)
		// && (THBall.getSharpDistance(largaDistancia) <= 350.0f)));
	}

	@Override
	public void suppress() {
		suppressed = true;
		THBall.stopMoving();
	}

}
