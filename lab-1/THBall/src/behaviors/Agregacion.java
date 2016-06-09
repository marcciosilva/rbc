package behaviors;

import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import main.THBall;

public class Agregacion implements Behavior {

	static OpticalDistanceSensor largaDistancia = THBall.largaDistancia;
	static OpticalDistanceSensor cortaDistancia = THBall.cortaDistancia;
	static boolean suppressed = false;

	@Override
	public boolean takeControl() {
		Delay.msDelay(1000);
		int medidaCorta = THBall.cortaDistanciaPromedio;
		int medidaLarga = THBall.largaDistanciaPromedio;
		RConsole.println("A - Larga - " + Integer.toString(medidaLarga));
		RConsole.println("A - Corta - " + Integer.toString(medidaCorta));
		// RConsole.println("Larga = " + Integer.toString(medidaLarga) +
		// ", Corta = "
		// + Integer.toString(medidaCorta));
		RConsole.println("Diferencia = "
				+ Integer.toString(Math.abs(medidaCorta - medidaLarga)));
		RConsole.println("###########################################");
		// return (((THBall.inRange(medidaLarga, 500.0f, 100.0f))
		// && (THBall.inRange(medidaLarga, medidaCorta, 50.0f)))
		// || ((THBall.inRange(medidaCorta, 500.0f, 100.0f))
		// && (THBall.inRange(medidaLarga, medidaCorta, 50.0f))));
		// && (((float) (Math.abs(medidaLarga - medidaCorta)) >= 50.0f));
		// return ((medidaLarga >= 500.0f
		// || (medidaCorta >= 500.0f && medidaCorta <= 750.0f))
		// // && (Math.abs(medidaLarga-medidaCorta) < 150)
		// && (THBall.inRange(Math.abs(medidaLarga - medidaCorta), 200, 20))
		// && (!THBall.inRange(Math.abs(medidaLarga - medidaCorta), 50, 20)));
		// int numObj = cam.getNumberOfObjects();
		// RConsole.println("Numobjects = " + Integer.toString(numObj));
		// if (numObj > 0) {
		// java.awt.Rectangle rect = cam.getRectangle(0);
		// RConsole.println(rect.toString());
		// // medidaLarga = THBall.getSharpDistance(largaDistancia);
		// return (THBall.inRange(rect.x + rect.width / 2.0f, 176 / 2.0f,
		// 30.0f));
		// && (medidaLarga >= 500.0f && medidaLarga <= 800.0f);
		// } else
		// return false;
		return false;
	}

	@Override
	public void action() {
		RConsole.println("Ejecutando Agregacion");// : largo = " +
													// Integer.toString(medidaLarga));
		// + ", corto = " + Integer.toString(medidaCorta));
		suppressed = false;
		THBall.atrasar();
		while (!suppressed) {
			Thread.yield();
		}

		// while (!suppressed && cortaDistancia.getDistance() > 400.0f) {
		// Thread.yield();
		// }

		// int numObj;
		// java.awt.Rectangle rect = null;
		// do {
		// numObj = cam.getNumberOfObjects();
		// if (numObj > 0) {
		// rect = cam.getRectangle(0);
		// }
		// } while (!suppressed && rect != null
		// && (THBall.inRange(rect.x + rect.width / 2.0f, 176 / 2.0f, 30.0f)
		// && (THBall.getSharpDistance(largaDistancia) > 400.0f)));

	}

	@Override
	public void suppress() {
		suppressed = true;
		THBall.stopMoving();

	}

}
