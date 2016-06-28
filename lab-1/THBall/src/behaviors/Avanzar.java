package behaviors;

import lejos.robotics.subsumption.Behavior;
import main.THBall;
import utils.Movilidad;
import utils.Suppressable;

public class Avanzar extends Suppressable implements Behavior {

	public boolean takeControl() {
		return true;
	}

	public void action() {
		setSuppressed(false);
		THBall.timer = System.currentTimeMillis();
		Movilidad.avanzar();
		while (!getSuppressed()) {
			Thread.yield();
		}
	}

	public void suppress() {
		setSuppressed(true);
		Movilidad.stopMoving();
	}

}
