package utils;

import lejos.nxt.ColorSensor.Color;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class Utils {

	public static NXTRegulatedMotor leftMotor = Motor.A;
	public static NXTRegulatedMotor rightMotor = Motor.C;
	public static NXTRegulatedMotor catapulta = Motor.B;

	/**
	 * Dado un angulo, lo mapea al intervalo 0-360
	 * 
	 * @param angulo
	 *            Angulo
	 * @return Se devuelve el angulo convertido
	 */
	public static float modAngulo(float angulo) {
		angulo = angulo % 360.0f;
		if (angulo < 0)
			angulo += 360.0f;
		return angulo % 360.0f;
	}

	/**
	 * Enumerado que indica sentido de rotacion
	 * 
	 * @author marccio
	 *
	 */
	public static enum TurnSide {
		RIGHT, LEFT
	}

	/**
	 * Dado un angulo actual y un angulo objetivo, se determina si hay que girar
	 * hacia la izquierda o derecha para alcanzarlo
	 * 
	 * @param current
	 *            Angulo actual
	 * @param target
	 *            Angulo objetivo
	 * @return
	 */
	public static TurnSide FindTurnSide(float current, float target) {
		float diff = target - current;
		if (diff < 0.0f)
			diff += 360.0f;
		if (diff > 180.0f)
			return TurnSide.LEFT;
		else
			return TurnSide.RIGHT;
	}

	/***
	 * Determina si la medida actual esta en un rango de tolerancia de una
	 * medida esperada
	 * 
	 * @param valorActual
	 *            Medida actual
	 * @param valorEsperado
	 *            Medida esperada
	 * @param error
	 *            Tolerancia
	 * @return True si se esta en rango, False en otro caso
	 */
	public static boolean inRange(float valorActual, float valorEsperado, float error) {
		// true if value is in range of reference
		return ((valorActual <= valorEsperado + error) && (valorActual >= valorEsperado - error));
	}

	/***
	 * Determina si el angulo actual esta en un rango de tolerancia de un angulo
	 * esperado
	 * 
	 * @param valorActual
	 *            Angulo actual
	 * @param valorEsperado
	 *            Angulo esperado
	 * @param error
	 *            Tolerancia
	 * @return True si se esta en rango, False en otro caso
	 */
	public static boolean inRangeAngle(float valorActual, float valorEsperado, float error) {
		// true if value is in range of reference
		valorActual = modAngulo(valorActual);
		valorEsperado = modAngulo(valorEsperado);
		float smallestDifference = valorEsperado - valorActual;
		smallestDifference = ((smallestDifference + 180.0f) % 360.0f) - 180.0f;
		return (Math.abs(smallestDifference) <= error);
	}

	/**
	 * Dada una diferencia de medidas entre sensor largo y de media, determina
	 * si se esta delante de un robot
	 * 
	 * @param diferencia
	 * @return
	 */
	public static boolean hayRobot(int diferencia) {
		return diferencia > 400;
	}

	public static boolean isColorOrange(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		return (r >= 100 && g >= 100 && b >= 40);
	}

	public static boolean isColorBlue(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		return (r >= 75 && g <= 55 && b >= 65);
	}

}
