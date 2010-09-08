import java.io.DataInputStream;
import java.io.IOException;

import lejos.nxt.*;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class Receiver {

	private static int enter = 0;
	private static int left = 0;
	private static int right = 0;
	
	
	private static boolean terminate = false;
	
	
	public static void main(String [ ] args){
		
		NXTConnection connectionBT = Bluetooth.waitForConnection();
		DataInputStream disBT =
			connectionBT.openDataInputStream();
		
		while(!terminate){
			try {
				if(disBT.readInt() == 1){
					enter = disBT.readInt();
					left = disBT.readInt();
					right = disBT.readInt();
				}
				else{
					terminate = true;
				}
			} catch (IOException e) {
				
			}
			LCD.drawString("Enter:" + enter, 0, 1);
			LCD.drawString("Left:" + left, 0, 2);
			LCD.drawString("Right:" + right, 0, 3);
		}
		
		LCD.drawString("Terminated!", 0, 5);
		try {
			disBT.close();
		} catch (IOException e) {
		
		}
		
	}

	
	
}
