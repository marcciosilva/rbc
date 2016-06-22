package behaviors;

import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import sensors.SensorSharp;

public class DistanceTest implements Behavior {

	SensorSharp sensorLargaDistancia;
	SensorSharp sensorMediaDistancia;
	SensorSharp sensorMediaDistanciaMuerta;

	public DistanceTest(SensorSharp sld, SensorSharp smd, SensorSharp smdm) {
		sensorLargaDistancia = sld;
		sensorMediaDistancia = smd;
		sensorMediaDistanciaMuerta = smdm;
	}

	@Override
	public boolean takeControl() {
		Delay.msDelay(1000);
		int medidaMedia = sensorMediaDistancia.getMeasurement(true);
		int medidaLarga = sensorLargaDistancia.getMeasurement(true);
		int medidaMediaMuerta = sensorMediaDistanciaMuerta.getMeasurement(true);
		RConsole.println("Media = " + Integer.toString(medidaMedia));
		RConsole.println("Larga = " + Integer.toString(medidaLarga));
		RConsole.println("Media Muerta = " + Integer.toString(medidaMediaMuerta));
		RConsole.println("Diferencia Larga y Media = "
				+ Integer.toString(Math.abs(medidaMedia - medidaLarga)));
		RConsole.println("Diferencia Media y Media Muerta = "
				+ Integer.toString(Math.abs(medidaMedia - medidaMediaMuerta)));
		RConsole.println("#################");
		return false;
	}

	@Override
	public void action() {
	}

	@Override
	public void suppress() {
	}

}
