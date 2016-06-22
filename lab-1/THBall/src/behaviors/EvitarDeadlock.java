package behaviors;

import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import main.THBall;
import sensors.SensorGDF;
import utils.Movilidad;
import utils.Suppressable;

public class EvitarDeadlock extends Suppressable implements Behavior {

	SensorGDF gdf;

	public EvitarDeadlock(SensorGDF gdf) {
		this.gdf = gdf;
		setSuppressed(false);
	}

	@Override
	public boolean takeControl() {
		return System.currentTimeMillis() - THBall.timer >= 20000;
	}

	@Override
	public void action() {
		RConsole.println("Ejecutando Evitar Deadlock");
		setSuppressed(false);
		Movilidad.stopMoving();
		Movilidad.atrasar((int) (Math.random() * 1750 + 250));
		Movilidad.turnBy(gdf, 90.0f, this);
		THBall.timer = System.currentTimeMillis();
		setSuppressed(false);
	}

	@Override
	public void suppress() {
		setSuppressed(true);
		Movilidad.stopMoving();
	}

}
