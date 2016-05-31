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
	static TouchSensor leftTouchSensor = THBall.leftTouchSensor;
	static TouchSensor rightTouchSensor = THBall.rightTouchSensor;
	static NXTRegulatedMotor leftMotor = THBall.leftMotor;
	static NXTRegulatedMotor rightMotor = THBall.rightMotor;
	static OpticalDistanceSensor largaDistancia = THBall.largaDistancia;
	static OpticalDistanceSensor cortaDistancia = THBall.cortaDistancia;
	static boolean suppressed = false;

	@Override
	public boolean takeControl() {
		int medidaLarga = largaDistancia.getDistance();
		int medidaCorta = cortaDistancia.getDistance();
		// RConsole.println("A - Larga - " + Integer.toString(medidaLarga));
		// RConsole.println("A - Corta - " + Integer.toString(medidaCorta));
		return (((THBall.inRange(medidaLarga, 500.0f, 100.0f))
				&& (THBall.inRange(medidaLarga, medidaCorta, 50.0f)))
				|| ((THBall.inRange(medidaCorta, 500.0f, 100.0f))
						&& (THBall.inRange(medidaLarga, medidaCorta, 50.0f))));
		// && (((float) (Math.abs(medidaLarga - medidaCorta)) >= 50.0f));
	}

	@Override
	public void action() {
		RConsole.println("Ejecutando Agregacion");
		suppressed = false;
		THBall.atrasar();
		while (!suppressed && cortaDistancia.getDistance() > 400.0f) {
			Thread.yield();
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
		THBall.stopMoving();

	}

}
