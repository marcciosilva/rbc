package behaviors;

import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.Behavior;
import main.THBall;
import sensors.SensorColor;
import sensors.SensorGDF;
import utils.Movilidad;
import utils.Suppressable;
import utils.Utils;

public class TirarNaranja extends Suppressable implements Behavior {

	SensorColor colorSensor;
	static TouchSensor leftTouchSensor = THBall.leftTouchSensor;
	static TouchSensor rightTouchSensor = THBall.rightTouchSensor;
	static int error = 5;
	private SensorGDF gdf;

	public TirarNaranja(SensorColor cs, SensorGDF gdf) {
		colorSensor = cs;
		this.gdf = gdf;
		setSuppressed(false);
	}

	@Override
	public boolean takeControl() {
		// al no ser el primero en pedir una medida en un ciclo del arbitrator
		// no necesita pedir una nueva medida, usa la anterior
		return Utils.isColorOrange(colorSensor.getMeasurement(false));
	}

	@Override
	public void action() {
		setSuppressed(false);
		THBall.timer = System.currentTimeMillis();
		Movilidad.stopMoving();
		Movilidad.atrasar(350);
		Movilidad.turnTo(gdf, 90.0f, this);
		Movilidad.avanzar();
		while ((!leftTouchSensor.isPressed() && !rightTouchSensor.isPressed())
				&& !getSuppressed())
			;
		// si el comportamiento no fue suprimido por el evitar deadlock
		if (!getSuppressed()) {
			Movilidad.stopMoving();
			Movilidad.atrasar(250);
			Movilidad.turnTo(gdf, 270.0f, this);
			Movilidad.tirarPelota();
		}
	}

	@Override
	public void suppress() {
		setSuppressed(true);
		Movilidad.stopMoving();
	}

}
