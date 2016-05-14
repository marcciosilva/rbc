package behaviors;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import main.THBall;
import main.THBall.TurnSide;

public class Avoid implements Behavior {

	// static OpticalDistanceSensor largaDistancia = THBall.largaDistancia;
	// static OpticalDistanceSensor cortaDistancia = THBall.cortaDistancia;
	static GyroDirectionFinder gdf = THBall.gdf;
	static TouchSensor leftTouchSensor = THBall.leftTouchSensor;
	static TouchSensor rightTouchSensor = THBall.rightTouchSensor;
	static NXTRegulatedMotor leftMotor = THBall.leftMotor;
	static NXTRegulatedMotor rightMotor = THBall.rightMotor;
	static NXTRegulatedMotor catapulta = THBall.catapulta;
	static boolean suppressed = false;

	@Override
	public boolean takeControl() {
		return leftTouchSensor.isPressed() && rightTouchSensor.isPressed();
	}

	@Override
	public void action() {
		suppressed = false;
		Delay.msDelay(300);
		THBall.stopMoving();
		THBall.resetGyro();
		// retrocede hasta estar a por lo menos 20 cm
		// de algun obstaculo
		THBall.atrasar(250);
		turnBy(90.0f);
	}

	public static void turnBy(float angulo) {
		float anguloActual = THBall.modAngulo(gdf.getDegrees());
		float anguloObjetivo = anguloActual + THBall.modAngulo(angulo);
		turnTo(anguloObjetivo);
	}

	public static void turnTo(float anguloObjetivo) {
		THBall.setSpeed(THBall.SPEED_TURN);
		float anguloActual = THBall.modAngulo(gdf.getDegrees());
		// por si me pasan un valor de afuera y no desde turnBy
		anguloObjetivo = THBall.modAngulo(anguloObjetivo);
		TurnSide turnSide;
		if (THBall.FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.RIGHT) {
			turnSide = TurnSide.RIGHT;
			THBall.turnRight();
		} else {
			turnSide = TurnSide.LEFT;
			THBall.turnLeft();
		}
		while (!suppressed) {
			anguloActual = THBall.modAngulo(gdf.getDegrees());
			if (THBall.inRangeAngle(anguloActual, anguloObjetivo, THBall.ERROR_PERMITIDO_ANGULO)) {
				THBall.stopMoving();
				break;
			}
			if ((THBall.FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.RIGHT) && (turnSide != TurnSide.RIGHT)) {
				turnSide = TurnSide.RIGHT;
				THBall.turnRight();
			} else if ((THBall.FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.LEFT)
					&& turnSide != TurnSide.LEFT) {
				turnSide = TurnSide.LEFT;
				THBall.turnLeft();
			}
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
		THBall.stopMoving();
	}

}
