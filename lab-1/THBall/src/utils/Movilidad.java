package utils;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;
import sensors.SensorGDF;
import utils.Utils.TurnSide;

public class Movilidad {

	public static NXTRegulatedMotor leftMotor = Motor.A;
	public static NXTRegulatedMotor rightMotor = Motor.C;
	public static NXTRegulatedMotor catapulta = Motor.B;

	/**
	 * El robot deja de moverse
	 */
	public static void stopMoving() {
		try {
			leftMotor.stop(true);
			rightMotor.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Establece la velocidad de los motores de movilidad
	 */
	public static void setSpeed(int speed) {
		try {
			leftMotor.setSpeed(speed);
			rightMotor.setSpeed(speed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Baja la catapulta hacia la posicion de recoleccion
	 */
	public static void bajarCatapulta() {
		try {
			catapulta.setSpeed(Constants.CATAPULTA_MOVER);
			catapulta.rotateTo(-190, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sube la catapulta hacia la posicion de origen
	 */
	public static void subirCatapulta() {
		try {
			catapulta.setSpeed(Constants.CATAPULTA_MOVER);
			catapulta.rotateTo(0, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Se mueve la catapulta mediante la cantidad de grados indicados por
	 * parametro
	 * 
	 * @param angle
	 *            Cantidad de grados a mover
	 */
	public static void moverCatapulta(int angle) {
		try {
			catapulta.setSpeed(Constants.CATAPULTA_MOVER);
			catapulta.rotateTo(angle, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * El robot arroja la pelota
	 */
	public static void tirarPelota() {
		try {
			catapulta.setSpeed(Constants.CATAPULTA_TIRAR);
			catapulta.rotateTo(-60, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bajarCatapulta();
	}

	/**
	 * El robot avanza a velocidad SPEED_DRIVE
	 */
	public static void avanzar() {
		try {
			setSpeed(Constants.SPEED_DRIVE);
			leftMotor.forward();
			rightMotor.forward();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * El robot retrocede durante la cantidad de tiempo indicada por parametro
	 * 
	 * @param ms
	 *            Cantidad de milisegundos
	 */
	public static void atrasar(int ms) {
		try {
			setSpeed(Constants.SPEED_DRIVE);
			leftMotor.backward();
			rightMotor.backward();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Delay.msDelay(ms);
		stopMoving();
	}

	/***
	 * Gira hacia la derecha seteando la velocidad de los motores de acuerdo al
	 * parametro speed
	 * 
	 * @param speed
	 *            Nueva velocidad de los motores
	 */
	public static void turnRight(int speed) {
		try {
			setSpeed(speed);
			leftMotor.forward();
			rightMotor.backward();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * Gira hacia la izquierda seteando la velocidad de los motores de acuerdo
	 * al parametro speed
	 * 
	 * @param speed
	 *            Nueva velocidad de los motores
	 */
	public static void turnLeft(int speed) {
		try {
			setSpeed(speed);
			rightMotor.forward();
			leftMotor.backward();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * El robot se mueve hacia atras a velocidad SPEED_DRIVE
	 */
	public static void atrasar() {
		try {
			setSpeed(Constants.SPEED_DRIVE);
			leftMotor.backward();
			rightMotor.backward();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void resetearCatapulta() {
		try {
			catapulta.resetTachoCount();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void turnBy(SensorGDF gdf, float angulo, Suppressable caller) {
		float anguloActual = Utils.modAngulo(gdf.getMeasurement(true));
		float anguloObjetivo = anguloActual + Utils.modAngulo(angulo);
		turnTo(gdf, anguloObjetivo, caller);
	}

	public static void turnTo(SensorGDF gdf, float anguloObjetivo, Suppressable caller) {
		float anguloActual = Utils.modAngulo(gdf.getMeasurement(false));
		// por si me pasan un valor de afuera y no desde turnBy
		anguloObjetivo = Utils.modAngulo(anguloObjetivo);
		TurnSide turnSide;
		if (Utils.FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.RIGHT) {
			turnSide = TurnSide.RIGHT;
			Movilidad.turnRight(Constants.SPEED_TURN);
		} else {
			turnSide = TurnSide.LEFT;
			Movilidad.turnLeft(Constants.SPEED_TURN);
		}
		while (!caller.getSuppressed()) {
			anguloActual = Utils.modAngulo(gdf.getMeasurement(true));
			if (Utils.inRangeAngle(anguloActual, anguloObjetivo,
					Constants.ERROR_PERMITIDO_ANGULO)) {
				Movilidad.stopMoving();
				break;
			}
			if ((Utils.FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.RIGHT)
					&& (turnSide != TurnSide.RIGHT)) {
				turnSide = TurnSide.RIGHT;
				Movilidad.turnRight(Constants.SPEED_TURN);
			} else if ((Utils.FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.LEFT)
					&& turnSide != TurnSide.LEFT) {
				turnSide = TurnSide.LEFT;
				Movilidad.turnLeft(Constants.SPEED_TURN);
			}
		}
	}

}
