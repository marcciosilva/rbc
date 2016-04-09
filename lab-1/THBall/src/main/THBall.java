package main;

import behaviors.Avanzar;
import behaviors.Avoid;
import behaviors.Corregir;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class THBall {

	public final static int SPEED_DRIVE = 500;
	public final static int SPEED_TURN = 300;
	// facingLeft = true indica que al inicio, el robot
	// tiene a la zona muerta a su lado izquierdo
	public static boolean facingLeft = false;
	public static boolean originalFacing = false;
	public static NXTRegulatedMotor leftMotor = Motor.A;
	public static NXTRegulatedMotor rightMotor = Motor.C;
	public static UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S4);
	public static CompassHTSensor compass = new CompassHTSensor(SensorPort.S1);
	public static float anguloOriginal = 0.0f;
	// conversion de clicks del tacometro a grados
	public static float conversionAngles = 11.6f;
	public static boolean girando = false;

	public static void main(String[] args) {
		// seteo sensor a modo continuo para no tener que
		// mandar pings manualmente
		ultrasonicSensor.continuous();
		Behavior avanzar = new Avanzar();
		Behavior correct = new Corregir();
		Behavior avoid = new Avoid();

		Behavior behaviors[] = { avanzar, correct, avoid };

		Arbitrator arbitrator = new Arbitrator(behaviors);

		// si se toca escape, sale
		// si toca right, esta mirando a la derecha
		// si toca left, esta mirando a la izquierda
		LCD.drawString("Calibrando...", 0, 0);
		anguloOriginal = compass.getDegrees();
		boolean exitMenu = false;
		LCD.clear();
		LCD.drawString("Presione algun boton", 0, 0);
		while (!exitMenu) {
			if ((Button.readButtons() & Button.ID_ESCAPE) != Button.ID_ESCAPE) {
				if ((Button.readButtons() & Button.ID_LEFT) == Button.ID_LEFT) {
					LCD.clear();
					facingLeft = true;
					originalFacing = facingLeft;
					arbitrator.start();
					exitMenu = true;
				} else if ((Button.readButtons() & Button.ID_RIGHT) == Button.ID_RIGHT) {
					facingLeft = false;
					originalFacing = facingLeft;
					arbitrator.start();
					exitMenu = true;
				}
			}
		}
	}

	public static void setSpeed(int speed) {
		if (speed < SPEED_DRIVE) {
			leftMotor.setSpeed(speed);
			rightMotor.setSpeed(speed);
		} else {
			leftMotor.setSpeed(SPEED_DRIVE);
			rightMotor.setSpeed(SPEED_DRIVE);
		}
	}

}