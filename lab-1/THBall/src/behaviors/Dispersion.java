package behaviors;

import lejos.robotics.subsumption.Behavior;
import main.THBall;
import sensors.SensorSharp;
import utils.Movilidad;
import utils.Suppressable;
import utils.Utils;

public class Dispersion extends Suppressable implements Behavior {

	private SensorSharp sensorLargaDistancia;
	private SensorSharp sensorMediaDistancia;
	private SensorSharp sensorMediaDistanciaMuerta;

	public Dispersion(SensorSharp sld, SensorSharp smd, SensorSharp smdm) {
		sensorLargaDistancia = sld;
		sensorMediaDistancia = smd;
		sensorMediaDistanciaMuerta = smdm;
		setSuppressed(false);
	}

	@Override
	public boolean takeControl() {
		return Utils.hayRobot(300, 
				sensorLargaDistancia.getMeasurement(false), 
				sensorMediaDistancia.getMeasurement(false), 
				sensorMediaDistanciaMuerta.getMeasurement(false));
	}

	@Override
	public void action() {
		setSuppressed(false);
		THBall.timer = System.currentTimeMillis();
		Movilidad.atrasar();
	}

	@Override
	public void suppress() {
		setSuppressed(true);
		Movilidad.stopMoving();
	}

}
