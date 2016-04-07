package main;

import behaviors.Avanzar;
import behaviors.Avoid;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class THBall {

	public final static int SPEED_DRIVE = 500;
	public final static int SPEED_TURN = 150;
	// facingLeft = true indica que al inicio, el robot
	// tiene a la zona muerta a su lado izquierdo
	public static boolean facingLeft = false;
	public static NXTRegulatedMotor leftMotor = Motor.A;
	public static NXTRegulatedMotor rightMotor = Motor.C;
	public static UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S4);

	public static void main(String[] args) {
		// seteo sensor a modo continuo para no tener que
		// mandar pings manualmente
		ultrasonicSensor.continuous();
		Behavior avanzar = new Avanzar();
		Behavior avoid = new Avoid();

		Behavior behaviors[] = { avanzar, avoid };

		Arbitrator arbitrator = new Arbitrator(behaviors);

		// si se toca escape, sale
		// si toca right, esta mirando a la derecha
		// si toca left, esta mirando a la izquierda
		boolean exitMenu = false;
		while (!exitMenu) {
			while (Button.readButtons() != 0)
				;
			if ((Button.readButtons() & Button.ID_ESCAPE) != Button.ID_ESCAPE) {
				if ((Button.readButtons() & Button.ID_LEFT) == Button.ID_LEFT) {
					facingLeft = true;
					arbitrator.start();
					exitMenu = true;
				} else if ((Button.readButtons() & Button.ID_RIGHT) == Button.ID_RIGHT) {
					facingLeft = false;
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