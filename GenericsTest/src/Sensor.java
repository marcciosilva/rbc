public abstract class Sensor<T> {

	/**
	 * Returns new measure if newMeasure is true Returns old measure if
	 * newMeasure is false
	 * 
	 * @param newMeasure
	 * @return
	 */
	public abstract T getMeasurement(boolean newMeasure);

}
