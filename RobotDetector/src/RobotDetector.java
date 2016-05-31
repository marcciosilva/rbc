import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class RobotDetector {

	public static void main(String[] args) {
		RConsole.openAny(10000);
		OpticalDistanceSensor larga = new OpticalDistanceSensor(SensorPort.S1);
		OpticalDistanceSensor corta = new OpticalDistanceSensor(SensorPort.S2);
		LCD.clear();
		LCD.drawString("Apreta algo viejita", 0, 0);
		Button.waitForAnyPress();
		LCD.clear();
		while (true) {
			LCD.clear();
			// LCD.drawString(Integer.toString(larga.getDistance()), 0, 0);
			// LCD.drawString(Integer.toString(corta.getDistance()), 0, 1);
			RConsole.println("Larga = " + Integer.toString(larga.getDistance()));
			RConsole.println("Corta = " + Integer.toString(corta.getDistance()));
			Delay.msDelay(1000);
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE)
				break;
		}
	}

}
