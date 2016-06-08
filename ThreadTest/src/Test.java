import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

	public static Queue<Integer> largaDistanciaQueue = new LinkedList<Integer>();
	public static Queue<Integer> cortaDistanciaQueue = new LinkedList<Integer>();
	// promedios
	public static final Lock lockLargaDistanciaPromedio = new ReentrantLock();
	public static int largaDistanciaPromedio = 0;
	public static final Lock lockCortaDistanciaPromedio = new ReentrantLock();
	public static int cortaDistanciaPromedio = 0;

	public static void main(String[] args) {
		(new Thread() {
			float promedioLocal = 0.0f;
			int cantMediciones = 10;

			@Override
			public void run() {
				while (true) {
					if (Test.largaDistanciaQueue.size() == cantMediciones)
						// saco un elemento
						Test.largaDistanciaQueue.poll();
					// agrego una medida imaginaria para reemplazar sensor
					Test.largaDistanciaQueue.add((int) (Math.random() * 100));
					// calculo nuevo promedio
					promedioLocal = 0.0f;
					for (int val : Test.largaDistanciaQueue) {
						promedioLocal += val;
					}
					promedioLocal /= Test.largaDistanciaQueue.size();
					try {
						Test.lockLargaDistanciaPromedio.lock();
						Test.largaDistanciaPromedio = (int) promedioLocal;
					} finally {
						Test.lockLargaDistanciaPromedio.unlock();
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
					if (Test.cortaDistanciaQueue.size() == cantMediciones)
						// saco un elemento
						Test.cortaDistanciaQueue.poll();
					// agrego una medida imaginaria para reemplazar sensor
					Test.cortaDistanciaQueue.add((int) (Math.random() * 100));
					// calculo nuevo promedio
					promedioLocal = 0.0f;
					for (int val : Test.cortaDistanciaQueue) {
						promedioLocal += val;
					}
					promedioLocal /= Test.cortaDistanciaQueue.size();
					try {
						Test.lockCortaDistanciaPromedio.lock();
						Test.cortaDistanciaPromedio = (int) promedioLocal;
					} finally {
						Test.lockCortaDistanciaPromedio.unlock();
					}

				}
			}
		}).start();

		while (true) {
			lockCortaDistanciaPromedio.lock();
			System.out.println("Promedio corta distancia = " + Integer.toString(cortaDistanciaPromedio));
			lockCortaDistanciaPromedio.unlock();
			lockLargaDistanciaPromedio.lock();
			System.out.println("Promedio larga distancia = " + Integer.toString(largaDistanciaPromedio));
			lockLargaDistanciaPromedio.unlock();
			System.out.println("##################################################");
			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// System.out.println(e.getMessage());
			// }
		}

	}

}
