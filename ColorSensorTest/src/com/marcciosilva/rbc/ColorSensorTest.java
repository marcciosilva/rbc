package com.marcciosilva.rbc;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.util.Delay;

public class ColorSensorTest {

	static int error = 20;

	public static boolean inRange(int min, int max, int value) {
		// true if value is in range of reference
		return ((value > min + error || value > min - error) && (value < max + error || value < max - error));
	}

	public static void main(String[] args) {
		final ColorSensor sensor = new ColorSensor(SensorPort.S1);
		while (true) {
			ColorSensor.Color color = sensor.getColor();
			LCD.clear();
			String strColor = "Color: ";
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();
			if (inRange(85, 115, r) && inRange(85, 120, g) && inRange(100, 145, b)) {
				strColor = "ORANGE";
			} else {
				switch (color.getColor()) {
				case 5:
					strColor = "ORANGE";
					break;
				case 2:
					strColor = "BLUE";
					break;
				default:
					break;
				}
			}
			LCD.drawString(strColor, 0, 0);
			LCD.drawString("R: " + Integer.toString(r), 0, 1);
			LCD.drawString("G: " + Integer.toString(g), 0, 2);
			LCD.drawString("B: " + Integer.toString(b), 0, 3);
			Delay.msDelay(300);
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				break;
			}
		}
	}

}
