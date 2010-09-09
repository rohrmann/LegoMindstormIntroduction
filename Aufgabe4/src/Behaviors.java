import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

/**
 * This class uses the behavior control model to implement the following
 * behavior: The default behavior is that the robot drives forward using snake
 * lines. If it finds a different underground, the robot will randomly turn and
 * then continue its endless journey. If someone holds his hand above the robot,
 * it will drive straight forward without checking the underground.
 * 
 * Hardware: Robot needs a light sensor which checks the ground. Moreover it
 * needs an ultrasonic sensor which is directed to the ceiling.
 * 
 * @author rohrmann
 * 
 */
public class Behaviors {
	public static void main(String[] args) {

		LightSensor light = new LightSensor(SensorPort.S1);

		LCD.drawString("Press button", 0, 0);
		Button.waitForPress();
		/**
		 * read the current light value of the underground and use this value as
		 * the default value
		 */
		int color = light.getLightValue();

		Pilot pilot = new TachoPilot(5.6f, 11.5f, Motor.A, Motor.B, false);

		SnakeLines snake = new SnakeLines(75, 45, pilot);
		BorderFound border = new BorderFound(color, SensorPort.S1, pilot);
		GoingStraight straight = new GoingStraight(20, SensorPort.S2, pilot);
		Behavior[] behaviors = { snake, border, straight };
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.start();
	}
}
