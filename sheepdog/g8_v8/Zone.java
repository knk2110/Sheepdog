package sheepdog.g8_v8;
import sheepdog.sim.Point;

public class Zone{

	private double innerRadius;
	private double outerRadius;
	private boolean isTopEdgeZone;
	private boolean isBottomEdgeZone;
	
	public Zone(double iR, double oR, boolean iTEZ, boolean iBEZ){
		innerRadius = iR;
		outerRadius = oR;
		isTopEdgeZone = iTEZ;
		isBottomEdgeZone = iBEZ;
	}
	
	public double getInnerRadius(){
		return innerRadius;
	}
	
	public double getOuterRadius(){
		return outerRadius;
	}
	
	public boolean isInnermostZone(){
		if (innerRadius == 0)
			return true;
		return false;
	}

	public boolean isTopEdgeZone(){
		return isTopEdgeZone;
	}

	public boolean isBottomEdgeZone(){
		return isBottomEdgeZone;
	}
			
	public boolean isInZone(Point p1){
		
		if (p1.x < 50) //if on the left side, always is not in zne
			return false;
		
		Point p = new Point (p1.x-50, p1.y-50); //accomodate offset
		double circleCoord = Math.sqrt(Math.pow(p.x,2) + Math.pow(p.y,2));
		//System.out.println("circleCoord: " + circleCoord + " x: " + p.x + " y: " + p.y);
		//special case #1: innermost zone
		if (innerRadius == 0){
			if (circleCoord <= outerRadius && p.x >= 0.0)
				return true;
			return false;
		}
		//special case #2: top edge zone
		else if (isTopEdgeZone){
			if (circleCoord > innerRadius && p.y < 50.0)
				return true;
			return false;		
		}
		//special case #3: bottom edge zone
		else if (isBottomEdgeZone){
			if (circleCoord > innerRadius && p.y >=50.0)
				return true;
			return false;
		}
		else{	//no special case
			if (circleCoord > innerRadius && circleCoord <= outerRadius)
				return true;
			return false;
		}
	
	}
	


}
