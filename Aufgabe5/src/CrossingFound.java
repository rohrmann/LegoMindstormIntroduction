import java.util.ArrayList;
import java.util.List;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.subsumption.Behavior;

/**
 * This class implements the behavior to handle crossings. It will be activated if both light sensors detect
 * a line. It then drives a short distance forward and tries to analyze the crossing by doing a 360 degree turn
 * and saving the angles at which streets are recognized. Afterwards it tries to detect the direction from where
 * it came and then decides with this information where to turn to.
 * @author rohrmann
 *
 */
public class CrossingFound implements Behavior {
	
	private int color;
	private int tolerance;
	private Pilot pilot;
	private LightSensor left;
	private LightSensor right;
	private int turns;
	
	public CrossingFound(int color,int tolerance,Pilot pilot, SensorPort leftPort, SensorPort rightPort){
		this.color = color;
		this.tolerance = tolerance;
		this.pilot = pilot;
		left = new LightSensor(leftPort);
		right = new LightSensor(rightPort);
		turns =0;
	}

	@Override
	public void action() {
		/**
		 * drive forward so that the robot stands directly above the crossing
		 */
		Helper.drawString("drive to crossing", 0, 0);
		pilot.travel(7f);
		Helper.drawString("check crossing",0,0);
		pilot.reset();
		/**
		 * turn 360 degrees
		 */
		pilot.rotate(360,true);
		
		boolean lineFound = false;
		List<Float> list = new ArrayList<Float>();
		
		/**
		 * while the robot is moving check if a line was detected
		 */
		while(pilot.isMoving()){
			if(lineFound==false && Helper.lineIntersection(left, color, tolerance))
				lineFound = true;
			else if(lineFound==true && !Helper.lineIntersection(left, color, tolerance)){
				/**
				 * save angle of street
				 */
				list.add(pilot.getAngle());
				lineFound = false;
			}
		}
	
		/**
		 * Find the street from where the robot came. This is achieved by finding the street with
		 * the minimum angle difference compared to 180 degrees
		 */
		float diff = Math.abs(180-list.get(0));
		int index = 0;
		
		for(int i =1; i<list.size();i++){
			if(Math.abs(180-list.get(i))<diff){
				diff = Math.abs(180-list.get(i));
				index = i;
			}
		}
		
		/**
		 * right turn is the street with the index: index+1
		 */
		int rightIndex = index+1%list.size();
		
		/**
		 * left turn is the street with the index: index-1
		 */
		int leftIndex =0;
		if(index == 0)
			leftIndex = list.size()-1;
		else
			leftIndex = index-1;
		
		Helper.drawString("Turn", 0, 0);
		if(turns%2==0){
			pilot.rotate(list.get(leftIndex)<(360-list.get(leftIndex))?list.get(leftIndex):-(360-list.get(leftIndex)));
		}
		else{
			pilot.rotate(list.get(rightIndex)<(360-list.get(rightIndex))?list.get(rightIndex):-(360-list.get(rightIndex)));
		}
		
		turns++;
		
		
	}

	@Override
	public void suppress() {
		pilot.stop();
	}

	/**
	 * if both light sensors detect a line, then it is assumed that a crossing is found
	 */
	@Override
	public boolean takeControl() {
		return Helper.lineIntersection(left, color, tolerance)&& Helper.lineIntersection(right, color, tolerance);
	}

}
