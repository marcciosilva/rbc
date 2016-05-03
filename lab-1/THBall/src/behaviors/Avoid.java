package behaviors;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Avoid implements Behavior {

	static OpticalDistanceSensor largaDistancia = THBall.largaDistancia;
	static OpticalDistanceSensor cortaDistancia = THBall.cortaDistancia;
	static TouchSensor touchSensor = THBall.touchSensor;
	static NXTRegulatedMotor leftMotor = THBall.leftMotor;
	static NXTRegulatedMotor rightMotor = THBall.rightMotor;
	static NXTRegulatedMotor catapulta = THBall.catapulta;

	@Override
	public boolean takeControl() {
		return touchSensor.isPressed();
	}

	@Override
	public void action() {
		THBall.setSpeed(THBall.SPEED_TURN);
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		// retrocede hasta estar a por lo menos 20 cm
		// de algun obstaculo
		while (cortaDistancia.getDistance() < 100) {
			leftMotor.backward();
			rightMotor.backward();
			// Thread.yield();
			// THBall.sleep(50);
		}
		leftMotor.stop();
		rightMotor.stop();
		THBall.girarRandom();
		// while (rightMotor.isMoving() || leftMotor.isMoving()) {
		// Thread.yield();
		// THBall.sleep(50);
		// }
	}

	@Override
	public void suppress() {
		THBall.stopMoving();
	}

}
