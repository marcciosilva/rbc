package behaviors;

import lejos.nxt.TouchSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import main.THBall;
import main.THBall.TurnSide;

public class Avoid implements Behavior {

	static GyroDirectionFinder gdf = THBall.gdf;
	static TouchSensor leftTouchSensor = THBall.leftTouchSensor;
	static TouchSensor rightTouchSensor = THBall.rightTouchSensor;
	static boolean suppressed = false;

	@Override
	public boolean takeControl() {
		return leftTouchSensor.isPressed() && rightTouchSensor.isPressed();
	}

	@Override
	public void action() {
		RConsole.println("Ejecutando Avoid");
		THBall.timer = System.currentTimeMillis();
		suppressed = false;
		THBall.avanzar();
		Delay.msDelay(1000);
		THBall.stopMoving();
		THBall.resetGyro();
		// retrocede hasta estar a por lo menos 20 cm
		// de algun obstaculo
		THBall.atrasar((int) (Math.random() * 1750 + 250));
		turnBy(90.0f);
	}

	public static void turnBy(float angulo) {
		float anguloActual = THBall.modAngulo(gdf.getDegrees());
		float anguloObjetivo = anguloActual + THBall.modAngulo(angulo);
		turnTo(anguloObjetivo);
	}

	public static void turnTo(float anguloObjetivo) {
		float anguloActual = THBall.modAngulo(gdf.getDegrees());
		// por si me pasan un valor de afuera y no desde turnBy
		anguloObjetivo = THBall.modAngulo(anguloObjetivo);
		TurnSide turnSide;
		if (THBall.FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.RIGHT) {
			turnSide = TurnSide.RIGHT;
			THBall.turnRight(THBall.SPEED_TURN);
		} else {
			turnSide = TurnSide.LEFT;
			THBall.turnLeft(THBall.SPEED_TURN);
		}
		while (!suppressed) {
			// Delay.msDelay(1);
			// THBall.timer++;
			anguloActual = THBall.modAngulo(gdf.getDegrees());
			if (THBall.inRangeAngle(anguloActual, anguloObjetivo, THBall.ERROR_PERMITIDO_ANGULO)) {
				THBall.stopMoving();
				break;
			}
			if ((THBall.FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.RIGHT) && (turnSide != TurnSide.RIGHT)) {
				turnSide = TurnSide.RIGHT;
				THBall.turnRight(THBall.SPEED_TURN);
			} else if ((THBall.FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.LEFT)
					&& turnSide != TurnSide.LEFT) {
				turnSide = TurnSide.LEFT;
				THBall.turnLeft(THBall.SPEED_TURN);
			}
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
		THBall.stopMoving();
	}

}
