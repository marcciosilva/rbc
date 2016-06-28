package behaviors;

import lejos.nxt.comm.RConsole;
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
	int medidaMedia;
	int medidaLarga;
	int medidaMediaMuerta;
	int diferenciaSensoresMedia;

	public Agregacion(SensorSharp sld, SensorSharp smd, SensorSharp smdm) {
		sensorLargaDistancia = sld;
		sensorMediaDistancia = smd;
		sensorMediaDistanciaMuerta = smdm;
		setSuppressed(false);
	}

	@Override
	public boolean takeControl() {
		medidaMedia = sensorMediaDistancia.getMeasurement(true);
		medidaLarga = sensorLargaDistancia.getMeasurement(true);
		medidaMediaMuerta = sensorMediaDistanciaMuerta.getMeasurement(true);
		diferenciaSensoresMedia = Math.abs(medidaMedia - medidaMediaMuerta);
		return ((Utils.inRange(medidaMedia, 600, 20) || Utils.inRange(medidaMediaMuerta,
				600, 20)) // rango de agregacion
				&& diferenciaSensoresMedia < 120 // no zona muerta
				&& (!Utils.inRange(medidaLarga, medidaMedia, 100) || !Utils.inRange(
						medidaLarga, medidaMediaMuerta, 100)) // no pared
		&& (medidaMedia < 800) // distancia maxima sin que explote - prueba 3
		);
	}

	@Override
	public void action() {
		RConsole.println("###############");
		RConsole.println("Agregacion");
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
		Movilidad.avanzar();
		// while (!getSuppressed()) {
		// Thread.yield();
		// }
	}

	@Override
	public void suppress() {
		setSuppressed(true);
		Movilidad.stopMoving();

	}

}
