package com.marcciosilva.rbc;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class Program5 {

	public static void main(String[] args) {
		RConsole.open();
		LCD.clear();
		LCD.drawString("Program5", 0, 0);

		Button.waitForAnyPress();
		LCD.clear();
		Motor.A.setSpeed(720);
		Motor.B.setSpeed(720);
		Motor.C.setSpeed(720);
		String display = "";
		int i = 0;
		while (i < 8) {
			Motor.A.rotate(2 * 360, true);
			Motor.B.rotate(2 * 360, true);
			Motor.C.rotate(2 * 360, true);
			while (Motor.A.isMoving() || Motor.B.isMoving() || Motor.C.isMoving()) {
				Delay.msDelay(200);
				display = Integer.toString(Motor.A.getTachoCount()) + " " + Integer.toString(Motor.B.getTachoCount())
						+ " " + Integer.toString(Motor.A.getTachoCount()) + " ";
				LCD.drawString(display, 0, i);
			}
			RConsole.print(display + "\n");
			i++;
		}
		Button.waitForAnyPress();

	}

}
