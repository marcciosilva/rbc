package behaviors;

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
		return ultrasonicSensor.getDistance() <= 10 || THBall.girando;
	}

	@Override
	public void action() {
		THBall.girando = true;
		THBall.setSpeed(THBall.SPEED_TURN);
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		while (ultrasonicSensor.getDistance() < 20) {
			leftMotor.backward();
			rightMotor.backward();
			// Thread.yield();
			// try {
			// Thread.sleep(50);
			// } catch (InterruptedException e) {
			// LCD.drawString(e.getMessage(), 0, 0);
			// }
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
		THBall.facingLeft = !THBall.facingLeft;
		THBall.girando = false;
	}

	@Override
	public void suppress() {
		THBall.leftMotor.stop();
		THBall.rightMotor.stop();
	}

	public static void turn(int angle) throws Exception {
		if (angle < 0) {
			rightMotor.rotate((int) Math.abs(angle * THBall.conversionAngles), false);
		} else {
			leftMotor.rotate((int) (angle * THBall.conversionAngles), false);
		}
	}

}
