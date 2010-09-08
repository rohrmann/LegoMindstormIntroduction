import lejos.nxt.LCD;

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

}
