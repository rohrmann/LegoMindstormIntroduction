import java.util.Random;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.subsumption.Behavior;

/**
 * This class implements the behavior if the robot detects another underground.
 * It is activated if the light value differs from the default light value by
 * tolerance. If another underground is detected, the robot turns randomly and
 * continues its endless journey.
 * 
 * @author rohrmann
 * 
 */
public class BorderFound implements Behavior {

	private final int tolerance = 5;
	private int color;
	private LightSensor light;
	private Pilot pilot;
	private Random rand;
	private final int deterministicTurn = 60;
	private final int randomTurn = 60;

	public BorderFound(int color, SensorPort lightPort, Pilot pilot) {
		this.color = color;
		light = new LightSensor(lightPort);
		this.pilot = pilot;
		rand = new Random();
	}

	@Override
	public void action() {
		LCD.drawString("BorderFound", 0, 5);
		int direction = rand.nextBoolean() ? 1 : -1;
		int angle = rand.nextInt(randomTurn);
		pilot.rotate(direction * (angle + deterministicTurn));
	}

	@Override
	public void suppress() {
		LCD.drawString("suppress BorderFound", 0, 5);
		pilot.stop();
	}

	@Override
	public boolean takeControl() {
		LCD.drawInt(light.getLightValue(), 0, 0);
		return Math.abs(light.getLightValue() - color) > tolerance;
	}

}
