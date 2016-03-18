package com.marcciosilva.rbc;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.MotorPort;
import lejos.nxt.addon.LnrActrFirgelliNXT;
import lejos.robotics.LinearActuator;

public class LnrActTest {

	public static void main(String[] args) {

		LinearActuator actuator = new LnrActrFirgelliNXT(MotorPort.A);
		Button.LEFT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
				actuator.stop();
			}

			@Override
			public void buttonPressed(Button b) {
				actuator.moveTo(-200, true);
			}
		});

		Button.RIGHT.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				actuator.stop();
			}

			@Override
			public void buttonPressed(Button b) {
				actuator.moveTo(200, true);
			}
		});

		actuator.resetTachoCount();

		while (true) {
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				actuator.moveTo(0, false);
				break;
			}
		}
	}

}
