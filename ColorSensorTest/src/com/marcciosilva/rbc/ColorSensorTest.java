package com.marcciosilva.rbc;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class ColorSensorTest {

	static int error = 20;

	public static boolean inRange(int min, int max, int value) {
		// true if value is in range of reference
		return ((value > min + error || value > min - error) && (value < max + error || value < max
				- error));
	}

	public static void main(String[] args) {
		final ColorSensor sensor = new ColorSensor(SensorPort.S3);
		RConsole.openAny(30000);
		while (true) {
			ColorSensor.Color color = sensor.getColor();
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();
			RConsole.println(Integer.toString(r) + ";" + Integer.toString(g) + ";"
					+ Integer.toString(b));
			Delay.msDelay(300);
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				break;
			}
		}
	}

}
