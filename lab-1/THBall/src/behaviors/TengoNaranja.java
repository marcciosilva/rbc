package behaviors;

import lejos.nxt.ColorSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.robotics.subsumption.Behavior;
import main.THBall;
import main.THBall.TurnSide;

public class TengoNaranja implements Behavior {

	static GyroDirectionFinder gdf = THBall.gdf;
	static boolean suppressed = false;
	static TouchSensor touchSensor = THBall.leftTouchSensor;
	static ColorSensor colorSensor = THBall.colorSensor;
	static NXTRegulatedMotor catapulta = THBall.catapulta;
	static NXTRegulatedMotor leftMotor = THBall.leftMotor;
	static NXTRegulatedMotor rightMotor = THBall.rightMotor;
	static int error = 5;

	public static boolean inRange(int min, int max, int value) {
		// true if value is in range of reference
		return ((value > min + error || value > min - error) && (value < max + error || value < max - error));
	}

	@Override
	public boolean takeControl() {
		ColorSensor.Color color = colorSensor.getColor();
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		return (inRange(85, 115, r) && inRange(85, 120, g) && inRange(30, 50, b))
				&& (!THBall.inRangeAngle(THBall.modAngulo(gdf.getDegrees()), 90.0f, THBall.ERROR_PERMITIDO_ANGULO));
	}

	@Override
	public void action() {
		suppressed = false;
		THBall.stopMoving();
		THBall.atrasar(250);
		THBall.turnTo(90.0f);
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
