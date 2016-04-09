package behaviors;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class AvoidDeathzone implements Behavior {

	static UltrasonicSensor ultrasonicSensor = THBall.ultrasonicSensor;

	@Override
	public boolean takeControl() {
		return ultrasonicSensor.getDistance() <= 10 && THBall.nextToDeathZone();
	}

	@Override
	public void action() {
		THBall.goingLeft = !THBall.goingLeft;
		THBall.resetCurrentRow();
	}

	@Override
	public void suppress() {
	}

}
