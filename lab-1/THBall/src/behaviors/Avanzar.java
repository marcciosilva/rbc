package behaviors;

import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Avanzar implements Behavior {

	boolean stop = false;

	public boolean takeControl() {
		return true;
	}

	public void action() {
		stop = false;
		while (!stop)
			THBall.travelFor(100);
	}

	public void suppress() {
		stop = true;
		THBall.stopMoving();
	}

	// @Override
	// public void action() {
	// THBall.setSpeed(THBall.SPEED_DRIVE);
	// NXTRegulatedMotor leftMotor = THBall.leftMotor;
	// NXTRegulatedMotor rightMotor = THBall.rightMotor;
	// leftMotor.forward();
	// rightMotor.forward();
	// }

	// @Override
	// public void suppress() {
	// THBall.stopMotors();
	// }

}
