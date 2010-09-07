
import lejos.nxt.*;
import lejos.nxt.comm.RConsole;

import javax.microedition.lcdui.Graphics;


/**
 * Aufgabe 1 
 * Lego Praktikum SS 2010
 * 
 * @author Wiebke Koepp 
 * @author Till Rohrmann
 * 
 */
public class Aufgabe1
{
	
	private static String text = "text that is to long to display in one line";
		
	private static int counter = 0;
	
	private static boolean escapePressed = false;

	private static boolean automaticMode = false;
	
	private static Thread textThread = null;
	
	private static boolean textThreadRunning = true;
	
	private static LightSensor sensor = null;
		
	
  public static void main (String[] aArg)
  throws Exception
  {
	  LCD.drawString("Waiting for RConsole connect", 0, 0);
	  RConsole.openUSB(5000);
	  
	  Button.ESCAPE.addButtonListener(new ButtonListener(){
		public void buttonPressed(Button b) {
			escapePressed = !escapePressed;
		}
		public void buttonReleased(Button b) {
		}
	  });
	  
	  Button.LEFT.addButtonListener(new ButtonListener(){
		public void buttonPressed(Button b) {
			if(escapePressed == false && automaticMode == false){
				moveright();
			}
		}
		public void buttonReleased(Button b) {	
		}
		
	 	
    });
	  
	  Button.RIGHT.addButtonListener(new ButtonListener(){

		public void buttonPressed(Button b) {
			if(escapePressed == false && automaticMode == false){
				moveleft();
			}
		}

		public void buttonReleased(Button b) {
		
		}
		  
	  });
	  
	  Button.ENTER.addButtonListener(new ButtonListener(){

		public void buttonPressed(Button b) {
					
			if(escapePressed == false){
				if(automaticMode == false){
					automaticMode = true;
					textThreadRunning = true;
					textThread = new Thread(){
						
						
						
						public void run(){
							boolean dir = true;
					
							while(textThreadRunning == true){
								if(escapePressed == false){
									if(dir == true){
										dir = moveleft();
									}else{
										dir = moveright();
									}
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
								}
								}
							}
						}
					};
					textThread.start();
				}else{
					textThreadRunning = false;
					automaticMode = false;
				}
			}
			
		}

		public void buttonReleased(Button b) {
			// TODO Auto-generated method stub
			
		}
		  
	  });
	  
	  sensor = new LightSensor(SensorPort.S1);
	  SensorPort.S1.addSensorPortListener(new SensorPortListener(){

		public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
			if(escapePressed == false){
				LCD.drawInt(sensor.getLightValue(), 0, 1);
				RConsole.println(sensor.getLightValue()+ "");
			}
			
		}
		  
	  });
	  
	  new Thread(){
		  public void run(){
			  int counter = 0;
			  Graphics g = new Graphics();
			  int inc = 5;
			  while(true){
				  if(escapePressed == false){
					  counter += inc;
					  if(counter >= 360 || counter == 0){
						  inc = -inc;
						  g.setColor(1- g.getColor());
					  }
					  g.fillArc(26, 16, 48, 48, counter, inc);
					  
			
				  }
				  
				  try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}
			  }
		  }
	  }.start();
	  
	  updateLCD();
	  
	  while(true){
		  Thread.sleep(1000);
	  }
	  
  }	  
  
  public static boolean moveleft(){
	  
	  if(text.length() - counter > 16){
			counter++;
			updateLCD();
			if (text.length() - counter == 16){
				Sound.beep();
				return false;
			}
		return true;	
	  }
	  return false;
  }
  
  public static boolean moveright(){
	  
	  if(counter > 0){
		  counter--;
		  updateLCD();
		  if(counter == 0){
			  Sound.beep();
			  return true;
		  }
		  return false;
	  }
	  return true;
  }
  
  
  public static void updateLCD (){
	 LCD.drawString(text.substring(counter), 0, 0);
	 RConsole.println(text.substring(counter,counter+16));
  }
  
  
  
  
}
