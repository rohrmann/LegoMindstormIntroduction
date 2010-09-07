import lejos.nxt.*;
import lejos.robotics.navigation.*;
import java.util.Random;


/**
 * EchoNavigator is a obstacle avoiding  robot that attempts reach its destination.
 *  Uses SimpleNavigator
 * Hareware rquirements:   an ultrasonic sensor mounted on a vertical axle
 * driven by the  third motor.
 * Since it relies on dead reckoning to keep track of its
 * location, the accuracy of navigation degrades with each obstacle.  Does not
 * mep the obstacles, but uses a randomized avoiding strategy.
 * @author Roger
 */
public class EchoNavigator
{

	public SimpleNavigator navigator ;
	Random rand = new Random();
	UltrasonicSensor sonar;
	LightSensor light;
	private Motor scanner;
	int _limit =35; //cm
	final int obstacle = 35;
	
	public EchoNavigator(SimpleNavigator navigator, SensorPort echo,SensorPort lightPort, Motor scanMotor)
	{
		this.navigator = navigator;
		sonar= new UltrasonicSensor(echo);
		scanner = scanMotor ;
		light = new LightSensor(lightPort);
	}

	/**
	 * attempt to reach a destinaton at coordinates x,y despite obstacle.
	 * @param x coordinate of destination
	 * @param y coordinate of destination.
	 */

	public void goTo(float x, float y)
	{
		navigator.setMoveSpeed(20);
		navigator.setTurnSpeed(180);
		float destX = x;
		float destY = y;
		while (navigator.distanceTo(destX,destY) > 5)
		{
			navigator.goTo(destX, destY,true);
			int clear  = readDistance();
			if (clear!= 0) //  obstacle found
			{
				do{
					if(clear == 1)
						clear  = avoid();
					else if(clear == 2)
						clear = avoidGround();
					
					Thread.yield();  // keeps calling avoid until no obstacle is in view
				}while(clear != 0);
			}
		}
	}
	/**
	 * backs up, rotates away from the obstacle, and travels forward;
	 * returns true if no obstacle was discovered while traveling
	 * calls readSensor()
	 * @return
	 */
	private  int  avoid()
	{
		int leftDist = 0;
		int rightDist = 0;
		byte turnDirection = 1;
		boolean more = true;
		while(more)
		{
			scanner.rotateTo(75);
			Sound.pause(20);
			leftDist = sonar.getDistance();
			scanner.rotateTo(-70);
			Sound.pause(20);
			rightDist = sonar.getDistance();
			if(leftDist>rightDist) turnDirection = 1;
			else turnDirection = -1;
			more = leftDist < _limit && rightDist < _limit;
			if(more) navigator.travel(-4);
			LCD.drawInt(leftDist,4,0,5);
			LCD.drawInt(rightDist,4,8,5);
		}
		scanner.rotateTo(0);
		navigator.travel(-10 - rand.nextInt(10));
		int angle = 60+rand.nextInt(60);
		navigator.rotate(turnDirection * angle);
		navigator.travel(10 + rand.nextInt(60), true);
		return  readDistance ();  // watch for hit while moving forward
	}
	
	private int avoidGround(){
		
		int direction = rand.nextBoolean()?1:-1;
		navigator.travel(-10 - rand.nextInt(10));
		int angle = 60+rand.nextInt(60);
		navigator.rotate(direction * angle);
		navigator.travel(10 + rand.nextInt(60), true);
		return readDistance();
	}
	/**
	 * Monitors the ultrasonic sensor while the robot is moving.
	 * Returns if an obstacle is detected or if the robot stops
	 * @return false if obstacle was detected
	 */
	public int readDistance()
	{
		System.out.println(" Moving ");
		int distance = 255;
		int underground = obstacle+1;
		int clear = 0;
		while( navigator.isMoving()& distance > _limit  & underground > obstacle)
		{
			distance = sonar.getDistance();
			LCD.drawString("D "+distance, 0, 0);
			
			underground = light.getLightValue();
			LCD.drawString("L " + underground,0,1);

			if(underground <= obstacle)
				clear =2;
			if(distance <= _limit)
				clear =1;
			Thread.yield();
		}
		navigator.stop();
		return clear;
	}
	
	public static void main(String[] args)
	{
		System.out.println("Any Button");
		TachoPilot p = new TachoPilot(5.6f, 10.5f, Motor.A, Motor.C, false);
		EchoNavigator  robot  = new EchoNavigator( new SimpleNavigator(p), SensorPort.S1,SensorPort.S2, Motor.B);
		Button.waitForPress();
		robot.goTo(100,0);
	}


}