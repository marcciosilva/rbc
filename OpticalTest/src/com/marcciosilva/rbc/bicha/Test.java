package com.marcciosilva.rbc.bicha;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Queue;

import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;
import lejos.nxt.remote.RemoteNXT;

public class Test {

	public static RemoteNXT remoteNxt;
	public static OpticalDistanceSensor largaDistancia;
	// OpticalDistanceSensor(SensorPort.S1);
	public static OpticalDistanceSensor cortaDistancia;
	public static Queue<Integer> largaDistanciaQueue = new Queue<Integer>();
	public static Queue<Integer> cortaDistanciaQueue = new Queue<Integer>();
	// promedios
	// public static final Lock lockLargaDistanciaPromedio = new
	// ReentrantLock();
	public static int largaDistanciaPromedio = 0;
	// public static final Lock lockCortaDistanciaPromedio = new
	// ReentrantLock();
	public static int cortaDistanciaPromedio = 0;

	public static void main(String[] args) {
		try {
			remoteNxt = new RemoteNXT("rbc4_2", Bluetooth.getConnector());
		} catch (IOException e) {
			RConsole.println("puto");
		}
		cortaDistancia = new OpticalDistanceSensor(remoteNxt.S2);
		largaDistancia = new OpticalDistanceSensor(remoteNxt.S1);
		RConsole.openAny(300000);

		(new Thread() {
			float promedioLocal = 0.0f;
			int cantMediciones = 5;

			@Override
			public void run() {
				while (true) {
					try {
						if (cortaDistanciaQueue.size() == cantMediciones)
							// saco un elemento
							Test.cortaDistanciaQueue.pop();
						// agrego una medida de uno de los sharps
						Test.cortaDistanciaQueue.push(cortaDistancia.getDistance());
						// calculo nuevo promedio
						promedioLocal = 0.0f;
						Enumeration<Integer> elements = Test.cortaDistanciaQueue
								.elements();
						while (elements.hasMoreElements()) {
							promedioLocal += elements.nextElement();
						}
						promedioLocal /= (float) Test.cortaDistanciaQueue.size();
						cortaDistanciaPromedio = (int) promedioLocal;
						// Thread.sleep(10);
					} catch (Exception e) {
						RConsole.println(e.getMessage());
					}
				}
			}
		}).start();

		(new Thread() {
			float promedioLocal = 0.0f;
			int cantMediciones = 5;

			@Override
			public void run() {
				while (true) {
					try {
						if (largaDistanciaQueue.size() == cantMediciones)
							// saco un elemento
							largaDistanciaQueue.pop();
						// agrego una medida de uno de los sharps
						largaDistanciaQueue.push(largaDistancia.getDistance());
						// calculo nuevo promedio
						promedioLocal = 0.0f;
						Enumeration<Integer> elements = largaDistanciaQueue.elements();
						while (elements.hasMoreElements()) {
							promedioLocal += elements.nextElement();
						}
						promedioLocal /= (float) largaDistanciaQueue.size();
						largaDistanciaPromedio = (int) promedioLocal;
						// Thread.sleep(10);
					} catch (Exception e) {
						RConsole.println(e.getMessage());
					}
				}
			}
		}).start();

		while (true) {
			RConsole.println("Corta = " + Integer.toString(cortaDistanciaPromedio));
			RConsole.println("Larga = " + Integer.toString(largaDistanciaPromedio));
			RConsole.println("#############################################");
		}
	}

}
