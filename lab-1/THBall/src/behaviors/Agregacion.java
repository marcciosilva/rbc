package behaviors;

import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
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
		Delay.msDelay(1000);
		int medidaCorta = sensorMediaDistancia.getMeasurement(false);
		int medidaLarga = sensorLargaDistancia.getMeasurement(false);
		int diferencia = Math.abs(medidaCorta - medidaLarga);
		RConsole.println("Corta = " + Integer.toString(medidaCorta));
		RConsole.println("Larga = " + Integer.toString(medidaLarga));
		RConsole.println("Diferencia = " + Integer.toString(diferencia));
		if (medidaCorta > 600 && Utils.hayRobot(diferencia)) {
			RConsole.println("Agregacion");
		}
		return false;
	}

	@Override
	public void action() {
		RConsole.println("Agregacion");
		Movilidad.avanzar();
	}

	@Override
	public void suppress() {
		setSuppressed(true);
		Movilidad.stopMoving();

	}

}
