import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;


public class Behaviors {
	public static void main(String[] args){
		
		LightSensor light = new LightSensor(SensorPort.S1);
		
		LCD.drawString("Press button",0,0);
		Button.waitForPress();
		int color = light.getLightValue();
		
		Pilot pilot = new TachoPilot(5.6f, 10.5f, Motor.A, Motor.B,false);
		
		SnakeLines snake = new SnakeLines(75,45,pilot);
		BorderFound border = new BorderFound(color,SensorPort.S1,pilot);
		GoingStraight straight = new GoingStraight(20,SensorPort.S2,pilot);
		Behavior[] behaviors = {snake,border,straight};
		Arbitrator arbitrator = new Arbitrator(behaviors);
		arbitrator.start();
	}
}
