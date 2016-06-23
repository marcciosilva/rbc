import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.addon.LnrActrFirgelliNXT;
import lejos.robotics.LinearActuator;
import lejos.util.Delay;

public class LnrActTest {

	public static void main(String[] args) {

		// primera prueba (de control del actuador)
		final LinearActuator actuator = new LnrActrFirgelliNXT(MotorPort.C);
		actuator.resetTachoCount();
		// actuator.moveTo(27, false);
		LCD.clear();
		LCD.drawString("RIGHT extiende", 0, 0);
		LCD.drawString("LEFT retrae", 0, 1);
		LCD.drawString("ESCAPE sig test", 0, 2);
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
		while (true) {
			LCD.clear(3);
			LCD.drawString(Integer.toString(actuator.getTachoCount()), 0, 3);
			Delay.msDelay(200);
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				LCD.clear();
				actuator.moveTo(0, false);
				break;
			}
		}
		// segunda prueba
		// Button.LEFT.addButtonListener(null);
		// Button.RIGHT.addButtonListener(null);
		// // 20 ticks equivalen a 10 mm, 1 cm
		// actuator.moveTo(20, false);
		// LCD.drawString(Integer.toString(actuator.getTachoCount()), 0, 0);
		// actuator.moveTo(0, false);
		// actuator.moveTo(40, false);
		// LCD.drawString(Integer.toString(actuator.getTachoCount()), 0, 1);
		// actuator.moveTo(0, false);
		// actuator.moveTo(60, false);
		// LCD.drawString(Integer.toString(actuator.getTachoCount()), 0, 3);
		// actuator.moveTo(0, false);
		// Button.waitForAnyPress();

	}

}
