import lejos.nxt.*;

/**
 * Aufgabe 2 
 * Lego Praktikum SS 2010
 * 
 * @author Wiebke Koepp 
 * @author Till Rohrmann
 * 
 */
public class Aufgabe2
{
	
	/* number of the cm the wheel has moved */
	private static float counter = 0;
	
	private static int oldTacho = 0;
	
	
  public static void main (String[] aArg)
  throws Exception
  {

	 /* Thread that resets the couter to 0 */
	 new Thread(){
		 public void run(){
			 while(true){
				 Button.waitForPress();
			 	 counter = 0;
			 	 updateLCD();
			 }
		 }
	 }.start();
	 
	 Motor.A.resetTachoCount();
	 
	 while(true){
		int currentTacho = Motor.A.getTachoCount();
		int dif = currentTacho - oldTacho;
		oldTacho = currentTacho;
		counter += (float)dif/360*Math.PI*5.6;
		updateLCD();
	 }
	  
  }
  
  public static void updateLCD(){
	  LCD.drawString(counter + getWhitespaces(16-(counter+"").length()), 0, 0);
  }
  
  public static String getWhitespaces(int number){
	  String result = "";
	  for(int i = 0;i < number; i++){
		  result += " ";
	  }
	  return result;
  }
  
}
