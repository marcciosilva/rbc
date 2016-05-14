package behaviors;

import lejos.nxt.ColorSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.robotics.subsumption.Behavior;
import main.THBall;
import main.THBall.TurnSide;

public class TirarAzul implements Behavior {

	static GyroDirectionFinder gdf = THBall.gdf;
	static boolean suppressed = false;
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
		suppressed = false;
		ColorSensor.Color color = colorSensor.getColor();
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		return (inRange(20, 30, r) && inRange(30, 40, g) && inRange(45, 50, b));
	}

	@Override
	public void action() {
		THBall.atrasar(100);
		THBall.moverCatapulta(50);
		THBall.turnBy(30);
		THBall.bajarCatapulta();
		THBall.turnBy(330);
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
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void suppress() {
		suppressed = true;
		THBall.bajarCatapulta();
		THBall.stopMoving();
	}

}
