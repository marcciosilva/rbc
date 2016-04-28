package behaviors;

import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Avanzar implements Behavior {

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		THBall.setSpeed(THBall.SPEED_DRIVE);
		NXTRegulatedMotor leftMotor = THBall.leftMotor;
		NXTRegulatedMotor rightMotor = THBall.rightMotor;
		leftMotor.forward();
		rightMotor.forward();
	}

	@Override
	public void suppress() {
		THBall.stopMotors();
	}

}
