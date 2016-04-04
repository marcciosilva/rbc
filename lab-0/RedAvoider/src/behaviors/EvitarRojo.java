package behaviors;

import java.awt.Color;
import java.awt.image.BufferedImage;

import lejos.robotics.subsumption.Behavior;
import robot.Robot;
import simbad.sim.CameraSensor;

public class EvitarRojo implements Behavior {
	private robot.Robot r;
	CameraSensor cam;
	BufferedImage cameraImg;
	Color currentAvgColor = new Color(0, 0, 0);
	double tv = 1;

	public EvitarRojo(Robot r1) {
		r = r1;
		cam = r.getCamera();
		// reservar espacio para la imágen
		cameraImg = cam.createCompatibleImage();
	}

	public boolean takeControl() {
		// para no calcular el promedo de color fps * sec veces
		if (r.getCurrentFrame() == 0) {
			cam.copyVisionImage(cameraImg);
			currentAvgColor = averageColor(cameraImg, 0, 0, cameraImg.getWidth(),
					cameraImg.getHeight());
		}
		// si ve rojo, se inicia el comportamiento
		return isRed(currentAvgColor);
	}

	public void action() {
		// si ve rojo, se mueve hacia atrás
		r.setTranslationalVelocity(-tv);
		System.out.println("behavior:evitar rojo");
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

	public static Color averageColor(BufferedImage img, int x0, int y0, int width, int height) {
		int x1 = x0 + width;
		int y1 = y0 + height;
		int avgR = 0;
		int avgG = 0;
		int avgB = 0;
		// para cada pixel de la imágen se suman valores
		// r, g y b a avgR, avgG y avgB, para calcular
		// un color promedio al final
		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				Color pixel = new Color(img.getRGB(x, y));
				avgR += pixel.getRed();
				avgG += pixel.getGreen();
				avgB += pixel.getBlue();
			}
		}
		int pixels = width * height;
		avgR /= pixels;
		avgG /= pixels;
		avgB /= pixels;
		return new Color(avgR, avgG, avgB);
	}

	public static boolean isRed(Color color) {
		return color.getRed() > 50;
	}

}
