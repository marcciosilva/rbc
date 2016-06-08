package main;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import behaviors.Agregacion;
import behaviors.Dispersion;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;
import lejos.nxt.remote.RemoteNXT;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class THBall {

	// public static NXTCam cam;
	public static long timer;
	private static Arbitrator arbitrator;
	public final static int SPEED_DRIVE = (int) (Motor.A.getMaxSpeed() / 2.0f);
	public final static int SPEED_TURN = (int) (Motor.A.getMaxSpeed() / 6.0f);
	public final static int SPEED_CORRECT = (int) (Motor.A.getMaxSpeed() / 32.0f);
	final static int SPEED_CALIBRATION = 18;
	final static int CATAPULTA_TIRAR = (int) Motor.A.getMaxSpeed();
	final static int CATAPULTA_MOVER = (int) (CATAPULTA_TIRAR / 10.0f);
	public final static float ERROR_PERMITIDO_ANGULO = 10.0f;
	// actuadores
	public static NXTRegulatedMotor leftMotor = Motor.A;
	public static NXTRegulatedMotor rightMotor = Motor.C;
	public static NXTRegulatedMotor catapulta = Motor.B;
	// sensores
	public static RemoteNXT remoteNxt;
	public static OpticalDistanceSensor largaDistancia;
	// OpticalDistanceSensor(SensorPort.S1);
	public static OpticalDistanceSensor cortaDistancia;
	public static Queue<Integer> largaDistanciaQueue = new LinkedList<Integer>();
	public static Queue<Integer> cortaDistanciaQueue = new LinkedList<Integer>();
	// promedios
	public static final Lock lockLargaDistanciaPromedio = new ReentrantLock();
	public static int largaDistanciaPromedio = 0;
	public static final Lock lockCortaDistanciaPromedio = new ReentrantLock();
	public static int cortaDistanciaPromedio = 0;
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
				timer = System.currentTimeMillis();
				arbitrator.start();
				exitMenu = true;
			} else
				exitMenu = true;
		}
	}

	/**
	 * Inicializa comportamientos y arbitrator
	 */
	private static void setupBehaviors() {
		// Behavior avanzar = new Avanzar();
		// Behavior avoid = new Avoid();
		// Behavior corregir = new Corregir();
		// Behavior tirarAzul = new TirarAzul();
		// // Behavior tengoNaranja = new TengoNaranja();
		// Behavior tirarNaranja = new TirarNaranja();
		Behavior dispersion = new Dispersion();
		Behavior agregacion = new Agregacion();
		// Behavior evitarDeadlock = new EvitarDeadlock();
		// pongo behaviors en orden de prioridad
		// a mayor indice mayor prioridad
		Behavior behaviors[] = { /*
									 * avanzar, corregir, avoid, tirarNaranja,
									 * tirarAzul, evitarDeadlock,
									 */dispersion, agregacion };
		// tirarNaranja
		// };
		// declaro arbitrator
		arbitrator = new Arbitrator(behaviors);
	}

	/**
	 * El robot deja de moverse
	 */
	public static void stopMoving() {
		leftMotor.stop();
		rightMotor.stop();
	}

	/**
	 * Establece la velocidad de los motores de movilidad
	 */
	public static void setSpeed(int speed) {
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
	}

	/**
	 * Inicializa sensores remotos y threads de polling a sensores de distancia
	 */
	public static void inicializar() {
		// largaDistancia = null;
		// cortaDistancia = null;
		try {
			remoteNxt = new RemoteNXT("rbc4_2", Bluetooth.getConnector());
			largaDistancia = new OpticalDistanceSensor(remoteNxt.S1);
			cortaDistancia = new OpticalDistanceSensor(remoteNxt.S2);
			// cam = new NXTCam(remoteNxt.S2);
			// cam = new NXTCam(SensorPort.S2);
			// // cam.sendCommand('B'); // object tracking mode
			// cam.sendCommand('A'); // sort by size
			// cam.sendCommand('E'); // enable tracking
			RConsole.openAny(10000);
			// inicializo threads para promediar medidas de los sharps
			(new Thread() {
				float promedioLocal = 0.0f;
				int cantMediciones = 10;

				@Override
				public void run() {
					while (true) {
						if (THBall.largaDistanciaQueue.size() == cantMediciones)
							// saco un elemento
							THBall.largaDistanciaQueue.poll();
						// agrego una medida de uno de los sharps
						THBall.largaDistanciaQueue.add(largaDistancia.getDistance());
						// calculo nuevo promedio
						promedioLocal = 0.0f;
						for (int val : THBall.largaDistanciaQueue) {
							promedioLocal += val;
						}
						promedioLocal /= THBall.largaDistanciaQueue.size();
						try {
							THBall.lockLargaDistanciaPromedio.lock();
							THBall.largaDistanciaPromedio = (int) promedioLocal;
						} finally {
							THBall.lockLargaDistanciaPromedio.unlock();
						}

					}
				}
			}).start();

			(new Thread() {
				float promedioLocal = 0.0f;
				int cantMediciones = 10;

				@Override
				public void run() {
					while (true) {
						if (THBall.cortaDistanciaQueue.size() == cantMediciones)
							// saco un elemento
							THBall.cortaDistanciaQueue.poll();
						// agrego una medida de uno de los sharps
						THBall.cortaDistanciaQueue.add(cortaDistancia.getDistance());
						// calculo nuevo promedio
						promedioLocal = 0.0f;
						for (int val : THBall.cortaDistanciaQueue) {
							promedioLocal += val;
						}
						promedioLocal /= THBall.cortaDistanciaQueue.size();
						try {
							THBall.lockCortaDistanciaPromedio.lock();
							THBall.cortaDistanciaPromedio = (int) promedioLocal;
						} finally {
							THBall.lockCortaDistanciaPromedio.unlock();
						}

					}
				}
			}).start();

		} catch (IOException e) {
			LCD.clear();
			LCD.drawString(e.getMessage(), 0, 0);
			System.exit(1);
		}
		gdf = new GyroDirectionFinder(gyro, true);
		Delay.msDelay(5000);
		// gdf.setDegreesCartesian(0.0f);
		gdf.setDegrees(0.0f);
		catapulta.resetTachoCount();
		bajarCatapulta();
	}

	/**
	 * Baja la catapulta hacia la posicion de recoleccion
	 */
	public static void bajarCatapulta() {
		catapulta.setSpeed(CATAPULTA_MOVER);
		catapulta.rotateTo(-190, false);
	}

	/**
	 * Sube la catapulta hacia la posicion de origen
	 */
	public static void subirCatapulta() {
		catapulta.setSpeed(CATAPULTA_MOVER);
		catapulta.rotateTo(0, false);
	}

	/**
	 * Se mueve la catapulta mediante la cantidad de grados indicados por
	 * parametro
	 * 
	 * @param angle
	 *            Cantidad de grados a mover
	 */
	public static void moverCatapulta(int angle) {
		catapulta.setSpeed(CATAPULTA_MOVER);
		catapulta.rotateTo(angle, false);
	}

	/**
	 * El robot arroja la pelota
	 */
	public static void tirarPelota() {
		catapulta.setSpeed(CATAPULTA_TIRAR);
		catapulta.rotateTo(-60, false);
		bajarCatapulta();
	}

	/**
	 * El robot avanza a velocidad SPEED_DRIVE
	 */
	public static void avanzar() {
		setSpeed(SPEED_DRIVE);
		leftMotor.forward();
		rightMotor.forward();
	}

	/**
	 * El robot retrocede durante la cantidad de tiempo indicada por parametro
	 * 
	 * @param ms
	 *            Cantidad de milisegundos
	 */
	public static void atrasar(int ms) {
		setSpeed(SPEED_DRIVE);
		leftMotor.backward();
		rightMotor.backward();
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

	/**
	 * Gira la cantidad de grados indicada por parametro
	 * 
	 * @param angulo
	 *            Cantidad de grados a girar
	 */
	public static void turnBy(float angulo) {
		float anguloActual = modAngulo(gdf.getDegrees());
		float anguloObjetivo = anguloActual + modAngulo(angulo);
		turnTo(anguloObjetivo);
	}

	/**
	 * Rota hacia el angulo objetivo
	 * 
	 * @param anguloObjetivo
	 *            Angulo objetivo
	 */
	public static void turnTo(float anguloObjetivo) {
		setSpeed(SPEED_TURN);
		float anguloActual = modAngulo(gdf.getDegrees());
		// por si me pasan un valor de afuera y no desde turnBy
		anguloObjetivo = modAngulo(anguloObjetivo);
		TurnSide turnSide;
		if (FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.RIGHT) {
			turnSide = TurnSide.RIGHT;
			turnRight(SPEED_TURN);
		} else {
			turnSide = TurnSide.LEFT;
			turnLeft(SPEED_TURN);
		}
		while (true) {
			anguloActual = modAngulo(gdf.getDegrees());
			if (inRangeAngle(anguloActual, anguloObjetivo, ERROR_PERMITIDO_ANGULO)) {
				stopMoving();
				break;
			}
			if ((FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.RIGHT) && (turnSide != TurnSide.RIGHT)) {
				turnSide = TurnSide.RIGHT;
				turnRight(SPEED_TURN);
			} else if ((FindTurnSide(anguloActual, anguloObjetivo) == TurnSide.LEFT) && turnSide != TurnSide.LEFT) {
				turnSide = TurnSide.LEFT;
				turnLeft(SPEED_TURN);
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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

	/***
	 * Gira hacia la derecha seteando la velocidad de los motores de acuerdo al
	 * parametro speed
	 * 
	 * @param speed
	 *            Nueva velocidad de los motores
	 */
	public static void turnRight(int speed) {
		setSpeed(speed);
		leftMotor.forward();
		rightMotor.backward();
	}

	/***
	 * Gira hacia la izquierda seteando la velocidad de los motores de acuerdo
	 * al parametro speed
	 * 
	 * @param speed
	 *            Nueva velocidad de los motores
	 */
	public static void turnLeft(int speed) {
		setSpeed(speed);
		rightMotor.forward();
		leftMotor.backward();
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
		setSpeed(SPEED_DRIVE);
		leftMotor.backward();
		rightMotor.backward();
	}

	/***
	 * Devuelve un promedio de 10 mediciones del sensor pasado por parametro
	 * 
	 * @param sensor
	 *            Sensor a partir del cual se obtiene medicion
	 * @return Promedio
	 */
	public static int getSharpDistance(OpticalDistanceSensor sensor) {
		// return sensor.getDistance();
		int address = sensor.getAddress();
		int returnVal = -1;
		// capaz hay que meter algun try aca por si failea algun lock
		if (address == largaDistancia.getAddress()) {
			THBall.lockLargaDistanciaPromedio.lock();
			returnVal = THBall.largaDistanciaPromedio;
			THBall.lockLargaDistanciaPromedio.unlock();
		} else if (address == cortaDistancia.getAddress()) {
			THBall.lockCortaDistanciaPromedio.lock();
			returnVal = THBall.cortaDistanciaPromedio;
			THBall.lockCortaDistanciaPromedio.unlock();
		}
		return returnVal;
	}

}