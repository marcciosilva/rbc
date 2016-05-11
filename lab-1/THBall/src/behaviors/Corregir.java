package behaviors;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Corregir implements Behavior {

	static GyroDirectionFinder gdf = THBall.gdf;
	static TouchSensor leftTouchSensor = THBall.leftTouchSensor;
	static TouchSensor rightTouchSensor = THBall.rightTouchSensor;
	static NXTRegulatedMotor leftMotor = THBall.leftMotor;
	static NXTRegulatedMotor rightMotor = THBall.rightMotor;

	@Override
	public boolean takeControl() {
		return (leftTouchSensor.isPressed() && !rightTouchSensor.isPressed())
				|| (!leftTouchSensor.isPressed() && rightTouchSensor.isPressed());
	}

	@Override
	public void action() {
		if (leftTouchSensor.isPressed() && !rightTouchSensor.isPressed()) {
			// desviado a la derecha
			THBall.setSpeed(THBall.SPEED_CORRECT);
			leftMotor.backward();
			rightMotor.forward();
		} else if (!leftTouchSensor.isPressed() && rightTouchSensor.isPressed()) {
			THBall.setSpeed(THBall.SPEED_CORRECT);
			rightMotor.backward();
			leftMotor.forward();
		}
	}

	@Override
	public void suppress() {
		THBall.stopMoving();
	}

}
