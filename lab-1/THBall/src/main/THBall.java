package main;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;
import lejos.nxt.remote.RemoteNXT;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import sensors.SensorColor;
import sensors.SensorGDF;
import sensors.SensorSharp;
import utils.Movilidad;
import behaviors.Agregacion;
import behaviors.Avanzar;
import behaviors.Avoid;
import behaviors.Corregir;
import behaviors.Dispersion;
import behaviors.EvitarDeadlock;
import behaviors.TirarAzul;
import behaviors.TirarNaranja;

public class THBall {
	public static long timer;
	private Arbitrator arbitrator;

	// sensores
	public static RemoteNXT remoteNxt;
	private SensorSharp sensorLargaDistancia;
	private SensorSharp sensorMediaDistancia;
	private SensorSharp sensorMediaDistanciaMuerta;
	private SensorGDF gdf;
	private SensorColor colorSensor;
	public static TouchSensor leftTouchSensor = new TouchSensor(SensorPort.S2);
	public static TouchSensor rightTouchSensor = new TouchSensor(SensorPort.S4);

	public static void main(String[] args) {

		LCD.drawString("Presione algun boton", 0, 0);
		Button.waitForAnyPress();
		THBall tb = new THBall();
		tb.inicializar();
		tb.setupBehaviors();
		boolean exitMenu = false;
		LCD.clear();
		int buttonPressed = 0;
		while (!exitMenu) {
			buttonPressed = Button.readButtons();
			if ((buttonPressed & Button.ID_ESCAPE) != Button.ID_ESCAPE) {
				LCD.clear();
				timer = System.currentTimeMillis();
				tb.arbitrator.start();
				exitMenu = true;
			} else
				exitMenu = true;
		}
	}

	/**
	 * Inicializa comportamientos y arbitrator
	 */
	private void setupBehaviors() {
		Behavior avanzar = new Avanzar();
		Behavior avoid = new Avoid(gdf);
		Behavior corregir = new Corregir();
		Behavior tirarAzul = new TirarAzul(colorSensor);
		Behavior tirarNaranja = new TirarNaranja(colorSensor, gdf);
		Behavior dispersion = new Dispersion(sensorLargaDistancia, sensorMediaDistancia,
				sensorMediaDistanciaMuerta);
		Behavior agregacion = new Agregacion(sensorLargaDistancia, sensorMediaDistancia,
				sensorMediaDistanciaMuerta);
		Behavior evitarDeadlock = new EvitarDeadlock(gdf);

		// pongo behaviors en orden de prioridad
		// a mayor indice mayor prioridad
		Behavior behaviors[] = { avanzar, corregir, avoid, tirarNaranja, tirarAzul,
				dispersion, agregacion, evitarDeadlock };
		// DistanceTest distanceTest = new DistanceTest(sensorLargaDistancia,
		// sensorMediaDistancia, sensorMediaDistanciaMuerta);
		// Behavior behaviors[] = { dispersion, agregacion };
		// declaro arbitrator
		arbitrator = new Arbitrator(behaviors);
	}

	/**
	 * Inicializa sensores remotos y threads de polling a sensores de distancia
	 */
	public void inicializar() {
		// init sensores remotos
		try {
			remoteNxt = new RemoteNXT("rbc4_2", Bluetooth.getConnector());
			sensorLargaDistancia = new SensorSharp(remoteNxt.S2);
			sensorMediaDistancia = new SensorSharp(remoteNxt.S1);
			sensorMediaDistanciaMuerta = new SensorSharp(remoteNxt.S3);
			// inicializacion de actuador lineal
			// remoteNxt.C.resetTachoCount();
			// // hack
			// remoteNxt.C.rotate(1000);
			// remoteNxt.C.rotate(1000);
			RConsole.openAny(10000);
		} catch (Exception e) {
			LCD.clear();
			LCD.drawString(e.getClass().toString(), 0, 0);
			System.exit(1);
		}
		// init sensores locales y catapulta
		colorSensor = new SensorColor(SensorPort.S3);
		gdf = new SensorGDF(SensorPort.S1);
		Movilidad.resetearCatapulta();
		Movilidad.bajarCatapulta();

		// // listener para bajar catapulta
		// Button.ESCAPE.addButtonListener(new ButtonListener() {
		// @Override
		// public void buttonReleased(Button b) {
		// }
		//
		// @Override
		// public void buttonPressed(Button b) {
		// Movilidad.stopMoving();
		// try {
		// remoteNxt.C.rotate(-1000, false);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// try {
		// remoteNxt.C.rotate(-1000, false);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// }
		// });
	}

}