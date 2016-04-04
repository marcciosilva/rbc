package com.marcciosilva.rbc;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Test1 {

	public static void main(String[] args) {
		LCD.drawInt(1, 0, 0);
		LCD.drawInt(2, 1, 0);
		LCD.drawInt(3, 2, 0);
		Button.waitForAnyPress();
	}

}
