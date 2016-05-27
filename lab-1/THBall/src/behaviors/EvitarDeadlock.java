package behaviors;

import lejos.nxt.addon.GyroDirectionFinder;
import lejos.robotics.subsumption.Behavior;
import main.THBall;
import main.THBall.TurnSide;

public class EvitarDeadlock implements Behavior {

	static boolean suppressed = false;
	static GyroDirectionFinder gdf = THBall.gdf;

	@Override
	public boolean takeControl() {
		// alrededor de 26 segundos
		// return THBall.timer >= 7500;
		return System.currentTimeMillis() - THBall.timer >= 20000;
	}

	@Override
	public void action() {
		suppressed = false;
		THBall.stopMoving();
		THBall.atrasar((int) (Math.random() * 1750 + 250));
		turnBy(90.0f);
		THBall.timer = System.currentTimeMillis();
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
