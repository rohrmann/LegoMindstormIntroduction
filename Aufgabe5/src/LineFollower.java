import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;


/***
 * Program which enables a robot to follow a line which can have crossings. To do that it needs
 * to lightsensors which enframe the line.
 * @author rohrmann
 *
 */
public class LineFollower {
	
	public static void main(String[] args){
		
		LightSensor left = new LightSensor(SensorPort.S2);
		LightSensor right = new LightSensor(SensorPort.S1);
		left.setFloodlight(true);
		right.setFloodlight(true);
		Helper.drawString("Press Button",0,0);
		Button.waitForPress();
		
		int color = left.getLightValue();
		int tolerance = 5;
		
		Helper.drawString("Left:" + left.getLightValue(),0,0);
		Helper.drawString("Right:"+right.getLightValue(), 0, 1);
		Helper.drawString("Press Button", 0, 2);
		
		Button.waitForPress();
		
		Pilot pilot = new TachoPilot(5.6f,105.f,Motor.A,Motor.B,false);
		
		pilot.setMoveSpeed(10);
		FollowLine line = new FollowLine(pilot);
		LineIntersection intersection = new LineIntersection(color, tolerance, pilot, SensorPort.S2, SensorPort.S1);
		
		Behavior[] behaviors = {line,intersection};
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.start();
	}

}
