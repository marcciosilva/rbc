package behaviors;

import lejos.nxt.LCD;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Avoid implements Behavior {

	static UltrasonicSensor ultrasonicSensor = THBall.ultrasonicSensor;
	static NXTRegulatedMotor leftMotor = THBall.leftMotor;
	static NXTRegulatedMotor rightMotor = THBall.rightMotor;

	@Override
	public boolean takeControl() {
		return ultrasonicSensor.getDistance() <= 10;
	}

	@Override
	public void action() {
		THBall.setSpeed(THBall.SPEED_TURN);
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		while (ultrasonicSensor.getDistance() < 20) {
			leftMotor.backward();
			rightMotor.backward();
			Thread.yield();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				LCD.drawString(e.getMessage(), 0, 0);
			}
		}
		leftMotor.stop();
		rightMotor.stop();
		try {
			if (THBall.facingLeft) {
				turn(-180);
			} else {
				turn(180);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void suppress() {
	}

	public static void turn(int angle) throws Exception {
		if (angle < 0) {
			rightMotor.rotate(Math.abs(angle), false);
		} else {
			leftMotor.rotate(angle, false);
		}
	}

}
