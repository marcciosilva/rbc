package behaviors;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Dispersion implements Behavior {

	static NXTRegulatedMotor leftMotor = THBall.leftMotor;
	static NXTRegulatedMotor rightMotor = THBall.rightMotor;
	static OpticalDistanceSensor largaDistancia = THBall.largaDistancia;
	static OpticalDistanceSensor cortaDistancia = THBall.cortaDistancia;
	static boolean suppressed = false;

	@Override
	public boolean takeControl() {
		int medidaLarga = largaDistancia.getDistance();
		int medidaCorta = cortaDistancia.getDistance();
		// RConsole.println("D - Larga - " + Integer.toString(medidaLarga));
		// RConsole.println("D - Corta - " + Integer.toString(medidaCorta));
		return ((medidaLarga <= 350.0f || medidaCorta <= 350.0f)
				&& (THBall.inRange(medidaLarga, medidaCorta, 50.0f)));
		// && (((float) (Math.abs(medidaLarga - medidaCorta)) >= 50.0f)));

		// return ((THBall.inRange(medidaLarga, 300.0f, 10.0f))
		// && (THBall.inRange(medidaLarga, medidaCorta, 50.0f)))
		// || ((THBall.inRange(medidaCorta, 300.0f, 10.0f))
		// && (THBall.inRange(medidaLarga, medidaCorta, 50.0f)));
	}

	@Override
	public void action() {
		RConsole.println("Ejecutando Dispersion");
		suppressed = false;
		THBall.setSpeed(THBall.SPEED_DRIVE);
		THBall.avanzar();
		while (!suppressed && cortaDistancia.getDistance() <= 350.0f) {
			Thread.yield();
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
		THBall.stopMoving();
	}

}
