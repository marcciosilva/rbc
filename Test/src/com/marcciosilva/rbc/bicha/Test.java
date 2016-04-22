package com.marcciosilva.rbc.bicha;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

public class Test {

	public static void main(String[] args) {

		int anguloObjetivo = -60;

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
		boolean plegado = true;
		Motor.B.resetTachoCount();
		while (true) {
			int buttons = Button.readButtons();
			if ((buttons & Button.ID_RIGHT) == Button.ID_RIGHT) {
				anguloObjetivo++;
				// lejos.nxt.LCD.clear();
				// lejos.nxt.LCD.drawString(Integer.toString(Motor.B.getTachoCount()),
				// 0, 0);
			} else if ((buttons & Button.ID_LEFT) == Button.ID_LEFT) {
				anguloObjetivo--;
				// lejos.nxt.LCD.clear();
				// lejos.nxt.LCD.drawString(Integer.toString(Motor.B.getTachoCount()),
				// 0, 0);
			} else if ((buttons & Button.ID_ENTER) == Button.ID_ENTER) {
				if (plegado) {
					Motor.B.setSpeed(Motor.B.getMaxSpeed());
					Motor.B.rotateTo(anguloObjetivo);
					plegado = false;
				} else {
					Motor.B.setSpeed(Motor.B.getMaxSpeed() / 2.0f);
					Motor.B.rotateTo(0);
					plegado = true;
				}
			}
			lejos.util.Delay.msDelay(50);
			LCD.clear(1);
			LCD.drawString(Integer.toString(anguloObjetivo), 0, 1);
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				break;
			}
		}

	}

}
