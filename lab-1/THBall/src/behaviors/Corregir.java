package behaviors;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.comm.RConsole;
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
		try {
			RConsole.println("Ejecutando Corregir");
			THBall.timer = System.currentTimeMillis();
			if (leftTouchSensor.isPressed() && !rightTouchSensor.isPressed()) {
				// desviado a la derecha
				THBall.turnLeft(THBall.SPEED_CORRECT);
				leftMotor.backward();
				// leftMotor.stop();
				rightMotor.forward();
			} else if (!leftTouchSensor.isPressed() && rightTouchSensor.isPressed()) {
				THBall.turnRight(THBall.SPEED_CORRECT);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void suppress() {
		THBall.stopMoving();
	}

}
