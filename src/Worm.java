import java.awt.Graphics;
import java.util.ArrayList;

public class Worm {
	ArrayList<BodySegment> segments;
	double maxAngle;
	
	public Worm(ArrayList<BodySegment> segments, double maxAngle) {
		this.segments = segments;
		this.maxAngle = maxAngle;
	}
	
	public Worm() {
		segments = new ArrayList<BodySegment>();
		maxAngle = Math.toRadians(5); //will never be used -> max is 180
		for (int i = 0; i < 100; i ++) {
			segments.add(new BodySegment(10, new Vector2d(i,0)));
		}
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
					segments.get(i).updateAngleConstraint(segments.get(i-1), rot);
				}
			}
		}
	}
	public void draw(Graphics g) {
		for (int i = 0; i < segments.size(); i ++) {
			BodySegment b = segments.get(i);
			g.drawOval((int)(b.pose.x-b.radius),(int)(b.pose.y-b.radius),(int)(2*b.radius),(int)(2*b.radius));
		}
	}
}
