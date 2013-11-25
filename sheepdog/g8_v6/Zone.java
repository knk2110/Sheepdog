package sheepdog.g8_v6;
import sheepdog.sim.Point;

public class Zone{

	private Point upperLeft;
	private Point upperRight;
	private Point lowerLeft;
	private Point lowerRight;
	private double horizontalLength;
	private double verticalLength;
	private Point goalPoint;
	private boolean dogAssigned = false;
	private int dogAssignment = -1;
	private boolean isEmpty;
	public Zone(Point upperL, Point upperR, Point lowerL, Point lowerR){
		upperLeft = upperL;
		lowerLeft = lowerL;
		lowerRight = lowerR;
		upperRight = upperR;
		horizontalLength = upperRight.x - upperLeft.x;
		verticalLength = upperRight.y - lowerRight.y;
		isEmpty = false;
	}

	public Zone(Point upperL, double w, double h, Point gP){
		horizontalLength = w;
		verticalLength = h;
		upperLeft = upperL;
		upperRight = new Point(upperL.x+horizontalLength, upperL.y);
		lowerLeft = new Point(upperL.x, upperL.y+verticalLength);
		lowerRight = new Point(upperL.x+horizontalLength, upperL.y+verticalLength);
		goalPoint = gP;
		isEmpty = false;
				
	}

	public Point getUpperRight(){
		return upperRight;
	}

	public Point getUpperLeft(){
		return upperLeft;
	}

	public Point getLowerLeft(){
		return lowerLeft;
	}

	public Point getLowerRight(){
		return lowerRight;
	}

	public double getHorizontalLength(){
		return horizontalLength;
	}

	public double getVerticalLength(){
		return verticalLength;
	}

	public Point getGoal(){
		return goalPoint;
	}

	public void assignDog(int dogPosition){
		dogAssignment = dogPosition;
		dogAssigned = true;
	}

	public boolean isDogAssigned(){
		return dogAssigned;
	}

	public int getDogAssignment(){
		return dogAssignment;
	}
	
	public boolean isInZone(Point p){
		if (p.x >= upperLeft.x && p.x <= upperRight.x && p.y >= upperRight.y && p.y <= lowerRight.y)
			return true;
		return false;
	}

	public boolean isEmpty(){
		return isEmpty;
	}
	
	public void setEmpty(boolean b){
		isEmpty = b;
	}

}
