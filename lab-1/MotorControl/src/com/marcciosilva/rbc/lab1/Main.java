package com.marcciosilva.rbc.lab1;

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Main {

	public static void main(String[] args) {

		new Thread() {
			boolean fin = false;

			@Override
			public synchronized void run() {
				while (!fin) {
					lejos.nxt.LCD.clear();
					lejos.nxt.LCD.drawString(Integer.toString(Motor.B.getTachoCount()), 0, 0);
					if ((lejos.nxt.Button.readButtons() & lejos.nxt.Button.ID_ESCAPE) == lejos.nxt.Button.ID_ESCAPE) {
						fin = true;
					}
					lejos.util.Delay.msDelay(50);
				}
			}

		}.start();

		while (true) {
			int buttons = Button.readButtons();
			if ((buttons & Button.ID_RIGHT) == Button.ID_RIGHT) {
				Motor.B.rotate(1);
				// lejos.nxt.LCD.clear();
				// lejos.nxt.LCD.drawString(Integer.toString(Motor.B.getTachoCount()),
				// 0, 0);
			} else if ((buttons & Button.ID_LEFT) == Button.ID_LEFT) {
				Motor.B.rotate(-1);
				// lejos.nxt.LCD.clear();
				// lejos.nxt.LCD.drawString(Integer.toString(Motor.B.getTachoCount()),
				// 0, 0);
			}
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				break;
			}
		}

	}

}
