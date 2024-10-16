import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class ArmSegment extends BodySegment {
	Vector2d relativeArmPose;
	double stepDistance;
	ArrayList<Double> lengths;
	
	public enum armState{step, pivot};
	armState[] armStates = new armState[2];
	Interpolator[] interpolator = new Interpolator[2];
	Vector2d[] armPose = new Vector2d[2];
	Arm[] arms = new Arm[2];
	
	public ArmSegment() {
		super();
		relativeArmPose = new Vector2d(0,20);
		stepDistance = 30;
		lengths = new ArrayList<Double>();
		lengths.add(20.0);
		lengths.add(30.0);
		for (int i = 0; i < 2; i ++) {
			armStates[i] = armState.pivot;
			armPose[i] = new Vector2d(0,0);
			arms[i] = new Arm(pose, armPose[1], Math.PI, lengths);
			interpolator[i] = new Interpolator();
		}
	}

	public ArmSegment(double radius, Vector2d pose, Vector2d relativeArmPose, double stepDistance, ArrayList<Double> lengths) {
		super(radius, pose);
		this.relativeArmPose = relativeArmPose;
		this.stepDistance = stepDistance;
		this.lengths = lengths;
		for (int i = 0; i < 2; i ++) {
			armStates[i] = armState.pivot;
			armPose[i] = new Vector2d(0,0);
			arms[i] = new Arm(pose, armPose[1], Math.PI, lengths);
			interpolator[i] = new Interpolator();
		}
	}

	public ArmSegment(BodySegment s, Vector2d relativeArmPose, double stepDistance, ArrayList<Double> lengths) {
		this(s.radius, s.pose, relativeArmPose, stepDistance, lengths);
	}
	
	@Override
	public void update(BodySegment b) {
		super.update(b);
		double ang = pose.getAngle(b.pose);
		for (int i = 0; i < 2; i ++) {
			switch (armStates[i]) {
			case pivot: //When our foot is still we need to determine if we need to take the next step
				Vector2d armPosition = pose.add(relativeArmPose.rotate(ang));
				// if the arm pose gets stepDistance units from its new target -> take step
				// if the arm pose is so far away from the base that it cannot reach -> take step
				if (armPose[i].subtract(armPosition).getMag() >= stepDistance || armPose[i].subtract(pose).getMag() >= arms[i].totalLength*0.9) { //take a step
					//get the current relative arm pose
					Vector2d previousRelativeArmPose = (armPose[i].subtract(pose)).rotate(-ang);
					//The target that we are going for is relativeArmPose
					
					//Set new target for the interpolater to go from previousRelativeArmPose to relativeArmPose (default)
					interpolator[i].setConditions(previousRelativeArmPose, relativeArmPose);
					armStates[i] = armState.step;
				}
				break;
			case step: //Linearly interpolate the relative armPosition so that we don't have a foot that teleports
				armPose[i] = pose.add((interpolator[i].getPose()).rotate(ang));
				if (interpolator[i].finished()) {
					armStates[i] = armState.pivot;
				}
				break;
			}
			relativeArmPose.y *= -1;
			arms[i].update(pose, armPose[i]);
		}
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		for (int i = 0; i < 2; i ++) {
			//Draw the arms
			g.setColor(Color.red);
			g.drawOval((int)(armPose[i].x-5),(int)(armPose[i].y-5),10,10);
			
			if (arms[i] != null) {
				arms[i].draw(g);
			}
		}
	}
}
