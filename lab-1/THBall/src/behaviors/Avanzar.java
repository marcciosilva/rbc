package behaviors;

import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Avanzar implements Behavior {

	boolean stop = false;

	public boolean takeControl() {
		return true;
	}

	public void action() {
		THBall.avanzar();
	}

	public void suppress() {
		THBall.stopMoving();
	}

}
