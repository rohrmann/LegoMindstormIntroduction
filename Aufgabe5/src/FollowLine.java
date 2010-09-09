import lejos.robotics.navigation.Pilot;
import lejos.robotics.subsumption.Behavior;

/**
 * This class implements the default behavior of the robot: It let the robot drive forward.
 * @author rohrmann
 *
 */
public class FollowLine implements Behavior {
	
	private Pilot pilot;
	
	public FollowLine(Pilot pilot){
		this.pilot = pilot;
	}

	@Override
	public void action() {
		pilot.forward();
	}

	@Override
	public void suppress() {
		pilot.stop();
	}

	@Override
	public boolean takeControl() {
		return true;
	}

}
