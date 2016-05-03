package com.marcciosilva.rbc;

import java.text.DecimalFormat;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;

public class GyroTest {

	public static void main(String[] args) {
		LCD.drawString("Pulse algun boton", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		GyroSensor gyro = new GyroSensor(SensorPort.S4);
		GyroDirectionFinder gdf = new GyroDirectionFinder(gyro, true);
		// imprime angulo y capta boton de escape
		DecimalFormat df = new DecimalFormat("#.00");
		while (true) {
			LCD.clear();
			LCD.drawString(df.format(gdf.getDegrees()), 0, 0);
			if ((Button.readButtons() & lejos.nxt.Button.ID_ESCAPE) == lejos.nxt.Button.ID_ESCAPE) {
				break;
			}
			lejos.util.Delay.msDelay(100);
		}

	}

}
