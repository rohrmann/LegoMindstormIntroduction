import lejos.nxt.*;
import lejos.nxt.comm.RConsole;

import javax.microedition.lcdui.Graphics;

/**
 * Aufgabe 1 Lego Praktikum SS 2010
 * 
 * This class shows a string on the LCD which can be automatically as well as
 * manually scrolled. Moreover, it shows the light value of a light sensor,
 * which has to be connected to the sensor port S1 in the continually in the
 * second line. Last but not least, it prints an animated circle in the lower
 * half of the screen.
 * 
 * @author Wiebke Koepp
 * @author Till Rohrmann
 * 
 */
public class Aufgabe1 {

	private static String text = "text that is to long to display in one line";

	/**
	 * counter counts how far the text is scrolled on the display
	 */
	private static int counter = 0;

	/**
	 * if escape is pressed, then all actions are suspended
	 */
	private static boolean escapePressed = false;

	private static boolean automaticMode = false;

	/**
	 * this thread is responsible for the automatic text scrolling
	 */
	private static Thread textThread = null;

	private static boolean textThreadRunning = true;

	private static LightSensor sensor = null;

	public static void main(String[] aArg) throws Exception {
		LCD.drawString("Waiting for RConsole connect", 0, 0);

		/**
		 * if no client connects to the rconsole within 5 seconds, then the
		 * program will be continued
		 */
		RConsole.openUSB(5000);

		Button.ESCAPE.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				escapePressed = !escapePressed;
			}

			public void buttonReleased(Button b) {
			}
		});

		Button.LEFT.addButtonListener(new ButtonListener() {
			public void buttonPressed(Button b) {
				if (escapePressed == false && automaticMode == false) {
					moveright();
				}
			}

			public void buttonReleased(Button b) {
			}

		});

		Button.RIGHT.addButtonListener(new ButtonListener() {

			public void buttonPressed(Button b) {
				if (escapePressed == false && automaticMode == false) {
					moveleft();
				}
			}

			public void buttonReleased(Button b) {

			}

		});

		Button.ENTER.addButtonListener(new ButtonListener() {

			public void buttonPressed(Button b) {

				if (escapePressed == false) {
					/**
					 * start the automatic mode
					 */
					if (automaticMode == false) {
						automaticMode = true;
						textThreadRunning = true;
						textThread = new Thread() {
							/**
							 * run scrolls continually the text form left to
							 * right and vice versa
							 */
							public void run() {
								boolean dir = true;

								while (textThreadRunning == true) {
									if (escapePressed == false) {
										if (dir == true) {
											dir = moveleft();
										} else {
											dir = moveright();
										}
										try {
											Thread.sleep(500);
										} catch (InterruptedException e) {
										}
									}
								}
							}
						};
						textThread.start();
					}
					/**
					 * terminate the automatic mode
					 */
					else {
						textThreadRunning = false;
						try {
							textThread.join();
						} catch (InterruptedException e) {
						}
						automaticMode = false;
					}
				}

			}

			public void buttonReleased(Button b) {
			}

		});

		sensor = new LightSensor(SensorPort.S1);
		SensorPort.S1.addSensorPortListener(new SensorPortListener() {

			public void stateChanged(SensorPort aSource, int aOldValue,
					int aNewValue) {
				if (escapePressed == false) {
					LCD.drawInt(sensor.getLightValue(), 0, 1);
					RConsole.println(sensor.getLightValue() + "");
				}

			}

		});

		/**
		 * Thread which is responsible for the animation. It increases
		 * continually the arc of the filled circle, which it draws. After it
		 * reached 360 degrees, it will decrease it continually.
		 */
		new Thread() {
			public void run() {
				int counter = 0;
				Graphics g = new Graphics();
				int inc = 5;
				while (true) {
					if (escapePressed == false) {
						counter += inc;
						if (counter >= 360 || counter <= 0) {
							inc = -inc;
							g.setColor(1 - g.getColor());
						}
						g.fillArc(26, 16, 48, 48, counter, inc);

					}

					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();

		updateLCD();

		while (true) {
			Thread.yield();
		}

	}

	/**
	 * This method moves the textline one character to the left
	 * 
	 * @return true if there are still some characters left on the right side,
	 *         which are not displayed yet
	 */
	public static boolean moveleft() {

		if (text.length() - counter > 16) {
			counter++;
			updateLCD();
			if (text.length() - counter == 16) {
				Sound.beep();
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * This method moves the line of text one character to the right
	 * 
	 * @return false if there are still some characters left on the left side,
	 *         which are not displayed yet
	 */
	public static boolean moveright() {

		if (counter > 0) {
			counter--;
			updateLCD();
			if (counter == 0) {
				Sound.beep();
				return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * This method prints the line of text on the display and the rconsole
	 */
	public static void updateLCD() {
		LCD.drawString(text.substring(counter), 0, 0);
		RConsole.println(text.substring(counter, counter + 16));
	}

}
