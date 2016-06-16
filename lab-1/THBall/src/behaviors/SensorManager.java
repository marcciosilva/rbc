package behaviors;

import lejos.nxt.addon.OpticalDistanceSensor;
import main.THBall;

public class SensorManager {

	private OpticalDistanceSensor largaDistancia;
	private OpticalDistanceSensor cortaDistancia;

	private int distanciaCorta;
	private int distanciaLarga;

	public SensorManager() {
		largaDistancia = THBall.largaDistancia;
		cortaDistancia = THBall.cortaDistancia;
	}

	public int getNewMeasureCorta() {
		return distanciaCorta = cortaDistancia.getDistance();

	}

	public int getOldMeasureCorta() {
		return distanciaCorta;
	}

	public int getNewMeasureLarga() {
		return distanciaLarga = largaDistancia.getDistance();
	}

	public int getOldMeasureLarga() {
		return distanciaLarga;
	}

}
