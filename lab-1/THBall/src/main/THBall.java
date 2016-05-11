package main;

import behaviors.Avanzar;
import behaviors.Avoid;
import behaviors.Corregir;
import behaviors.TengoNaranja;
import behaviors.TirarAzul;
import behaviors.TirarNaranja;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class THBall {

	private static Arbitrator arbitrator;
	public final static int SPEED_DRIVE = (int) (Motor.A.getMaxSpeed() / 2.0f);
	public final static int SPEED_TURN = (int) (SPEED_DRIVE / 4.0f);
	public final static int SPEED_CORRECT = (int) (SPEED_TURN / 4.0f);
	final static int SPEED_CALIBRATION = 18;
	final static int CATAPULTA_MOVER = 50;
	final static int CATAPULTA_TIRAR = (int) Motor.A.getMaxSpeed();
	// actuadores
	public static NXTRegulatedMotor leftMotor = Motor.A;
	public static NXTRegulatedMotor rightMotor = Motor.C;
	public static NXTRegulatedMotor catapulta = Motor.B;
	// sensores
	// public static OpticalDistanceSensor largaDistancia = new
	// OpticalDistanceSensor(SensorPort.S1);
	// public static OpticalDistanceSensor cortaDistancia = new
	// OpticalDistanceSensor(SensorPort.S4);
	public static GyroSensor gyro = new GyroSensor(SensorPort.S1);
	// LCD.drawString("Calibrando", 0, 0);
	public static GyroDirectionFinder gdf;
	public static TouchSensor leftTouchSensor = new TouchSensor(SensorPort.S2);
	public static TouchSensor rightTouchSensor = new TouchSensor(SensorPort.S4);
	public static ColorSensor colorSensor = new ColorSensor(SensorPort.S3);

	public static float conversionAngles = 11.6f / 2f;

	public static void main(String[] args) {

		LCD.drawString("Presione algun boton", 0, 0);
		Button.waitForAnyPress();

		inicializar();
		setupBehaviors();
		boolean exitMenu = false;
		LCD.clear();
		LCD.drawString("Presione algun boton", 0, 0);
		int buttonPressed = 0;
		while (!exitMenu) {
			buttonPressed = Button.readButtons();
			if ((buttonPressed & Button.ID_ESCAPE) != Button.ID_ESCAPE) {
				LCD.clear();
				arbitrator.start();
				exitMenu = true;
			} else
				exitMenu = true;
		}
	}

	private static void setupBehaviors() {
		Behavior avanzar = new Avanzar();
		Behavior avoid = new Avoid();
		Behavior corregir = new Corregir();
		Behavior tirarAzul = new TirarAzul();
		Behavior tengoNaranja = new TengoNaranja();
		Behavior tirarNaranja = new TirarNaranja();
		// pongo behaviors en orden de prioridad
		// a mayor indice mayor prioridad
		Behavior behaviors[] = { avanzar, corregir, avoid, tirarNaranja };// tirarAzul,
		// tengoNaranja,
		// avoid, tirarNaranja };
		// declaro arbitrator
		arbitrator = new Arbitrator(behaviors);
	}

	public static void sleep(int tiempo) {
		try {
			Thread.sleep(tiempo);
		} catch (InterruptedException e) {
			LCD.clear();
			LCD.drawString(e.getMessage(), 0, 0);
		}
	}

	public static void stopMoving() {
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

	public static void turn(int angle) {
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
			// Thread.yield();
			// sleep(50);
		}
		forwardMotor.stop();
		backwardMotor.stop();
	}

	public static void exit() {
	}

	public static void inicializar() {
		gdf = new GyroDirectionFinder(gyro, true);
		Delay.msDelay(5000);
		gdf.setDegreesCartesian(0.0f);
		catapulta.resetTachoCount();
		bajarCatapulta();
	}

	public static void bajarCatapulta() {
		catapulta.setSpeed(CATAPULTA_MOVER);
		catapulta.rotateTo(90);
	}

	public static void subirCatapulta() {
		catapulta.setSpeed(CATAPULTA_MOVER);
		catapulta.rotateTo(0);
	}

	public static void moverCatapulta(int angle) {
		catapulta.setSpeed(CATAPULTA_MOVER);
		catapulta.rotateTo(angle);
	}

	public static void tirarPelota() {
		catapulta.setSpeed(CATAPULTA_TIRAR);
		catapulta.rotateTo(30);
		bajarCatapulta();
	}

	public static void girarRandom() {
		try {
			double sign = Math.random();
			if (sign > 0.5)
				THBall.turn((int) (Math.random() * 180));
			else
				THBall.turn((int) (-Math.random() * 180));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void avanzar() {
		leftMotor.forward();
		rightMotor.forward();
	}

	/**
	 * Viaja durante los milisegundos que se le pasen
	 * 
	 * @param i
	 */
	public static void travelFor(int miliseconds) {
		THBall.setSpeed(SPEED_DRIVE);
		avanzar();
		Delay.msDelay(miliseconds);
		stopMoving();
	}

	public static void turnBy(float angulo) {
		float anguloInicial = gdf.getDegrees() % 360;
		if (anguloInicial < 0)
			anguloInicial += 360;
		float anguloActual;
		turn();
		while (true) {
			anguloActual = gdf.getDegrees() % 360;
			if (anguloActual < 0)
				anguloActual += 360;
			if (inRange(anguloActual, (anguloInicial + 90) % 360, 3.0f)) {
				stopMoving();
				break;
			}
			// Motor.A.forward();
			// Motor.C.backward();
		}

	}

	public static boolean inRange(float valorActual, float valorEsperado, float error) {
		// true if value is in range of reference
		return ((valorActual <= valorEsperado + error) && (valorActual >= valorEsperado - error));
	}

	public static void turnTo(float anguloDeseado) {
		turn();
		while (true) {
			float anguloActual = gdf.getDegrees() % 360;
			if (anguloActual < 0)
				anguloActual += 360;
			if (inRange(anguloActual, anguloDeseado, 3.0f)) {
				stopMoving();
				break;
			}
		}
	}

	public static void turn() {
		leftMotor.forward();
		rightMotor.backward();
	}

	public static void resetGyro() {
		float error = 5.0f;
		if (inRange(gdf.getDegrees(), 0.0f, error))
			gdf.setDegreesCartesian(0.0f);
		else if (inRange(gdf.getDegrees(), 90.0f, error))
			gdf.setDegreesCartesian(90.0f);
		else if (inRange(gdf.getDegrees(), 180.0f, error))
			gdf.setDegreesCartesian(180.0f);
		else if (inRange(gdf.getDegrees(), 270.0f, error))
			gdf.setDegreesCartesian(270.0f);
	}

}