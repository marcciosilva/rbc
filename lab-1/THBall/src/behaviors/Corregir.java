package behaviors;

import lejos.nxt.TouchSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import main.THBall;
import utils.Constants;
import utils.Movilidad;

public class Corregir implements Behavior {

	static TouchSensor leftTouchSensor = THBall.leftTouchSensor;
	static TouchSensor rightTouchSensor = THBall.rightTouchSensor;

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
				Movilidad.turnLeft(Constants.SPEED_CORRECT);
			} else if (!leftTouchSensor.isPressed() && rightTouchSensor.isPressed()) {
				Movilidad.turnRight(Constants.SPEED_CORRECT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void suppress() {
		Movilidad.stopMoving();
	}

}
