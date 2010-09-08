import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.navigation.Pilot;


public class LineIntersection implements Behavior {
	
	private int color;
	private int tolerance;
	private Pilot pilot;
	private LightSensor left;
	private LightSensor right;
	private boolean active;
		
	public LineIntersection(int color, int tolerance,Pilot pilot,SensorPort leftLightPort, SensorPort rightLightPort){
		this.color=color;
		this.tolerance = tolerance;
		this.pilot = pilot;
		this.left = new LightSensor(leftLightPort);
		this.right=new LightSensor(rightLightPort);
	}

	@Override
	public void action() {
		active = true;

		if(Helper.lineIntersection(left,color,tolerance)){
			Helper.drawString("I left",0,0);
			Motor.A.stop();
			Motor.B.forward();
		}
		else{
			Helper.drawString("I right",0,0);
			Motor.A.forward();
			Motor.B.stop();
		}

		while(active && (Helper.lineIntersection(left,color,tolerance)^Helper.lineIntersection(right,color,tolerance)))
			Thread.yield();
		
		pilot.stop();
	}

	@Override
	public void suppress() {
		pilot.stop();
		active = false;

	}
	
	

	@Override
	public boolean takeControl() {
		return Helper.lineIntersection(left,color,tolerance)^Helper.lineIntersection(right,color,tolerance);
	}

}
