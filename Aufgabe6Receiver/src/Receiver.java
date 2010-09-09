import java.io.DataInputStream;
import java.io.IOException;

import lejos.nxt.*;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

/**
 * Class that represents the Receiver of a communication between two bricks. The
 * sender builds up the connection and sends information about how often its
 * buttons have been pressed and also tells the receiver whether it will send
 * more information or terminate now. The receiver than displays that
 * information.
 * 
 * @author Wiebke Koepp
 * @author Till Rohrmann
 * 
 */
public class Receiver {

	// counter for the three buttons: enter, left and right
	private static int enter = 0;
	private static int left = 0;
	private static int right = 0;

	public static void main(String[] args) {

		// wait for the connection to be established
		NXTConnection con = Bluetooth.waitForConnection();

		// open a data input stream to be able to read the data
		DataInputStream dis = con.openDataInputStream();

		// boolean that is false until sender wants to terminate
		boolean terminate = false;

		// the first number of the information send by the sender is either a 0
		// or a 1
		// if it is a 1, the sender wants to send new numbers
		// if it is a 0, the sender want to terminate
		while (!terminate) {
			try {
				// if the sender sends a 1, the next three numbers will be the
				// counts for
				// the buttons
				if (dis.readInt() == 1) {
					enter = dis.readInt();
					left = dis.readInt();
					right = dis.readInt();
				}
				// sender sends a 0, there will be no more information
				// -> make the while-loop terminate
				else {
					terminate = true;
				}
			} catch (IOException e) {
				LCD.clear();
				LCD.drawString("Error while reading", 0, 1);
				LCD.drawString("information.", 0, 2);
				terminate = true;
			}
			// show the numbers on the display
			LCD.drawString("Enter:" + enter, 0, 1);
			LCD.drawString("Left:" + left, 0, 2);
			LCD.drawString("Right:" + right, 0, 3);
		}

		LCD.drawString("Terminated!", 0, 5);

		// close connection and stream
		try {
			dis.close();
		} catch (IOException e) {
			LCD.clear();
			LCD.drawString("Error while closing", 0, 1);
			LCD.drawString("stream.", 0, 2);
		}
		con.close();
	}

}
