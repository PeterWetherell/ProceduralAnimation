import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Vertibrate {
	ArrayList<BodySegment> segments;
	double maxAngle;
	
	public Vertibrate(ArrayList<BodySegment> segments, double maxAngle) {
		this.segments = segments;
		this.maxAngle = maxAngle;
	}
	
	public Vertibrate() {
		segments = new ArrayList<BodySegment>();
		maxAngle = Math.toRadians(30); //will never be used -> max is 180
		int numSegments = 30;
		for (int i = 0; i < numSegments; i ++) {
			segments.add(new BodySegment(10+7*Math.sin(Math.PI * i/((double)numSegments)), new Vector2d(i,0)));
		}
		

		ArrayList<Double> lengths = new ArrayList<Double>();
		lengths.add(55.0);
		lengths.add(40.0);
		//add legs at index 7 and 17
		segments.set(7, new ArmSegment(segments.get(7), new Vector2d(-30,60), 70, lengths));
		segments.set(17, new ArmSegment(segments.get(17), new Vector2d(-30,60), 70, lengths));
	}
	
	public void setTarget(int x, int y) {
		segments.get(0).pose.x = x;
		segments.get(0).pose.y = y;
	}
	
	public void update() {
		for (int i = 1; i < segments.size(); i ++) {
			//Iterate over every segment avoiding the first one -> make them move into the right area
			segments.get(i).update(segments.get(i-1));
			if (i < segments.size()-1) {
				//Get the relative angle for the body
				double ang = segments.get(i-1).pose.getAngle(segments.get(i).pose) - segments.get(i).pose.getAngle(segments.get(i+1).pose);
				while (Math.abs(ang) > Math.PI) {
					ang -= Math.signum(ang) * Math.PI * 2;
				}
				if (Math.abs(ang) > maxAngle) { //This is checking that we have it bundled up weird
					double rot = Math.signum(ang)*(maxAngle - Math.abs(ang)); //this is the amount we would have to rotate it to not violate the angle constraints
					segments.get(i+1).updateAngleConstraint(segments.get(i), -rot);
				}
			}
		}
	}
	
	public void draw(Graphics g) {
		for (BodySegment b : segments) {
			b.draw(g);
		}
	}
}
