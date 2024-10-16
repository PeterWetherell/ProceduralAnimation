import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Arm {
	ArrayList<BodySegment> arm = new ArrayList<BodySegment>();
	double totalLength = 0;
	public Arm (Vector2d start, Vector2d end, double theta, ArrayList<Double> length) {
		arm.clear();
		arm.add(new BodySegment(length.get(0), new Vector2d(start.x, start.y)));
		totalLength = length.get(0);
		for(int i = 1; i < length.size(); i ++) {
			totalLength += length.get(i);
			arm.add(new BodySegment(length.get(i), arm.get(i-1).pose.add(new Vector2d(length.get(i-1)*Math.cos(theta), length.get(i-1)*Math.sin(theta)))));
		}
		arm.add(new BodySegment(length.get(length.size()-1), arm.get(arm.size()-1).pose.add(new Vector2d(length.get(length.size()-1)*Math.cos(theta), length.get(length.size()-1)*Math.sin(theta)))));
	}
	
	public void update(Vector2d start, Vector2d end) {
		//System.out.println(start.x + " " + start.y + ", " + end.x + " " + end.y);
		int size = arm.size();
		if (size == 0) {
			return;
		}
		for (int i = 0; i < 5; i ++) { //5 forward backward passes
			//backward pass
			arm.get(size-1).pose = end.copy();
			for (int j = size-1; j > 0; j --) {
				arm.get(j-1).update(arm.get(j));
			}
			
			//forward pass
			arm.get(0).pose = start.copy();
			for (int j = 1; j < size; j ++) {
				arm.get(j).update(arm.get(j-1));
			}
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.blue);
		for (int i = 0; i < arm.size()-1; i ++) {
			g.drawLine((int)arm.get(i).pose.x, (int)arm.get(i).pose.y, (int)arm.get(i+1).pose.x, (int)arm.get(i+1).pose.y);
		}
	}
}
