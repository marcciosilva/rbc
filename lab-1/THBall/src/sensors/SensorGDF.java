package sensors;

import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;
import lejos.util.Delay;
import utils.Utils;

public class SensorGDF extends Sensor<Float> {

	private GyroSensor gyro;
	private GyroDirectionFinder gdf;
	private float measurement;

	public SensorGDF(SensorPort port) {
		gyro = new GyroSensor(port);
		gdf = new GyroDirectionFinder(gyro, true);
		// delay para calibracion
		Delay.msDelay(5000);
		gdf.setDegrees(0.0f);
		measurement = 0.0f;
	}

	@Override
	public Float getMeasurement(boolean newMeasure) {
		if (newMeasure)
			measurement = gdf.getDegrees();
		return measurement;
	}

	/***
	 * Resetea el heading del gyroDirectionFinder de acuerdo al angulo al cual
	 * deberia estar apuntando
	 */
	public void resetHeading() {
		float error = 30.0f;
		if (Utils.inRangeAngle(gdf.getDegrees(), 0.0f, error))
			gdf.setDegrees(0.0f);
		else if (Utils.inRangeAngle(gdf.getDegrees(), 90.0f, error))
			gdf.setDegrees(90.0f);
		else if (Utils.inRangeAngle(gdf.getDegrees(), 180.0f, error))
			gdf.setDegrees(180.0f);
		else if (Utils.inRangeAngle(gdf.getDegrees(), 270.0f, error))
			gdf.setDegrees(270.0f);
	}

}
