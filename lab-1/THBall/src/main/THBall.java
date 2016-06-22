package main;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;
import lejos.nxt.remote.RemoteNXT;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import utils.SensorColor;
import utils.SensorSharp;
import behaviors.DistanceTest;

public class THBall {
	public static long timer;
	private Arbitrator arbitrator;
	public final static int SPEED_DRIVE = (int) (Motor.A.getMaxSpeed() / 2.0f);
	public final static int SPEED_TURN = (int) (Motor.A.getMaxSpeed() / 6.0f);
	public final static int SPEED_CORRECT = (int) (Motor.A.getMaxSpeed() / 32.0f);
	final static int SPEED_CALIBRATION = 18;
	final static int CATAPULTA_TIRAR = (int) Motor.A.getMaxSpeed();
	final static int CATAPULTA_MOVER = (int) (CATAPULTA_TIRAR / 10.0f);
	public final static float ERROR_PERMITIDO_ANGULO = 10.0f;
	// actuadores
	// public static LinearActuator lnrAct;
	public static NXTRegulatedMotor leftMotor = Motor.A;
	public static NXTRegulatedMotor rightMotor = Motor.C;
	public static NXTRegulatedMotor catapulta = Motor.B;
	// sensores
	public static RemoteNXT remoteNxt;
	private SensorSharp sensorLargaDistancia;
	private SensorSharp sensorMediaDistancia;
	private SensorSharp sensorMediaDistanciaMuerta;
	public static GyroSensor gyro = new GyroSensor(SensorPort.S1);
	public static GyroDirectionFinder gdf;
	public static TouchSensor leftTouchSensor = new TouchSensor(SensorPort.S2);
	public static TouchSensor rightTouchSensor = new TouchSensor(SensorPort.S4);
	private SensorColor colorSensor;
	public static float conversionAngles = 11.6f / 2f;

	public static void main(String[] args) {

		LCD.drawString("Presione algun boton", 0, 0);
		Button.waitForAnyPress();
		THBall tb = new THBall();
		tb.inicializar();
		tb.setupBehaviors();
		boolean exitMenu = false;
		LCD.clear();
		int buttonPressed = 0;
		while (!exitMenu) {
			buttonPressed = Button.readButtons();
			if ((buttonPressed & Button.ID_ESCAPE) != Button.ID_ESCAPE) {
				LCD.clear();
				timer = System.currentTimeMillis();
				tb.arbitrator.start();
				exitMenu = true;
			} else
				exitMenu = true;
		}
	}

	/**
	 * Inicializa comportamientos y arbitrator
	 */
	private void setupBehaviors() {
		// Behavior avanzar = new Avanzar();
		// Behavior avoid = new Avoid();
		// Behavior corregir = new Corregir();
		// Behavior tirarAzul = new TirarAzul(colorSensor);
		// Behavior tirarNaranja = new TirarNaranja(colorSensor);
		// Behavior dispersion = new Dispersion(sensorLargaDistancia,
		// sensorMediaDistancia,
		// sensorMediaDistanciaMuerta);
		// Behavior agregacion = new Agregacion(sensorLargaDistancia,
		// sensorMediaDistancia,
		// sensorMediaDistanciaMuerta);
		// Behavior evitarDeadlock = new EvitarDeadlock();

		// pongo behaviors en orden de prioridad
		// a mayor indice mayor prioridad
		// Behavior behaviors[] = { avanzar, corregir, avoid, tirarNaranja,
		// tirarAzul,
		// evitarDeadlock, dispersion, agregacion };
		DistanceTest distanceTest = new DistanceTest(sensorLargaDistancia,
				sensorMediaDistancia, sensorMediaDistanciaMuerta);
		Behavior behaviors[] = { distanceTest };
		// declaro arbitrator
		arbitrator = new Arbitrator(behaviors);
	}

