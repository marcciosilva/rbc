import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;
import lejos.util.Delay;

public class ColorSensorTest {

	public static int error = 50;

	public static boolean inRange(int value, int reference) {
		// true if value is in range of reference
		return value >= reference - error && value <= reference + error;
	}

	public static void main(String[] args) {
		int rgbs[][] = { { 0, 0, 0 }, { 200, 60, 100 } };

		final ColorSensor sensor = new ColorSensor(SensorPort.S1);

		Button.LEFT.addButtonListener(new ButtonListener() {
			boolean calibradoLow = false;

			@Override
			public void buttonReleased(Button b) {
			}

			@Override
			public void buttonPressed(Button b) {
				if (!calibradoLow) {
					sensor.calibrateLow();
					LCD.drawString("LOW OK", 0, 5);
					calibradoLow = true;
				}
			}
		});

		Button.RIGHT.addButtonListener(new ButtonListener() {
			boolean calibradoHigh = false;

			@Override
			public void buttonReleased(Button b) {
			}

			@Override
			public void buttonPressed(Button b) {
				if (!calibradoHigh) {
					sensor.calibrateHigh();
					LCD.drawString("HIGH OK", 0, 4);
					calibradoHigh = true;
				}
			}
		});

		Button.ENTER.addButtonListener(new ButtonListener() {
			boolean floodlight = false;

			@Override
			public void buttonReleased(Button b) {
				floodlight = !floodlight;
				if (!floodlight)
					sensor.setFloodlight(false);
				else
					sensor.setFloodlight(Color.WHITE);

				Delay.msDelay(1000);
			}

			@Override
			public void buttonPressed(Button b) {
			}
		});

		while (true) {
			ColorSensor.Color color = sensor.getColor();
			for (int i = 0; i < 4; i++)
				LCD.clear(i);
			LCD.drawString("Sensor:", 0, 0);
			LCD.drawInt(color.getRed(), 0, 1);
			LCD.drawInt(color.getGreen(), 0, 2);
			LCD.drawInt(color.getBlue(), 0, 3);
			String strColor = "";
			int r = color.getRed();
			int g = color.getGreen();
			int b = color.getBlue();

			switch (color.getColor()) {
			case 7:
				strColor = "BLACK";
				break;
			case 2:
				strColor = "BLUE";
				break;
			case 1:
				strColor = "GREEN";
				break;
			case 3:
				strColor = "YELLOW";
				break;
			case 0:
				int luz = sensor.getNormalizedLightValue(); // (0..1024)
				LCD.drawInt(luz, 0, 7);
				int r_aux;
				int g_aux;
				int b_aux;

				if (luz < 300) {
					r_aux = 200;
					g_aux = 50;
					b_aux = 100;
				} else {
					r_aux = 100;
					g_aux = 25;
					b_aux = 40;
				}

				if (inRange(r, r_aux) && inRange(g, g_aux) && inRange(b, b_aux)) {
					strColor = "MAGENTA";
				} else {
					strColor = "RED";
				}

				break;
			case 4:
				strColor = "MAGENTA";
				break;
			case 6:
				strColor = "WHITE";
				break;
			default:
				if (((inRange(g, 0)) || (!inRange(g, 0) && inRange(r, 255) && inRange(
						b, 255))) && Math.abs(r - b) <= error)
					strColor = "MAGENTA";
				break;

			}
			LCD.clear(6);
			LCD.drawString(strColor, 0, 6);
			Delay.msDelay(300);
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				break;
			}
		}
	}

}
