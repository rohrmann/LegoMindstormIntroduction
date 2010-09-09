import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.remote.RemoteNXT;


public class Controller {
	
	public static void main(String[] args){
		try {
			LCD.drawString("Connecting...",0,0);
			final RemoteNXT nxt = new  RemoteNXT("asd",Bluetooth.getConnector());

			LCD.clear();
			LCD.drawString("Connected",0,0);
			
			Button.LEFT.addButtonListener(new ButtonListener() {
				
				@Override
				public void buttonReleased(Button arg0) {
					nxt.A.stop();
					
				}
				
				@Override
				public void buttonPressed(Button arg0) {
					nxt.A.forward();
					
				}
			}
			);
			
			Button.RIGHT.addButtonListener(new ButtonListener(){

				@Override
				public void buttonPressed(Button arg0) {
					nxt.B.forward();
				}

				@Override
				public void buttonReleased(Button arg0) {
					nxt.B.stop();
					
				}
				
			});
			
			Button.ENTER.addButtonListener(new ButtonListener(){

				@Override
				public void buttonPressed(Button arg0) {
					nxt.A.forward();
					nxt.B.forward();
					
				}

				@Override
				public void buttonReleased(Button arg0) {
					nxt.A.stop();
					nxt.B.stop();
				}
				
			});
			
			Button.ESCAPE.addButtonListener(new ButtonListener(){

				@Override
				public void buttonPressed(Button arg0) {
					nxt.A.backward();
					nxt.B.backward();
					
				}

				@Override
				public void buttonReleased(Button arg0) {
					nxt.A.stop();
					nxt.B.stop();
				}
				
			});
			
			LCD.drawString("controlling", 0, 1);
			
			while(true)
				;
		} catch (IOException ioe) {
			LCD.clear();
			LCD.drawString("Conn Failed",0,0);
			Button.waitForPress();
			System.exit(1);
		}
		
		
	}

}
