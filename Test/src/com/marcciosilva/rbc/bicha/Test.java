package com.marcciosilva.rbc.bicha;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;
import lejos.util.Delay;

public class Test {

	public static void main(String[] args) {

		LCD.drawString("Pulse algun boton", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		GyroSensor gyro = new GyroSensor(SensorPort.S4);
		// LCD.drawString("Calibrando", 0, 0);
		GyroDirectionFinder gdf = new GyroDirectionFinder(gyro, true);
		Delay.msDelay(10000);
		gdf.setDegreesCartesian(0.0f);
		// imprime angulo y capta boton de escape
		// DecimalFormat df = new DecimalFormat("#.00");
		// LCD.clear();
		// LCD.drawString("Calibrado", 0, 0);
		// Button.waitForAnyPress();
		while (true) {
			LCD.clear();
			// LCD.drawString(df.format(gdf.getDegrees()), 0, 0);
			LCD.drawString(Float.toString(gdf.getDegrees() % 360), 0, 0);
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				break;
			}
			Delay.msDelay(100);
		}

		// int anguloObjetivo = -60;
		//
		// new Thread() {
		// boolean fin = false;
		//
		// @Override
		// public synchronized void run() {
		// while (!fin) {
		// lejos.nxt.LCD.clear();
		// lejos.nxt.LCD.drawString(Integer.toString(Motor.B.getTachoCount()),
		// 0, 0);
		// if ((lejos.nxt.Button.readButtons() & lejos.nxt.Button.ID_ESCAPE) ==
		// lejos.nxt.Button.ID_ESCAPE) {
		// fin = true;
		// }
		// lejos.util.Delay.msDelay(50);
		// }
		// }
		//
		// }.start();
		// boolean plegado = true;
		// Motor.B.resetTachoCount();
		// while (true) {
		// int buttons = Button.readButtons();
		// if ((buttons & Button.ID_RIGHT) == Button.ID_RIGHT) {
		// anguloObjetivo++;
		// // lejos.nxt.LCD.clear();
		// //
		// lejos.nxt.LCD.drawString(Integer.toString(Motor.B.getTachoCount()),
		// // 0, 0);
		// } else if ((buttons & Button.ID_LEFT) == Button.ID_LEFT) {
		// anguloObjetivo--;
		// // lejos.nxt.LCD.clear();
		// //
		// lejos.nxt.LCD.drawString(Integer.toString(Motor.B.getTachoCount()),
		// // 0, 0);
		// } else if ((buttons & Button.ID_ENTER) == Button.ID_ENTER) {
		// if (plegado) {
		// Motor.B.setSpeed(Motor.B.getMaxSpeed());
		// Motor.B.rotateTo(anguloObjetivo);
		// plegado = false;
		// } else {
		// Motor.B.setSpeed(Motor.B.getMaxSpeed() / 2.0f);
		// Motor.B.rotateTo(0);
		// plegado = true;
		// }
		// }
		// lejos.util.Delay.msDelay(50);
		// LCD.clear(1);
		// LCD.drawString(Integer.toString(anguloObjetivo), 0, 1);
		// if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
		// break;
		// }
		// }

	}

}
