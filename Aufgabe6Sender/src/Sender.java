import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

/**
 * Class that represents the Sender of a communication between two bricks. The
 * sender builds up the connection and sends information about how often its
 * buttons have been pressed and also tells the receiver whether it will send
 * more information or terminate now. The receiver than displays that
 * information.
 * 
 * @author Wiebke Koepp
 * @author Till Rohrmann
 * 
 */
public class Sender {

	public static void main(String[] args) {

		// bricks get to know each other (explorer = name of other brick)
		RemoteDevice rd = Bluetooth.getKnownDevice("explorer");

		// error handling -> device could not be found
		if (rd == null) {
			LCD.drawString("Could not find", 0, 0);
			LCD.drawString("explorer.", 0, 1);
			Button.waitForPress();
			System.exit(1);
		}

		// open the connection to the other brick
		NXTConnection con = Bluetooth.connect(rd);

		// error handling -> connecting did not work
		if (con == null) {
			LCD.clear();
			LCD.drawString("Could not establish", 0, 0);
			LCD.drawString("connection.", 0, 1);
			Button.waitForPress();
			System.exit(1);
		}

		LCD.clear();
		LCD.drawString("Con established.", 0, 0);

		// open data out put stream to send information
		DataOutputStream dos = con.openDataOutputStream();

		// initialize counters for the different buttons and a boolean for
		// termination
		int left = 0;
		int right = 0;
		int enter = 0;
		boolean escapePressed = false;

		while (!escapePressed) {
			// wait for press returns value with bits set or not set for every
			// button
			int id = Button.waitForPress();
			// 0x01 -> Enter
			if ((id & 1) != 0) {
				LCD.clear();
				LCD.drawString("Enter pressed.", 0, 0);
				enter++;
			}
			// 0x02 -> Left
			if ((id & 2) != 0) {
				LCD.clear();
				LCD.drawString("Left pressed.", 0, 1);
				left++;
			}
			// 0x04 -> Right
			if ((id & 4) != 0) {
				LCD.clear();
				LCD.drawString("Right pressed.", 0, 2);
				right++;
			}
			// 0x08 -> Escape
			if ((id & 8) != 0) {
				LCD.clear();
				LCD.drawString("Esc pressed.", 0, 3);
				escapePressed = true;
			}

			// Sender does not want to terminate -> Send a 1 followed by the
			// counters for enter, left and right
			if (!escapePressed) {
				try {
					dos.writeInt(1);
					dos.writeInt(enter);
					dos.writeInt(left);
					dos.writeInt(right);
				} catch (IOException e) {
					LCD.drawString("error while", 0, 0);
					LCD.drawString("sending data", 0, 1);
					System.exit(1);
				}
			}
			// Sender wants to terminate -> Send a 0 and do all the necessary
			// steps to terminate connection
			else {
				try {
					dos.writeInt(0);
				} catch (IOException e) {
					LCD.drawString("error while", 0, 0);
					LCD.drawString("sending term", 0, 1);
					System.exit(1);
				}
			}

			try {
				dos.flush();
			} catch (IOException e) {
				LCD.drawString("error while", 0, 0);
				LCD.drawString("flushing", 0, 1);
				System.exit(1);
			}
		}

		try {
			dos.close();
		} catch (IOException e) {
			LCD.drawString("error while closing", 0, 0);
			LCD.drawString("dos", 0, 1);
			System.exit(1);
		}

		con.close();

	}

}
