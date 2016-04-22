import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BTSend {
	public static void main(String[] args) throws Exception {
		String name = "rbc4_2";
		LCD.clear();
		LCD.drawString("Connecting...", 0, 0);
		LCD.refresh();
		try {
			RemoteDevice receiver = Bluetooth.getKnownDevice(name);
			if (receiver == null)
				throw new IOException("no such device");
			BTConnection connection = Bluetooth.connect(receiver);
			if (connection == null)
				throw new IOException("Connect fail");
			LCD.drawString("connected.", 0, 1);
			DataInputStream input = connection.openDataInputStream();
			DataOutputStream output = connection.openDataOutputStream();
			output.writeInt(42);
			output.writeInt(-42);
			output.flush();
			LCD.drawString("Sent data", 0, 2);
			LCD.drawString("Waiting ...", 0, 3);
			int answer = input.readInt();
			LCD.drawString("# = " + answer, 0, 4);
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
		Button.waitForAnyPress();
	}
}