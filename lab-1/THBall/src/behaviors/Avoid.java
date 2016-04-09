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
		// retrocede hasta estar a por lo menos 20 cm
		// de algun obstaculo
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
			if (THBall.goingLeft) {
				THBall.turn(-180);
			} else {
				THBall.turn(180);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (rightMotor.isMoving() || leftMotor.isMoving()) {
			Thread.yield();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				LCD.drawString(e.getMessage(), 0, 0);
			}
		}
		THBall.facingLeft = !THBall.facingLeft;
		THBall.nextRow();
	}

	@Override
	public void suppress() {
		THBall.stopMotors();
	}

}
