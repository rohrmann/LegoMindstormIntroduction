import lejos.nxt.LCD;
import lejos.nxt.LightSensor;

/**
 * Class which provides convenient functions
 * @author rohrmann
 *
 */
public class Helper {
	
	public static void drawString(String text, int column, int row){
		LCD.drawString(text+getWhitespaces(text.length(),16),column,row);
	}
	
	public static String getWhitespaces(int textLength, int columnLength){
		String result = "";
		
		for(int i =0; i< columnLength-textLength;i++){
			result += " ";
		}
		
		return result;
	}
	
	public static boolean lineIntersection(LightSensor sensor,int color, int tolerance){
		return Math.abs(sensor.getLightValue()-color)>tolerance;
	}

}
