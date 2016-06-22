package sensors;

import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.SensorPort;

public class SensorColor extends Sensor<Color> {

	ColorSensor sensor;
	Color measurement;

	public SensorColor(SensorPort port) {
		sensor = new ColorSensor(port);
		measurement = null;
	}

	@Override
	public Color getMeasurement(boolean newMeasure) {
		if (newMeasure)
			measurement = sensor.getColor();
		return measurement;
	}

}
