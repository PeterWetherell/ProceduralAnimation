
public class BodySegment {
	double radius;
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
}
