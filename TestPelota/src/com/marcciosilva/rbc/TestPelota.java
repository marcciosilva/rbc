package com.marcciosilva.rbc;

import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.remote.RemoteMotor;
import lejos.nxt.remote.RemoteNXT;
import lejos.util.Delay;

public class TestPelota {

	static RemoteNXT nxt = null;
	public static RemoteMotor pinzaDer;
	public static RemoteMotor pinzaIzq;
	public static RemoteMotor catapulta;

	public static void main(String[] args) {
		try {
			LCD.drawString("Connecting...", 0, 0);
			NXTCommConnector connector = Bluetooth.getConnector();
			nxt = new RemoteNXT("rbc4_2", connector);
			LCD.clear();
			LCD.drawString("Connected", 0, 0);
		} catch (IOException ioe) {
			LCD.clear();
			LCD.drawString("Conn Failed", 0, 0);
			System.exit(1);
		}
		pinzaDer = nxt.C;
		pinzaIzq = nxt.A;
		catapulta = nxt.B;
		inicializar();
		Button.waitForAnyPress();
		Delay.msDelay(2000);
		catapulta.resetTachoCount();
		catapulta.rotateTo(-190, false);
		nxt.stopProgram();
		nxt.close();
	}

	public static void inicializar() {
		pinzaIzq.resetTachoCount();
		pinzaDer.resetTachoCount();
		catapulta.resetTachoCount();
		girarPinzas(-110);
	}

	public static void girarPinzas(int angulo) {
		pinzaIzq.rotateTo(angulo);
		pinzaDer.rotateTo(angulo);
	}

}
