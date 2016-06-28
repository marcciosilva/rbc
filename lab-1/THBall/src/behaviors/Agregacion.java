package behaviors;

import lejos.robotics.subsumption.Behavior;
import main.THBall;
import sensors.SensorSharp;
import utils.Movilidad;
import utils.Suppressable;
import utils.Utils;

public class Agregacion extends Suppressable implements Behavior {

	SensorSharp sensorLargaDistancia;
	SensorSharp sensorMediaDistancia;
	SensorSharp sensorMediaDistanciaMuerta;

	public Agregacion(SensorSharp sld, SensorSharp smd, SensorSharp smdm) {
		sensorLargaDistancia = sld;
		sensorMediaDistancia = smd;
		sensorMediaDistanciaMuerta = smdm;
		setSuppressed(false);
	}

	@Override
	public boolean takeControl() {
		return Utils.hayRobot(600, 
				sensorLargaDistancia.getMeasurement(false), 
				sensorMediaDistancia.getMeasurement(false), 
				sensorMediaDistanciaMuerta.getMeasurement(false));
	}

	@Override
	public void action() {
		setSuppressed(false);
		THBall.timer = System.currentTimeMillis();
		Movilidad.avanzar();
	}

	@Override
	public void suppress() {
		setSuppressed(true);
		Movilidad.stopMoving();

	}

}
