package behaviors;

import lejos.nxt.ColorSensor;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.robotics.subsumption.Behavior;
import main.THBall;
import main.THBall.TurnSide;
import utils.SensorColor;

public class TirarNaranja implements Behavior {

	static GyroDirectionFinder gdf = THBall.gdf;
	SensorColor colorSensor;
	static TouchSensor leftTouchSensor = THBall.leftTouchSensor;
	static TouchSensor rightTouchSensor = THBall.rightTouchSensor;
	static int error = 5;
	static boolean suppressed = false;

	public TirarNaranja(SensorColor cs) {
		colorSensor = cs;
	}

	public static boolean inRange(int min, int max, int value) {
		// true if value is in range of reference
		return ((value > min + error || value > min - error) && (value < max + error || value < max
				- error));
	}

	@Override
	public boolean takeControl() {
		// al no ser el primero en pedir una medida en un ciclo del arbitrator
		// no necesita pedir una nueva medida, usa la anterior
		ColorSensor.Color color = colorSensor.getMeasurement(false);
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		return (r >= 100 && g >= 100 && b >= 40);
	}

	@Override
	public void action() {
		suppressed = false;
		THBall.timer = System.currentTimeMillis();
		THBall.stopMoving();
		THBall.atrasar(250);
		turnTo(90.0f);
		THBall.avanzar();
		while ((!leftTouchSensor.isPressed() && !rightTouchSensor.isPressed())
				&& !suppressed)
			;
		// si el comportamiento no fue suprimido por el evitar deadlock
		if (!suppressed) {
			THBall.stopMoving();
			THBall.atrasar(250);
			turnTo(270);
			THBall.tirarPelota();
		}
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
			THBall.turnRight(THBall.SPEED_TURN);
		} else {
			turnSide = TurnSide.LEFT;
			THBall.turnLeft(THBall.SPEED_TURN);
		}
		while (!suppressed) {
			// Delay.msDelay(1);
			// THBall.timer++;
			anguloActual = THBall.modAngulo(gdf.getDegrees());
			if (THBall.inRangeAngle(anguloActual, anguloObjetivo,
					THBall.ERROR_PERMITIDO_ANGULO)) {
				THBall.stopMoving();
				break;
			}
			if ((THBall.FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.RIGHT)
					&& (turnSide != TurnSide.RIGHT)) {
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
