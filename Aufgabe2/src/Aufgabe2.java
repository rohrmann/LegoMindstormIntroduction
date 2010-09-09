import lejos.nxt.*;

/**
 * Aufgabe 2 
 * Lego Praktikum SS 2010
 * 
 * This class measures the distance a motor with a 56 mm wheel has traveled by using the tacho count of the motor.
 * The motor has to be connected to the motor port A and it has to have a tacho. 
 * 
 * @author Wiebke Koepp 
 * @author Till Rohrmann
 * 
 */
public class Aufgabe2
{
	
	/**
	 *  distance the wheel has moved in cm
	 */
	private static float counter = 0;
	
	private static int oldTachoCount = 0;
	
	
  public static void main (String[] aArg)
  throws Exception
  {

	 /**
	  *  Thread which waits for a button to be pressed and then resets the counter to 0 
	  */
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
		int diff = currentTacho - oldTachoCount;
		oldTachoCount = currentTacho;
		counter += (float)diff/360*Math.PI*5.6;
		updateLCD();
	 }
	  
  }
  
  /**
   * This method prints the traveled distance on the LCD
   */
  public static void updateLCD(){
	  LCD.drawString(counter + getWhitespaces(16-(counter+"").length()), 0, 0);
  }
  
  /**
   * This method returns a string, which consists of whitespaces and has a length of stringLength characters
   * @param stringLength length of the returned string
   * @return string consisting of stringLength whitespaces 
   */
  public static String getWhitespaces(int stringLength){
	  String result = "";
	  for(int i = 0;i < stringLength; i++){
		  result += " ";
	  }
	  return result;
  }
  
}
