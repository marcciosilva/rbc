public class DummySensor extends Sensor<Integer> {

	int measurement = 0;

	@Override
	public Integer getMeasurement(boolean newMeasure) {
		if (newMeasure)
			measurement = (int) Math.random() * 100;
		return measurement;
	}

}
