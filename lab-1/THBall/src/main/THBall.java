package main;

import behaviors.Avanzar;
import behaviors.Avoid;
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

	private static Arbitrator arbitrator;
	// mapa de la pista
	private static int currentRow = 0;
	private static int rows = 5;
	public static boolean goingLeft = false;

	public final static int SPEED_DRIVE = (int) Motor.A.getMaxSpeed();
	public final static int SPEED_TURN = SPEED_DRIVE / 2;
	final static int SPEED_CALIBRATION = 18;
	// facingLeft = true indica que al inicio, el robot
	// tiene a la zona muerta a su lado izquierdo
	public static boolean facingLeft = false;
	public static boolean originalFacing = false;

	// actuadores
	public static NXTRegulatedMotor leftMotor = Motor.A;
	public static NXTRegulatedMotor rightMotor = Motor.C;
	// sensores
	public static UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S4);
	public static CompassHTSensor compass = new CompassHTSensor(SensorPort.S1);
	// public static float anguloOriginal = 0.0f;
	// conversion de clicks del tacometro a grados
	public static float conversionAngles = 11.6f / 2f;

	public static void main(String[] args) {
		// RConsole.open();
		LCD.drawString("Presione algun boton", 0, 0);
		Button.waitForAnyPress();
		// seteo sensor a modo continuo para no tener que
		// mandar pings manualmente
		ultrasonicSensor.continuous();
		// new Thread() {
		//
		// int angulo;
		//
		// @Override
		// public void run() {
		// LCD.clear(1);
		// LCD.drawString(Integer.toString(angulo), 0, 1);
		// RConsole.print(Integer.toString(angulo));
		// Delay.msDelay(200);
		// }
		//
		// }.start();

		setupBehaviors();
		// calibración del compás magnético
		// calibrateCompass();
		// si se toca escape, sale
		// si toca right, la zona muerta esta a su derecha
		// si toca left, la zona muerta esta a su izquierda
		boolean exitMenu = false;
		LCD.clear();
		LCD.drawString("Presione algun boton", 0, 0);
		int buttonPressed = 0;
		while (!exitMenu) {
			buttonPressed = Button.readButtons();
			if ((buttonPressed & Button.ID_ESCAPE) != Button.ID_ESCAPE) {
				if ((buttonPressed & Button.ID_LEFT) == Button.ID_LEFT) {
					LCD.clear();
					facingLeft = true;
					goingLeft = true;
					originalFacing = facingLeft;
					arbitrator.start();
					exitMenu = true;
				} else if ((buttonPressed & Button.ID_RIGHT) == Button.ID_RIGHT) {
					facingLeft = false;
					goingLeft = false;
					originalFacing = facingLeft;
					arbitrator.start();
					exitMenu = true;
				}
			} else
				exitMenu = true;
		}
	}

	private static void setupBehaviors() {
		Behavior avanzar = new Avanzar();
		// Behavior correct = new Corregir();
		Behavior avoid = new Avoid();
		// Behavior avoidDeathzone = new AvoidDeathzone();
		// pongo behaviors en orden de prioridad
		// a mayor indice mayor prioridad
		Behavior behaviors[] = { avanzar, avoid };// , avoidDeathzone
		// };
		// declaro arbitrator
		arbitrator = new Arbitrator(behaviors);
	}

	private static void calibrateCompass() {
		LCD.clear();
		LCD.drawString("Calibrando...", 0, 0);
		// anguloOriginal = compass.getDegrees();
		compass.startCalibration();
		setSpeed(SPEED_CALIBRATION);
		leftMotor.forward();
		rightMotor.backward();
		// la idea es que en 40 segundos haga dos vueltas completas
		// hay que calibrar eso experimentalmente
		sleep(40000);
		leftMotor.stop();
		rightMotor.stop();
		compass.stopCalibration();
		// cinco segundos para posicionar al robot donde se quiera
		sleep(5000);
		compass.resetCartesianZero();
	}

	public static void sleep(int tiempo) {
		try {
			Thread.sleep(tiempo);
		} catch (InterruptedException e) {
			LCD.clear();
			LCD.drawString(e.getMessage(), 0, 0);
		}
	}

	public static void stopMotors() {
		leftMotor.stop();
		rightMotor.stop();
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

	public static void resetCurrentRow() {
		currentRow = 0;
	}

	public static void nextRow() {
		currentRow = (currentRow++) % rows;
	}

	public static boolean nextToDeathZone() {
		return currentRow == rows - 1;
	}

	// public static void turn(int angle) throws Exception {
	// if (angle < 0) {
	// rightMotor.rotate((int) Math.abs(angle * THBall.conversionAngles),
	// true);
	// } else {
	// leftMotor.rotate((int) (angle * THBall.conversionAngles), true);
	// }
	// rightMotor.rotate((int) (angle * THBall.conversionAngles), true);
	// leftMotor.rotate((int) (-angle * THBall.conversionAngles), true);
	// }

	public static void turn(int angle) {
		boolean stop = false;
		int numDegrees = (int) Math.abs(Math.round(angle * conversionAngles));
		// set motors up for counter-clockwise rotation
		NXTRegulatedMotor forwardMotor = leftMotor;
		NXTRegulatedMotor backwardMotor = rightMotor;
		// if angle is negative, switch motors for clockwise rotation
		if (angle < 0) {
			forwardMotor = rightMotor;
			backwardMotor = leftMotor;
		}
		forwardMotor.resetTachoCount();
		backwardMotor.resetTachoCount();
		forwardMotor.forward();
		backwardMotor.backward();
		while (((forwardMotor.getTachoCount() < numDegrees) || (backwardMotor.getTachoCount() > -numDegrees))) {
			if (forwardMotor.getTachoCount() > numDegrees)
				forwardMotor.stop();
			if (backwardMotor.getTachoCount() < -numDegrees)
				backwardMotor.stop();
			Thread.yield();
			sleep(50);
		}
		forwardMotor.stop();
		backwardMotor.stop();
	}

}