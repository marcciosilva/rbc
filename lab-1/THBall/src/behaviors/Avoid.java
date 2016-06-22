package behaviors;

import lejos.nxt.TouchSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import main.THBall;
import sensors.SensorGDF;
import utils.Movilidad;
import utils.Suppressable;

public class Avoid extends Suppressable implements Behavior {

	private SensorGDF gdf;
	static TouchSensor leftTouchSensor = THBall.leftTouchSensor;
	static TouchSensor rightTouchSensor = THBall.rightTouchSensor;

	public Avoid(SensorGDF gdf) {
		this.gdf = gdf;
		setSuppressed(false);
	}

	@Override
	public boolean takeControl() {
		return leftTouchSensor.isPressed() && rightTouchSensor.isPressed();
	}

	@Override
	public void action() {
		RConsole.println("Ejecutando Avoid");
		THBall.timer = System.currentTimeMillis();
		setSuppressed(false);
		Movilidad.stopMoving();
		gdf.resetHeading();
		// retrocede hasta estar a por lo menos 20 cm
		// de algun obstaculo
		Movilidad.atrasar((int) (Math.random() * 1750 + 250));
		Movilidad.turnBy(gdf, 90.0f, this);
	}

	@Override
	public void suppress() {
		setSuppressed(true);
		Movilidad.stopMoving();
	}

}
