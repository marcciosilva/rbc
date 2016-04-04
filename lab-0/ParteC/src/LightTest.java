import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;
import lejos.util.Delay;

public class LightTest {

	public static void main(String[] args) {
		final ColorSensor sensor = new ColorSensor(SensorPort.S1);
		ButtonListener listener = new ButtonListener() {
			boolean floodlight = true;

			@Override
			public void buttonReleased(Button b) {
			}

			@Override
			public void buttonPressed(Button b) {
				floodlight = !floodlight;
				if (!floodlight)
					sensor.setFloodlight(false);
				else
					sensor.setFloodlight(Color.WHITE);
			}
		};
		Button.ENTER.addButtonListener(listener);
		while (true) {
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				break;
			}
			Delay.msDelay(500);
			LCD.clear();
			LCD.drawString(Integer.toString(sensor.getRawLightValue()), 0, 0);
			LCD.drawInt(sensor.getColorID(), 0, 1);
		}
	}

}
