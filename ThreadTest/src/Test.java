import java.util.Enumeration;
import java.util.Queue;

import lejos.nxt.comm.RConsole;

public class Test {

	public static Queue<Integer> largaDistanciaQueue = new Queue<Integer>();
	public static Queue<Integer> cortaDistanciaQueue = new Queue<Integer>();
	// promedios
	// public static final Lock lockLargaDistanciaPromedio = new
	// ReentrantLock();
	// public static final Object lockLargaDistanciaPromedio = new Object();
	public static int largaDistanciaPromedio = 0;
	// public static final Lock lockCortaDistanciaPromedio = new
	// ReentrantLock();
	// public static final Object lockCortaDistanciaPromedio = new Object();
	public static int cortaDistanciaPromedio = 0;

	public static void main(String[] args) {
		RConsole.openAny(10000);
		(new Thread() {
			float promedioLocal = 0.0f;
			int cantMediciones = 10;

			@Override
			public void run() {
				while (true) {
					if (Test.largaDistanciaQueue.size() == cantMediciones)
						// saco un elemento
						Test.largaDistanciaQueue.pop();
					Test.largaDistanciaQueue.push((int) (Math.random() * 100));
					// agrego una medida imaginaria para reemplazar sensor
					promedioLocal = 0.0f;
					Enumeration<Integer> elements = Test.largaDistanciaQueue.elements();
					while (elements.hasMoreElements()) {
						promedioLocal += elements.nextElement();
					}
					promedioLocal /= Test.largaDistanciaQueue.size();
					// synchronized (Test.lockLargaDistanciaPromedio) {
					Test.largaDistanciaPromedio = (int) promedioLocal;
					// }
				}
			}
		}).start();

		(new Thread() {
			float promedioLocal = 0.0f;
			int cantMediciones = 10;

			@Override
			public void run() {
				while (true) {
					if (Test.cortaDistanciaQueue.size() == cantMediciones)
						// saco un elemento
						Test.cortaDistanciaQueue.pop();
					// agrego una medida imaginaria para reemplazar sensor
					Test.cortaDistanciaQueue.push((int) (Math.random() * 100));
					// agrego una medida imaginaria para reemplazar sensor
					promedioLocal = 0.0f;
					Enumeration<Integer> elements = Test.cortaDistanciaQueue.elements();
					while (elements.hasMoreElements()) {
						promedioLocal += elements.nextElement();
					}
					promedioLocal /= Test.cortaDistanciaQueue.size();
					// synchronized (Test.lockCortaDistanciaPromedio) {
					Test.cortaDistanciaPromedio = (int) promedioLocal;
					// }
				}
			}
		}).start();

		while (true) {
			// lockCortaDistanciaPromedio.lock();
			RConsole.println("Promedio corta distancia = "
					+ Integer.toString(cortaDistanciaPromedio));
			// lockCortaDistanciaPromedio.unlock();
			// lockLargaDistanciaPromedio.lock();
			RConsole.println("Promedio larga distancia = "
					+ Integer.toString(largaDistanciaPromedio));
			// lockLargaDistanciaPromedio.unlock();
			RConsole.println("##################################################");
			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// System.out.println(e.getMessage());
			// }
		}

	}
}
