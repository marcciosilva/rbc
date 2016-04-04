package com.marcciosilva.rbc;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;

public class Program2 {

	public static void main(String[] args) {
		LCD.drawString("Program 2", 0, 0);
		Motor.A.setSpeed(2 * 360);
		Motor.A.forward();
		Delay.msDelay(2000);
		LCD.drawString(Integer.toString(Motor.A.getTachoCount()), 0, 1);
		Motor.A.stop();
		LCD.drawString(Integer.toString(Motor.A.getTachoCount()), 0, 2);
		Motor.A.backward();
		while (Motor.A.getTachoCount() != 0)
			;
		LCD.drawString(Integer.toString(Motor.A.getTachoCount()), 0, 3);
		Motor.A.stop();
		LCD.drawString(Integer.toString(Motor.A.getTachoCount()), 0, 4);
		Button.waitForAnyPress();
	}

}
