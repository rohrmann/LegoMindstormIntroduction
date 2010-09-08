import lejos.nxt.Motor;
import lejos.robotics.subsumption.Behavior;

/**
 * This class implements the snake line behavior. If this behavior is active, the robot will 
 * alternating activate its motors. This behavior is the default one.
 * 
 * @author rohrmann
 *
 */
public class SnakeLines implements Behavior {
	
	private boolean active = false;
	private int angleToRotate = 30;

	@Override
	public void action() {
		active = true;
		int turn = 0;
		
		while(active){
			if(turn % 2 == 0){
				Motor.A.rotate(angleToRotate);
			}
			else{
				Motor.B.rotate(angleToRotate);
			}
			
			turn++;
		}

	}

	@Override
	public void suppress() {
		active = false;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

}
