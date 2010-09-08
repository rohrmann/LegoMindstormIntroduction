import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;


public class Behaviors {
	public static void main(String[] args){
		Behavior[] behaviors = null;
		Arbitrator arbitrator = new Arbitrator(behaviors);
		
		arbitrator.start();
	}
}
