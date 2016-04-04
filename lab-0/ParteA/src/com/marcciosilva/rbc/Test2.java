package com.marcciosilva.rbc;

import lejos.nxt.Motor;
import lejos.util.Delay;

public class Test2 {

	public static void main(String[] args) {
		Motor.A.forward();
		Delay.msDelay(2000);
		Motor.A.stop();
	}

}
