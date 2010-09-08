import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.subsumption.Behavior;


public class GoingStraight implements Behavior {

	private final int threshold;
	private UltrasonicSensor sonic;
	private Pilot pilot;
	
	public GoingStraight(int threshold,SensorPort ultrasonicPort,Pilot pilot){
		this.threshold = threshold;
		sonic = new UltrasonicSensor(ultrasonicPort);
		this.pilot = pilot;
	}
	
	@Override
	public void action() {
		LCD.drawString("GoingStraight", 0, 5);
		pilot.forward();
	}

	@Override
	public void suppress() {
		LCD.drawString("suppress GoingStraight",0,5);
		pilot.stop();

	}

	@Override
	public boolean takeControl() {
		LCD.drawInt(sonic.getDistance(), 0, 1);
		return sonic.getDistance() < threshold;
	}

}
