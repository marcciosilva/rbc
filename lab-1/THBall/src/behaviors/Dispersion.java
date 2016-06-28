package behaviors;

import lejos.nxt.comm.RConsole;
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
	int medidaMedia;
	int medidaLarga;
	int medidaMediaMuerta;
	int diferenciaSensoresMedia;
	int diferenciaLargaMedia; //NUEVO

	public Dispersion(SensorSharp sld, SensorSharp smd, SensorSharp smdm) {
		sensorLargaDistancia = sld;
		sensorMediaDistancia = smd;
		sensorMediaDistanciaMuerta = smdm;
		setSuppressed(false);
	}

	@Override
	public boolean takeControl() {
		medidaMedia = sensorMediaDistancia.getMeasurement(false);
		medidaLarga = sensorLargaDistancia.getMeasurement(false);
		medidaMediaMuerta = sensorMediaDistanciaMuerta.getMeasurement(false);
		return Utils.hayRobot(300, medidaLarga, medidaMedia, medidaMediaMuerta);
	}

	@Override
	public void action() {
		RConsole.println("###############");
		RConsole.println("Dispersion");
		RConsole.println("Media = " + Integer.toString(medidaMedia));
		RConsole.println("Larga = " + Integer.toString(medidaLarga));
		RConsole.println("Media Muerta = " + Integer.toString(medidaMediaMuerta));
		RConsole.println("Diferencia Larga y Media = "
				+ Integer.toString(Math.abs(medidaMedia - medidaLarga)));
		RConsole.println("Diferencia Media y Media Muerta = "
				+ Integer.toString(Math.abs(medidaMedia - medidaMediaMuerta)));
		RConsole.println("###############");
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
