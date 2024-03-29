package behaviors;

import lejos.robotics.subsumption.Behavior;
import sensors.SensorColor;
import utils.Movilidad;
import utils.Suppressable;
import utils.Utils;

public class TirarAzul extends Suppressable implements Behavior {

	SensorColor colorSensor;

	public TirarAzul(SensorColor cs) {
		colorSensor = cs;
		setSuppressed(false);
	}

	@Override
	public boolean takeControl() {
		setSuppressed(false);
		return Utils.isColorBlue(colorSensor.getMeasurement(true));
	}

	@Override
	public void action() {
		Movilidad.atrasar(450);
		Movilidad.subirCatapulta();
		Movilidad.bajarCatapulta();
	}

	@Override
	public void suppress() {
		setSuppressed(true);
		Movilidad.bajarCatapulta();
		Movilidad.stopMoving();
	}

}
