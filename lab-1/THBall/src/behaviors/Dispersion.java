package behaviors;

import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
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
		int medidaCorta = sensorMediaDistancia.getMeasurement(true);
		int medidaLarga = sensorLargaDistancia.getMeasurement(true);
		int diferencia = Math.abs(medidaCorta - medidaLarga);
		if (medidaCorta <= 300 && Utils.hayRobot(diferencia)) {
			RConsole.println("Dispersion");
		}
		RConsole.println("###############");
		return false;
	}

	@Override
	public void action() {
		RConsole.println("Dispersion");
		Movilidad.atrasar();
	}

	@Override
	public void suppress() {
		setSuppressed(true);
		Movilidad.stopMoving();
	}

}
