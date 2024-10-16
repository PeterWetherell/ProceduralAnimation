import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class ArmSegment extends BodySegment {
	Vector2d relativeArmPose;
	double stepDistance;
	Vector2d[] armPose = new Vector2d[2];
	Arm[] arms = new Arm[2];
	ArrayList<Double> lengths;
	
	public ArmSegment() {
		super();
		relativeArmPose = new Vector2d(0,20);
		stepDistance = 30;
		armPose[0] = new Vector2d(0,0);
		armPose[1] = new Vector2d(0,0);
		lengths = new ArrayList<Double>();
		lengths.add(20.0);
		lengths.add(30.0);
	}

	public ArmSegment(double radius, Vector2d pose, Vector2d relativeArmPose, double stepDistance, ArrayList<Double> lengths) {
		super(radius, pose);
		this.relativeArmPose = relativeArmPose;
		this.stepDistance = stepDistance;
		armPose[0] = new Vector2d(0,0);
		armPose[1] = new Vector2d(0,0);
		this.lengths = lengths;
		arms[0] = new Arm(pose, armPose[0], Math.PI, lengths);
		arms[1] = new Arm(pose, armPose[1], Math.PI, lengths);
	}

	public ArmSegment(BodySegment s, Vector2d relativeArmPose, double stepDistance, ArrayList<Double> lengths) {
		this(s.radius, s.pose, relativeArmPose, stepDistance, lengths);
	}
	
	@Override
	public void update(BodySegment b) {
		super.update(b);
		double ang = pose.getAngle(b.pose);
		for (int i = 0; i < 2; i ++) {
			Vector2d armPosition = pose.add(relativeArmPose.rotate(ang));
			// if the arm pose gets stepDistance units from its new target -> take step
			// if the arm pose is so far away from the base that it cannot reach -> take step
			if (armPose[i].subtract(armPosition).getMag() >= stepDistance || armPose[i].subtract(pose).getMag() >= arms[i].totalLength*0.9) { //take a step
				armPose[i] = armPosition;
				arms[i] = new Arm(pose, armPosition, ang + Math.signum(relativeArmPose.y)*Math.PI/2.0, lengths); //reset arms so they are facing straight outward
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
