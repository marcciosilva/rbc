import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class UltrasonicTest {

	public static void main(String[] args) {
		UltrasonicSensor sensor = new UltrasonicSensor(SensorPort.S1);
		sensor.continuous();

		while (true) {
			if (sensor.getMode() == UltrasonicSensor.MODE_CONTINUOUS) {
				LCD.drawInt(sensor.getDistance(), 0, 0);
			} else if (sensor.getMode() == UltrasonicSensor.MODE_PING) {
				sensor.ping();
				int[] distances = new int[8];
				if (sensor.getDistances(distances) > 0) {
					int i = 0;
					for (int dist : distances) {
						LCD.drawInt(dist, 0, i);
						i++;
					}
				}

			}
			Delay.msDelay(1000);
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				break;
			}

			if ((Button.readButtons() & Button.ID_ENTER) == Button.ID_ENTER) {
				if (sensor.getMode() == UltrasonicSensor.MODE_CONTINUOUS)
					sensor.setMode(UltrasonicSensor.MODE_PING);
				else
					sensor.continuous();
			}

			LCD.clear();
		}

		sensor.off();

	}

}
