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
	private int turns;
		
	public LineIntersection(int color, int tolerance,Pilot pilot,SensorPort leftLightPort, SensorPort rightLightPort){
		this.color=color;
		this.tolerance = tolerance;
		this.pilot = pilot;
		this.left = new LightSensor(leftLightPort);
		this.right=new LightSensor(rightLightPort);
		turns = 0;
	}

	@Override
	public void action() {
		active = true;

		while(active && (lineIntersection(left)||lineIntersection(right))){
			if(lineIntersection(left)&&lineIntersection(right)){
				Helper.drawString("crossing", 0, 0);
				if(turns %2 ==0 ){
					Motor.B.forward();
					Motor.A.stop();

					while(lineIntersection(right)){
						;
					}
				}
				else{
					Motor.A.forward();
					Motor.B.stop();
					while(lineIntersection(left)){
						;
					}
				}
				
				turns++;
			}
			if(lineIntersection(left)){
				Helper.drawString("I left",0,0);
				Motor.A.stop();
				Motor.B.forward();
			}
			else{
				Helper.drawString("I right",0,0);
				Motor.A.forward();
				Motor.B.stop();
			}
		}
		
		pilot.stop();
	}

	@Override
	public void suppress() {
		pilot.stop();
		active = false;

	}
	
	public boolean lineIntersection(LightSensor sensor){
		return Math.abs(sensor.getLightValue()-color)>tolerance;
	}

	@Override
	public boolean takeControl() {
		return lineIntersection(left)^lineIntersection(right);
	}

}
