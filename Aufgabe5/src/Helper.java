import lejos.nxt.LCD;
import lejos.nxt.LightSensor;

/**
 * Class which provides convenient methods
 * @author rohrmann
 *
 */
public class Helper {
	
	/**
	 * draw text starting at specified column and row and filling the rest of the line with whitespaces
	 * @param text
	 * @param column
	 * @param row
	 */
	public static void drawString(String text, int column, int row){
		LCD.drawString(text+getWhitespaces(text.length(),16),column,row);
	}
	
	/**
	 * Returns a string of the length columnLength-textlength consisiting of whitespaces
	 * @param textLength
	 * @param columnLength
	 * @return
	 */
	public static String getWhitespaces(int textLength, int columnLength){
		String result = "";
		
		for(int i =0; i< columnLength-textLength;i++){
			result += " ";
		}
		
		return result;
	}
	
	/**
	 * check whether the light sensor sensor detects a line
	 * @param sensor
	 * @param color
	 * @param tolerance
	 * @return
	 */
	public static boolean lineIntersection(LightSensor sensor,int color, int tolerance){
		return Math.abs(sensor.getLightValue()-color)>tolerance;
	}

}
