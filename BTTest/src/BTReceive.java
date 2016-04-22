import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BTReceive {
	public static void main(String[] args) throws Exception {
		LCD.clear();
		LCD.drawString("Receiver wait...", 0, 0);
		LCD.refresh();
		try {
			BTConnection connection = Bluetooth.waitForConnection();
			if (connection == null)
				throw new IOException("Connect fail");
			LCD.drawString("Connected.", 0, 1);
			DataInputStream input = connection.openDataInputStream();
			DataOutputStream output = connection.openDataOutputStream();
			int answer1 = input.readInt();
			LCD.drawString("1st = " + answer1, 0, 2);
			int answer2 = input.readInt();
			LCD.drawString("2nd = " + answer2, 0, 3);
			output.writeInt(0);
			output.flush();
			LCD.drawString("Sent data", 0, 4);
			input.close();
			output.close();
			connection.close();
			LCD.drawString("Bye ...", 0, 5);
		} catch (Exception ioe) {
			LCD.clear();
			LCD.drawString("ERROR", 0, 6);
			LCD.drawString(ioe.getMessage(), 0, 7);
			LCD.refresh();
		}
		Thread.sleep(4000);
	}
}