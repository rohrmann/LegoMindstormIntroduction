import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.remote.RemoteNXT;

/**
 * Class that represents a brick that is able to control a brick, which is
 * installed on a robot with two motors and wheels. The Controller is supposed
 * to be able to move that robot by the four buttons: Left -> Turn to the left,
 * Right -> Turn to the Right, Enter -> Move Forwards, Escape -> Move Backwards
 * 
 * @author Till Rohrmann
 * @author Wiebke Koepp
 * 
 */
public class Controller {

	public static void main(String[] args) {
		try {
			LCD.drawString("Connecting...", 0, 0);
			// connect with the brick on the robot (asd = name of other brick)
			final RemoteNXT nxt = new RemoteNXT("asd", Bluetooth.getConnector());

			LCD.clear();
			LCD.drawString("Connected", 0, 0);

			/*
			 * for every button define what needs to happen when button is
			 * pressed an released make robot drive when button is pressed and
			 * stop if button is released in case of Left and Right just use one
			 * motor to make the robot turn
			 */
			Button.LEFT.addButtonListener(new ButtonListener() {

				@Override
				public void buttonReleased(Button arg0) {
					nxt.A.stop();

				}

				@Override
				public void buttonPressed(Button arg0) {
					nxt.A.forward();

				}
			});

			Button.RIGHT.addButtonListener(new ButtonListener() {

				@Override
				public void buttonPressed(Button arg0) {
					nxt.B.forward();
				}


				@Override
				public void buttonReleased(Button arg0) {
					nxt.B.stop();
								}
			});

			Button.ENTER.addButtonListener(new ButtonListener() {

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

			Button.ESCAPE.addButtonListener(new ButtonListener() {

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

			LCD.drawString("controlling.", 0, 1);

			while (true)
				;
		} catch (IOException ioe) {
			LCD.clear();
			LCD.drawString("Con Failed.", 0, 0);
			Button.waitForPress();
			System.exit(1);
		}

	}

}
