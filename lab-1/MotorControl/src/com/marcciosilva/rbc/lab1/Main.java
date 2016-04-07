package com.marcciosilva.rbc.lab1;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Motor;

public class Main {

	public static void main(String[] args) {
		Button.LEFT.addButtonListener(new ButtonListener() {
			
			@Override
			public void buttonReleased(Button b) {
			}
			
			@Override
			public void buttonPressed(Button b) {
				Motor.B.rotate(1);
			}
		});
		
		Button.RIGHT.addButtonListener(new ButtonListener() {
			
			@Override
			public void buttonReleased(Button b) {
			}
			
			@Override
			public void buttonPressed(Button b) {
				Motor.B.rotate(-1);
			}
		});
		
		while (true) {
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE)
				break;
		}
		
	}

}
