package com.marcciosilva.rbc;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.Delay;

public class Program4 {
	public static void main(String[] args) {
		LCD.drawString("Programa 4", 0, 0);
		Button.waitForAnyPress();
		Motor.A.rotate(4*360,true);
		Delay.msDelay(300);
		while(Motor.A.isMoving()){
			Delay.msDelay(200);
			LCD.clear(1);
			LCD.drawInt(Motor.A.getTachoCount(), 0, 1);
			if(Button.readButtons() > 0){
				Motor.A.stop();
			}
		}
		LCD.drawInt(Motor.A.getTachoCount(), 0, 2);
		Button.waitForAnyPress();
		
		
	}

}
