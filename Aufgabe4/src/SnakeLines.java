import lejos.nxt.LCD;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.subsumption.Behavior;

/**
 * This class implements the snake line behavior. If this behavior is active,
 * the robot will alternating activate its motors. This behavior is the default
 * one.
 * 
 * @author rohrmann
 * 
 */
public class SnakeLines implements Behavior {

	private boolean active = false;
	private int arcAngle;
	private int turnRate;
	private Pilot pilot;

	public SnakeLines(int turnRate, int arcAngle, Pilot pilot) {
		this.pilot = pilot;
		this.arcAngle = arcAngle;
		this.turnRate = turnRate;
	}

	@Override
	public void action() {

		LCD.drawString("SnakeLines", 0, 5);

		active = true;
		int turn = 0;

		while (active) {
			if (turn % 2 == 0) {
				pilot.steer(turnRate, arcAngle);
			} else {
				pilot.steer(-1 * turnRate, arcAngle);
			}
			turn++;
		}
	}

	@Override
	public void suppress() {
		LCD.drawString("Suppress SnakeLines", 0, 5);
		active = false;
		pilot.stop();
	}

	@Override
	public boolean takeControl() {
		return true;
	}

}
