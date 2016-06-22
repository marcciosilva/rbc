package sensors;

import lejos.nxt.I2CPort;
import lejos.nxt.addon.OpticalDistanceSensor;

public class SensorSharp extends Sensor<Integer> {

	OpticalDistanceSensor sensor;
	int measurement;

	public SensorSharp(I2CPort port) {
		sensor = new OpticalDistanceSensor(port);
		measurement = 0;
	}

	@Override
	public Integer getMeasurement(boolean newMeasure) {
		if (newMeasure)
			measurement = sensor.getDistance();
		return measurement;
	}

}
