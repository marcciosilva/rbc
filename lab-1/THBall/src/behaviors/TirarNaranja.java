package behaviors;

import lejos.nxt.ColorSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import main.THBall;

public class TirarNaranja implements Behavior {

	static ColorSensor colorSensor = THBall.colorSensor;
	static NXTRegulatedMotor catapulta = THBall.catapulta;
	static NXTRegulatedMotor leftMotor = THBall.leftMotor;
	static NXTRegulatedMotor rightMotor = THBall.rightMotor;
	static TouchSensor touchSensor = THBall.touchSensor;
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
		return touchSensor.isPressed() && (inRange(85, 115, r) && inRange(85, 120, g) && inRange(30, 50, b));
	}

	@Override
	public void action() {
		THBall.setSpeed(THBall.SPEED_TURN);
		// leftMotor.resetTachoCount();
		// rightMotor.resetTachoCount();
		// retrocede hasta estar a por lo menos 20 cm
		// de algun obstaculo
		// int distanciaRetroceso = (int)(Math.random() * 20 + 30);
		// while (cortaDistancia.getDistance() < distanciaRetroceso) {
		leftMotor.backward();
		rightMotor.backward();
		// Thread.yield();
		// THBall.sleep(50);
		// }
		int tiempoRetroceso = (int) (Math.random() * 3 + 2) * 1000;
		Delay.msDelay(tiempoRetroceso);
		THBall.stopMoving();
		THBall.turnBy(180.0f);
		THBall.tirarPelota();
	}

	@Override
	public void suppress() {
		THBall.stopMoving();
	}

}
