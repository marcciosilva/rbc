package behaviors;

import lejos.nxt.ColorSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import main.THBall;

public class TirarAzul implements Behavior {

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
		return (inRange(20, 30, r) && inRange(30, 40, g) && inRange(45, 50, b));
	}

	@Override
	public void action() {
		THBall.stopMotors();
		leftMotor.backward();
		rightMotor.backward();
		Delay.msDelay(100);
		leftMotor.stop();
		rightMotor.stop();
		THBall.moverCatapulta(60);
		THBall.turn(30);
		THBall.bajarCatapulta();
	}

	@Override
	public void suppress() {
		THBall.bajarCatapulta();
		THBall.stopMotors();
	}

}
