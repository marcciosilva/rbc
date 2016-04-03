package behaviors;

import lejos.robotics.subsumption.Behavior;
import robot.Robot;

public class Wander implements Behavior {
	private Robot r;
	private double transVel = 0.5f;

	public Wander(Robot r1) {
		this.r = r1;
	}

	public boolean takeControl() {
		return true;
	}

	public void suppress() {
	}

	public void action() {
		r.setTranslationalVelocity(transVel);
		if ((r.getCounter() % 100) == 0)
			r.setRotationalVelocity(Math.PI / 2 * (0.5 - Math.random()));
		System.out.println("behavior:wander");
	}
}
