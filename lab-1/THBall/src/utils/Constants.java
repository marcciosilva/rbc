package utils;

import lejos.nxt.Motor;

public class Constants {

	public final static int SPEED_DRIVE = (int) (Motor.A.getMaxSpeed() / 2.0f);
	public final static int SPEED_TURN = (int) (Motor.A.getMaxSpeed() / 6.0f);
	public final static int SPEED_CORRECT = (int) (Motor.A.getMaxSpeed() / 32.0f);
	public final static int CATAPULTA_TIRAR = (int) Motor.A.getMaxSpeed();
	public final static int CATAPULTA_MOVER = (int) (CATAPULTA_TIRAR / 10.0f);
	public final static float ERROR_PERMITIDO_ANGULO = 10.0f;

}
