package behaviors;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Corregir implements Behavior {

	private float anguloActual = 0.0f;
	private CompassHTSensor compass = THBall.compass;
	private NXTRegulatedMotor leftMotor = THBall.leftMotor;
	private NXTRegulatedMotor rightMotor = THBall.rightMotor;

	@Override
	public boolean takeControl() {
		anguloActual = compass.getDegreesCartesian();
		// if (THBall.facingLeft == THBall.originalFacing) anguloDeseado = 0.0f;
		// else anguloDeseado = 180.0f;
		// toma control si se desvia mas de 2 grados
		if (originalFacing()) {
			return (anguloActual < 358 || anguloActual > 2);
		} else {
			return (anguloActual < 182 || anguloActual > 178);
		}
	}

	private boolean desviadoDerecha() {
		if (originalFacing()) {
			return anguloActual < 360 && anguloActual > 180;
		} else {
			return anguloActual < 180 && anguloActual > 0;
		}
	}

	private boolean originalFacing() {
		return THBall.facingLeft == THBall.originalFacing;
	}

	private boolean inRange(float angulo) {
		// devuelve true si se esta en un rango aceptable
		// del angulo ideal
		if (originalFacing()) {
			if (desviadoDerecha()) {
				return angulo == 0 || (angulo <= 359.9 && angulo >= 359);
			} else {
				return angulo <= 1 && angulo >= 0;
			}
		} else {
			if (desviadoDerecha()) {
				return angulo <= 180 && angulo >= 179;
			} else {
				return angulo <= 181 && angulo >= 180;
			}
		}
	}

	@Override
	public void action() {
		THBall.stopMoving();
		THBall.setSpeed(THBall.SPEED_TURN);
		if (desviadoDerecha()) {
			rightMotor.forward();
		} else {
			leftMotor.forward();
		}
		while (!inRange(anguloActual)) {
			anguloActual = compass.getDegreesCartesian();
			// doy lugar a que ejecute algun comportamiento de mayor
			// prioridad
			Thread.yield();
			THBall.sleep(50);
		}
		THBall.stopMoving();
	}

	@Override
	public void suppress() {
		THBall.stopMoving();
	}

}
