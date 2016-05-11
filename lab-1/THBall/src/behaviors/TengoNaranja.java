package behaviors;

import lejos.nxt.ColorSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class TengoNaranja implements Behavior {

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
		return (inRange(85, 115, r) && inRange(85, 120, g) && inRange(30, 50, b));
	}

	@Override
	public void action() {
		// THBall.stopMoving();
		// leftMotor.backward();
		// rightMotor.backward();
		// Delay.msDelay(100);
		THBall.setSpeed(THBall.SPEED_TURN);
		THBall.turnTo(90.0f);
		THBall.avanzar();
		while (!touchSensor.isPressed())
			;
	}

	@Override
	public void suppress() {
		THBall.stopMoving();
	}

}