	/**
	 * El robot deja de moverse
	 */
	public static void stopMoving() {
		try {
			leftMotor.stop();
			rightMotor.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Establece la velocidad de los motores de movilidad
	 */
	public static void setSpeed(int speed) {
		try {
			leftMotor.setSpeed(speed);
			rightMotor.setSpeed(speed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inicializa sensores remotos y threads de polling a sensores de distancia
	 */
	public void inicializar() {
		try {
			remoteNxt = new RemoteNXT("rbc4_2", Bluetooth.getConnector());
			sensorLargaDistancia = new SensorSharp(remoteNxt.S2);
			sensorMediaDistancia = new SensorSharp(remoteNxt.S1);
			sensorMediaDistanciaMuerta = new SensorSharp(remoteNxt.S3);
			// inicializacion de actuador lineal
			remoteNxt.C.resetTachoCount();
			remoteNxt.C.rotate(1000);
			remoteNxt.C.rotate(1000);
			RConsole.openAny(10000);
		} catch (Exception e) {
			LCD.clear();
			LCD.drawString(e.getClass().toString(), 0, 0);
			System.exit(1);
		}
		colorSensor = new SensorColor(SensorPort.S3);
		gdf = new GyroDirectionFinder(gyro, true);
		// espera para que el giroscopio se calibre
		Delay.msDelay(5000);
		gdf.setDegrees(0.0f);
		try {
			catapulta.resetTachoCount();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		bajarCatapulta();

		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}

			@Override
			public void buttonPressed(Button b) {
				try {
					remoteNxt.C.rotate(-1000, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					remoteNxt.C.rotate(-1000, false);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * Baja la catapulta hacia la posicion de recoleccion
	 */
	public static void bajarCatapulta() {
		try {
			catapulta.setSpeed(CATAPULTA_MOVER);
			catapulta.rotateTo(-190, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sube la catapulta hacia la posicion de origen
	 */
	public static void subirCatapulta() {
		try {
			catapulta.setSpeed(CATAPULTA_MOVER);
			catapulta.rotateTo(0, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Se mueve la catapulta mediante la cantidad de grados indicados por
	 * parametro
	 * 
	 * @param angle
	 *            Cantidad de grados a mover
	 */
	public static void moverCatapulta(int angle) {
		try {
			catapulta.setSpeed(CATAPULTA_MOVER);
			catapulta.rotateTo(angle, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * El robot arroja la pelota
	 */
	public static void tirarPelota() {
		try {
			catapulta.setSpeed(CATAPULTA_TIRAR);
			catapulta.rotateTo(-60, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bajarCatapulta();
	}

	/**
	 * El robot avanza a velocidad SPEED_DRIVE
	 */
	public static void avanzar() {
		try {
			setSpeed(SPEED_DRIVE);
			leftMotor.forward();
			rightMotor.forward();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * El robot retrocede durante la cantidad de tiempo indicada por parametro
	 * 
	 * @param ms
	 *            Cantidad de milisegundos
	 */
	public static void atrasar(int ms) {
		try {
			setSpeed(SPEED_DRIVE);
			leftMotor.backward();
			rightMotor.backward();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Delay.msDelay(ms);
		stopMoving();
	}

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
		return ((valorActual <= valorEsperado + error) && (valorActual >= valorEsperado
				- error));
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

	/***
	 * Gira hacia la derecha seteando la velocidad de los motores de acuerdo al
	 * parametro speed
	 * 
	 * @param speed
	 *            Nueva velocidad de los motores
	 */
	public static void turnRight(int speed) {
		try {
			setSpeed(speed);
			leftMotor.forward();
			rightMotor.backward();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * Gira hacia la izquierda seteando la velocidad de los motores de acuerdo
	 * al parametro speed
	 * 
	 * @param speed
	 *            Nueva velocidad de los motores
	 */
	public static void turnLeft(int speed) {
		try {
			setSpeed(speed);
			rightMotor.forward();
			leftMotor.backward();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * Resetea el heading del gyroDirectionFinder de acuerdo al angulo al cual
	 * deberia estar apuntando
	 */
	public static void resetGyro() {
		float error = 30.0f;
		if (inRangeAngle(gdf.getDegrees(), 0.0f, error))
			gdf.setDegrees(0.0f);
		else if (inRangeAngle(gdf.getDegrees(), 90.0f, error))
			gdf.setDegrees(90.0f);
		else if (inRangeAngle(gdf.getDegrees(), 180.0f, error))
			gdf.setDegrees(180.0f);
		else if (inRangeAngle(gdf.getDegrees(), 270.0f, error))
			gdf.setDegrees(270.0f);
	}

	/***
	 * El robot se mueve hacia atras a velocidad SPEED_DRIVE
	 */
	public static void atrasar() {
		try {
			setSpeed(SPEED_DRIVE);
			leftMotor.backward();
			rightMotor.backward();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean hayRobot(int diferencia) {
		return diferencia > 400;// && diferencia < 700;
	}

}