package com.marcciosilva.rbc;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassHTSensor;

public class Test {

	static CompassHTSensor compass;
	static float angle;
	static int UpdatePeriod = 200;

	public static void main(String[] args) throws Exception {
		compass = new CompassHTSensor(SensorPort.S1);

		while (true) {
			angle = compass.getDegrees();

			LCD.clear();
			LCD.drawString("angle = " + Integer.toString((int) angle), 0, 0);

			if (Button.readButtons() > 0 && ((Button.readButtons() & Button.ID_LEFT) == Button.ID_LEFT))
				break;

			Thread.sleep(UpdatePeriod);

		}
	}

}
