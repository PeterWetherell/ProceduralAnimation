
public class Interpolator {
	long lastCallTime;
	Vector2d target, currPose;
	double accel, decel, maxSpeed, minSpeed;
	double currSpeed;
	
	public Interpolator() {
		currPose = new Vector2d(0,0);
		target = new Vector2d(0,0);
		lastCallTime = System.nanoTime();
		minSpeed = 50;
		maxSpeed = 500;
		accel = 2000;
		decel = 4000;
		currSpeed = minSpeed; //pixels per second
	}
	
	public void setConditions(Vector2d initial, Vector2d target) {
		this.currPose = initial;
		this.target = target;
		lastCallTime = System.nanoTime();
	}
	
	public void update() {
		long currTime = System.nanoTime();
		double loopTime = (currTime-lastCallTime)/1.0e9;
		lastCallTime = currTime;
		
		Vector2d path = target.subtract(currPose);
		double dist = path.getMag();
		
		//Time it would take to decel * Average speed /2 = distance traveled while decelerating (trapezoid formula)
		if ((currSpeed-minSpeed)/decel * (currSpeed + minSpeed) / 2.0 > dist) { //See if we need to start decelerating
			currSpeed -= decel*loopTime; //Make sure we decelerate into the end
			if (currSpeed < minSpeed) {
				currSpeed = minSpeed;
			}
		}
		else {
			currSpeed += accel*loopTime; //Try to accelerate for the longest possible time
			if (currSpeed > maxSpeed) {
				currSpeed = maxSpeed;
			}
		}
		
		
		double distTraveled = currSpeed * loopTime; // we move this amount of distance
		if (distTraveled > dist ) { //don't overshoot the target
			distTraveled = dist;
		}
		
		currPose = currPose.add(path.mult(distTraveled/dist)); //add the % of the path that we just traveled
		
		if (finished()) { //reset speed when done
			currSpeed = minSpeed;
		}
	}
	
	public Vector2d getPose() {
		update();
		return currPose;
	}
	
	public boolean finished() { //Close enough to the end -> call it good enough
		return currPose.subtract(target).getMag() < 1;
	}

}
