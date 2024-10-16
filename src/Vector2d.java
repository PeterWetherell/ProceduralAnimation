
public class Vector2d {
	public double x,y;
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2d subtract(Vector2d v) {
		return new Vector2d(x-v.x,y-v.y);
	}
	
	public Vector2d add(Vector2d v) {
		return new Vector2d(x+v.x,y+v.y);
	}
	public Vector2d mult(double m) {
		return new Vector2d(x*m,y*m);
	}
	
	public double getAngle(Vector2d v) {
		return Math.atan2(y-v.y,x-v.x);
	}
	
	public double getMag() {
		return Math.sqrt(Math.pow(y,2)+Math.pow(x,2));
	}
	
	public Vector2d rotate(double ang) {
		return new Vector2d(x*Math.cos(ang) - y*Math.sin(ang), x*Math.sin(ang) + y*Math.cos(ang));
	}
	
	public Vector2d copy() {
		return new Vector2d(x, y);
	}
	
}
