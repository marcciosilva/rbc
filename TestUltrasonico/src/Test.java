import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.util.Delay;

public class Test {
	public static void main(String[] args) {
		LCD.drawString("Press any button", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		OpticalDistanceSensor os1 = new OpticalDistanceSensor(SensorPort.S1);
		OpticalDistanceSensor os2 = new OpticalDistanceSensor(SensorPort.S4);
		while (true) {
			LCD.clear();
			LCD.drawString("Largo - " + Integer.toString(os1.getDistance()), 0, 0);
			LCD.drawString("Corto - " + Integer.toString(os2.getDistance()), 0, 1);
			if ((lejos.nxt.Button.readButtons() & lejos.nxt.Button.ID_ESCAPE) == lejos.nxt.Button.ID_ESCAPE)
				break;
			Delay.msDelay(200);
		}
	}
}
