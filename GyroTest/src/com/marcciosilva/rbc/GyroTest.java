package com.marcciosilva.rbc;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;
import lejos.util.Delay;

public class GyroTest {

	public static void main(String[] args) {
		LCD.drawString("Pulse algun boton", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		GyroSensor gyro = new GyroSensor(SensorPort.S4);
		// LCD.drawString("Calibrando", 0, 0);
		GyroDirectionFinder gdf = new GyroDirectionFinder(gyro, true);
		// imprime angulo y capta boton de escape
		// DecimalFormat df = new DecimalFormat("#.00");
		// LCD.clear();
		// LCD.drawString("Calibrado", 0, 0);
		// Button.waitForAnyPress();
		while (true) {
			LCD.clear();
			// LCD.drawString(df.format(gdf.getDegrees()), 0, 0);
			LCD.drawString(Float.toString(gdf.getDegrees()), 0, 0);
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				break;
			}
			Delay.msDelay(100);
		}

	}

}
