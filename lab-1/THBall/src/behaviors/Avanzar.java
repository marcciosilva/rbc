package behaviors;

import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Avanzar implements Behavior {

	boolean suppressed = false;

	public boolean takeControl() {
		return true;
	}

	public void action() {
		suppressed = false;
		THBall.timer = System.currentTimeMillis();
		THBall.avanzar();
		while (!suppressed) {
			// Delay.msDelay(1);
			// THBall.timer++;
			RConsole.println("Ejecutando Avanzar");
			Thread.yield();
		}
	}

	public void suppress() {
		suppressed = true;
		THBall.stopMoving();
	}

}
