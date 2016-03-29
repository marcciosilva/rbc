package com.marcciosilva.rbc;

import java.awt.Rectangle;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lejos.nxt.addon.NXTCam;

public class CameraTest {
	

	public static void main(String[] args) {
		
		NXTCam cam = new NXTCam(SensorPort.S1);
		cam.enableTracking(true);
		int objNum = 0;
		while (true){
			if ((Button.readButtons() & Button.ID_ESCAPE) == Button.ID_ESCAPE) {
				cam.enableTracking(false);
				break;
			}
			Delay.msDelay(500); //espero para pollear la camara
			objNum = cam.getNumberOfObjects();
			LCD.clear();
			LCD.drawString("Amount = " + Integer.toString(objNum), 0, 0);
			for (int i = 0 ; i <= 8 ; i++){
				String obj = "";
				int objId = cam.getObjectColor(objNum);
				String color = "";
				switch (objId){
				case 1:
					color = "MAGENTA";
					break;
				case 2:
					color = "BLUE";
					break;
				case 3:
					color = "GREEN";
					break;
				case 4:
					color = "RED";
					break;
				case 5:
					color = "YELLOW";
					break;
				case 6:
					color = "BLACK";
					break;
				}
				obj = obj + color + " ";
				Rectangle rect = cam.getRectangle(objNum);
				float x = rect.x + rect.width / 2f;
				float y = rect.y - rect.height / 2f;
				obj = obj + "(" + Float.toString(x) + ", " + Float.toString(y) + ")";
				LCD.drawString(obj, 0, i+1);
			}
		}
	}

}
