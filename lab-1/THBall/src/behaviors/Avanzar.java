package behaviors;

import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import main.THBall;

public class Avanzar implements Behavior {

	boolean suppressed = false;

	public boolean takeControl() {
		return true;
	}

	public void action() {
		suppressed = false;
		THBall.timer = 0;
		THBall.avanzar();
		while (!suppressed) {
			Delay.msDelay(1);
			THBall.timer++;
			Thread.yield();
		}
	}

	public void suppress() {
		suppressed = true;
		THBall.stopMoving();
	}

}
