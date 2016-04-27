package behaviors;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.remote.RemoteMotor;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Avoid implements Behavior {

	static UltrasonicSensor ultrasonicSensor = THBall.ultrasonicSensor;
	static NXTRegulatedMotor leftMotor = THBall.leftMotor;
	static NXTRegulatedMotor rightMotor = THBall.rightMotor;
	static RemoteMotor pinzaDer = THBall.pinzaDer;
	static RemoteMotor pinzaIzq = THBall.pinzaIzq;
	static RemoteMotor catapulta = THBall.catapulta;

	@Override
	public boolean takeControl() {
		return ultrasonicSensor.getDistance() <= 21;
	}

	@Override
	public void action() {
		THBall.setSpeed(THBall.SPEED_TURN);
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		// retrocede hasta estar a por lo menos 20 cm
		// de algun obstaculo
		// while (ultrasonicSensor.getDistance() < 25) {
		// leftMotor.backward();
		// rightMotor.backward();
		// Thread.yield();
		// THBall.sleep(50);
		// }
		leftMotor.stop();
		rightMotor.stop();
		THBall.bajarCatapulta();
		THBall.atraparPelotas();
		// THBall.girarPinzas(0);
		try {
			THBall.turn(180);
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (rightMotor.isMoving() || leftMotor.isMoving()) {
			Thread.yield();
			THBall.sleep(50);
		}
	}

	@Override
	public void suppress() {
		THBall.stopMotors();
	}

}
