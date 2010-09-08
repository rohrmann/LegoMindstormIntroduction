import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;


public class Sender {
	
	public static void main(String[] args){
		RemoteDevice rd = Bluetooth.getKnownDevice("explorer");
		
		if(rd == null){
			LCD.drawString("could not find",0,0);
			LCD.drawString("explorer",0,1);
			Button.waitForPress();
			System.exit(1);
		}
		NXTConnection con = Bluetooth.connect(rd);
		
		if(con == null){
			LCD.clear();
			LCD.drawString("could not establish", 0, 0);
			LCD.drawString("connection",0,1);
			Button.waitForPress();
			System.exit(1);
		}
		
		LCD.clear();
		LCD.drawString("con established", 0,0);
		
		DataOutputStream dos = con.openDataOutputStream();
		
		int left =0;
		int right = 0;
		int enter = 0;
		boolean escapePressed=false;
		
		while(!escapePressed){
			int id = Button.waitForPress();
			if((id&1)!=0){
				LCD.clear();
				LCD.drawString("enter pressed",0,0);
				enter++;
			}
			if((id&2)!=0){
				LCD.clear();
				LCD.drawString("left pressed",0,1);
				left++;
			}
			if((id&4)!=0){
				LCD.clear();
				LCD.drawString("right pressed",0,2);
				right++;
			}
			if((id&8)!=0){
				LCD.clear();
				LCD.drawString("esc pressed",0,3);
				escapePressed = true;
			}
			
			if(!escapePressed){
				try {
					dos.writeInt(1);
					dos.writeInt(enter);
					dos.writeInt(left);
					dos.writeInt(right);
				} catch (IOException e) {
					LCD.drawString("error while", 0, 0);
					LCD.drawString("sending data",0,1);
					System.exit(1);
				}
			}
			else{
				try{
					dos.writeInt(0);
				}catch(IOException e){
					LCD.drawString("error while", 0, 0);
					LCD.drawString("sending term",0,1);
					System.exit(1);
				}
			}
			
			try {
				dos.flush();
			} catch (IOException e) {
				LCD.drawString("error while", 0, 0);
				LCD.drawString("flushing",0,1);
				System.exit(1);
			}
		}
		
		try {
			dos.close();
		} catch (IOException e) {
			LCD.drawString("error while closing", 0, 0);
			LCD.drawString("dos",0,1);
			System.exit(1);
		}
		
		con.close();
		
	}

}
