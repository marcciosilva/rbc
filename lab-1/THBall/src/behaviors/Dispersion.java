package behaviors;

import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;
import main.THBall;
import utils.SensorSharp;

public class Dispersion implements Behavior {

	SensorSharp sensorLargaDistancia;
	SensorSharp sensorMediaDistancia;
	SensorSharp sensorMediaDistanciaMuerta;
	static boolean suppressed = false;

	public Dispersion(SensorSharp sld, SensorSharp smd, SensorSharp smdm) {
		sensorLargaDistancia = sld;
		sensorMediaDistancia = smd;
		sensorMediaDistanciaMuerta = smdm;
	}

	@Override
	public boolean takeControl() {
		int medidaCorta = sensorMediaDistancia.getMeasurement(true);
		int medidaLarga = sensorLargaDistancia.getMeasurement(true);
		int diferencia = Math.abs(medidaCorta - medidaLarga);
		if (medidaCorta <= 300 && THBall.hayRobot(diferencia)) {
			RConsole.println("Dispersion");
		}
		RConsole.println("###############");
		return false;
	}

	@Override
	public void action() {
		RConsole.println("Dispersion");
		THBall.atrasar();
	}

	@Override
	public void suppress() {
		suppressed = true;
		THBall.stopMoving();
	}

}
