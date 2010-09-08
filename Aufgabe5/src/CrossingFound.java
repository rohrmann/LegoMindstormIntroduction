import java.util.ArrayList;
import java.util.List;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.subsumption.Behavior;


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
		Helper.drawString("drive to crossing", 0, 0);
		pilot.travel(7f);
		Helper.drawString("check crossing",0,0);
		pilot.reset();
		pilot.rotate(360,true);
		
		boolean lineFound = false;
		List<Float> list = new ArrayList<Float>();
		
		while(pilot.isMoving()){
			if(lineFound==false && Helper.lineIntersection(left, color, tolerance))
				lineFound = true;
			else if(lineFound==true && !Helper.lineIntersection(left, color, tolerance)){
				list.add(pilot.getAngle());
				lineFound = false;
			}
		}
		
		Helper.drawString("Streets:" + list.size(),0,0);
		for(int i=0; i< list.size();i++){
			Helper.drawString(list.get(i)+"",0,i+1);
		}
	
		
		float diff = Math.abs(180-list.get(0));
		int index = 0;
		
		for(int i =1; i<list.size();i++){
			if(Math.abs(180-list.get(i))<diff){
				diff = Math.abs(180-list.get(i));
				index = i;
			}
		}
		
		int rightIndex = index+1%list.size();
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

	@Override
	public boolean takeControl() {
		return Helper.lineIntersection(left, color, tolerance)&& Helper.lineIntersection(right, color, tolerance);
	}

}
