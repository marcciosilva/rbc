package behaviors;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Agregacion implements Behavior {

	static GyroDirectionFinder gdf = THBall.gdf;
	// static TouchSensor leftTouchSensor = THBall.leftTouchSensor;
	static TouchSensor rightTouchSensor = THBall.rightTouchSensor;
	static NXTRegulatedMotor leftMotor = THBall.leftMotor;
	static NXTRegulatedMotor rightMotor = THBall.rightMotor;
	static OpticalDistanceSensor largaDistancia = THBall.largaDistancia;
	static OpticalDistanceSensor cortaDistancia = THBall.cortaDistancia;
	static boolean suppressed = false;
	// static NXTCam cam = THBall.cam;
	int medidaLarga;// , medidaCorta;

	@Override
	public boolean takeControl() {
		int medidaCorta = THBall.getSharpDistance(cortaDistancia);
		int medidaLarga = THBall.getSharpDistance(largaDistancia);
		// int medidaLarga = largaDistancia.getDistance();
		// int medidaCorta = cortaDistancia.getDistance();
		// RConsole.println("A - Larga - " + Integer.toString(medidaLarga));
		// RConsole.println("A - Corta - " + Integer.toString(medidaCorta));
		RConsole.println(
				"Diferencia = " + Integer.toString(Math.abs(medidaCorta - medidaLarga)));
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
