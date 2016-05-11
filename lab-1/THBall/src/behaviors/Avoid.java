package behaviors;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import main.THBall;

public class Avoid implements Behavior {

	// static OpticalDistanceSensor largaDistancia = THBall.largaDistancia;
	// static OpticalDistanceSensor cortaDistancia = THBall.cortaDistancia;
	static GyroDirectionFinder gdf = THBall.gdf;
	static TouchSensor leftTouchSensor = THBall.leftTouchSensor;
	static TouchSensor rightTouchSensor = THBall.rightTouchSensor;
	static NXTRegulatedMotor leftMotor = THBall.leftMotor;
	static NXTRegulatedMotor rightMotor = THBall.rightMotor;
	static NXTRegulatedMotor catapulta = THBall.catapulta;

	@Override
	public boolean takeControl() {
		return leftTouchSensor.isPressed() && rightTouchSensor.isPressed();
	}

	@Override
	public void action() {
		Delay.msDelay(200);
		THBall.stopMoving();
		THBall.resetGyro();
		THBall.setSpeed(THBall.SPEED_TURN);
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
		THBall.turnBy(90.0f);
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
