package behaviors;

import lejos.robotics.subsumption.Behavior;
import main.THBall;

public class Corregir implements Behavior {

	private float anguloActual = 0.0f;
	private float anguloDeseado = 0.0f;

	@Override
	public boolean takeControl() {
		anguloActual = THBall.compass.getDegrees();
		if (THBall.facingLeft == THBall.originalFacing) {
			// se usa angulo original para corregir
			// porque deberia tener la misma orientacion
			anguloDeseado = THBall.anguloOriginal;
		} else {
			// tiene que respetar el angulo opuesto del original
			anguloDeseado = (THBall.anguloOriginal + 180) % 360;
		}
		// toma control si se desvia mas de 0.5 grados
		return (Math.abs(anguloActual - THBall.anguloOriginal) > 1.0f) && !THBall.girando;
	}

	@Override
	public void action() {
		if (anguloActual > anguloDeseado) {
			THBall.rightMotor.rotate((int) (Math.abs(anguloActual - anguloDeseado) * THBall.conversionAngles), true);
		} else if (anguloActual < anguloDeseado) {
			THBall.leftMotor.rotate((int) (Math.abs(anguloActual - anguloDeseado) * THBall.conversionAngles), true);
		}
	}

	@Override
	public void suppress() {
		THBall.leftMotor.stop();
		THBall.rightMotor.stop();
	}

}
