package com.marcciosilva.rbc;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

public class Program3 {

	public static void main(String[] args) {
		LCD.drawString("Program 3", 0, 0);
		Button.waitForAnyPress();
		Motor.A.rotate(4*360);
		LCD.drawInt(Motor.A.getTachoCount(), 0, 1);
		Motor.A.rotateTo(0);
		LCD.drawInt(Motor.A.getTachoCount(), 0, 2);
		Button.waitForAnyPress();
	}

}
