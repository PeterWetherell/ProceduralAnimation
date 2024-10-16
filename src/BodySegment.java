import java.awt.Color;
import java.awt.Graphics;

public class BodySegment {
	double radius;
	double maxCurvature; //minimum radius of the body segment in relation to others 
	Vector2d pose;
	public BodySegment() {
		radius = 10;
		pose = new Vector2d(0,0);
	}

	public BodySegment(double radius, Vector2d pose) {
		this.radius = radius;
		this.pose = pose;
	}
	
	public void update(BodySegment b) {
		Vector2d l = pose.subtract(b.pose);
		double scalar = radius/l.getMag();
		pose = b.pose.add(l.mult(scalar));
	}
	
	public void updateAngleConstraint(BodySegment b, double ang) {
		Vector2d l = pose.subtract(b.pose);
		pose = b.pose.add(l.rotate(ang));
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.drawOval((int)(pose.x-radius),(int)(pose.y-radius),(int)(2*radius),(int)(2*radius));
	}
}
