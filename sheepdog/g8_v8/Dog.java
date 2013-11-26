package sheepdog.g8_v8;
import sheepdog.sim.Point;

public class Dog{
	private int myZoneNum;
	private Point myPoint;
	private int myPosition;
	private boolean hasMovedToGate;
	private boolean hasMovedToFirstZone;
	
	
	public Dog(int pos, Point p){
		myPoint = p;
		myPosition = pos;
		myZoneNum = -1;
		hasMovedToGate = false;
		hasMovedToFirstZone = false;
	}
	
	public void setZone(int n){
		myZoneNum = n;
	}
		
	public boolean getHasMovedToGate(){
		return hasMovedToGate;
	}
	
	public boolean getHasMovedToFirstZone(){
		return hasMovedToFirstZone;
	}
	public int getMyZoneNum(){
		return myZoneNum;
	}
	
	public void setMyPoint(Point p){
		myPoint = p;
	}
	
	public Point getMyPoint(){
		return myPoint;
	}
	
	public Point moveTowardGate(){
		Point currentPoint = new Point(myPoint.x, myPoint.y);
		double distanceFromGate = Player.computeDistance(currentPoint, Player.GATE);
		if (distanceFromGate<=Player.MAX_SPEED){
			currentPoint.x += distanceFromGate*(Player.GATE.x-currentPoint.x)/distanceFromGate;
			currentPoint.y += distanceFromGate*(Player.GATE.y-currentPoint.y)/distanceFromGate;
			hasMovedToGate = true;
			return currentPoint;
		}
		currentPoint.x += Player.MAX_SPEED*(Player.GATE.x-currentPoint.x)/distanceFromGate;
		currentPoint.y += Player.MAX_SPEED*(Player.GATE.y-currentPoint.y)/distanceFromGate;
		return currentPoint;
	}
	
	public Point moveTowardZone(Zone z){
		Point zonePoint = new Point(-1, -1);
		Point currentPoint = new Point(myPoint.x, myPoint.y);
		if (z.isTopEdgeZone()){
			zonePoint = new Point(100,0);
		}
		else if (z.isBottomEdgeZone()){
			zonePoint = new Point(100,100);
		}
		else{//normal zone
			double zoneOuterRadius = z.getOuterRadius();
			zonePoint = new Point(50.0+zoneOuterRadius,50);
		}
		
		double distanceFromZone = Player.computeDistance(currentPoint, zonePoint);
		if (distanceFromZone <= Player.MAX_SPEED){
			currentPoint.x += distanceFromZone*(zonePoint.x-currentPoint.x)/distanceFromZone;
			currentPoint.y += distanceFromZone*(zonePoint.y-currentPoint.y)/distanceFromZone;
			hasMovedToFirstZone = true;
			Utils.makePointValid(currentPoint);
			return currentPoint;
		}
		currentPoint.x += Player.MAX_SPEED*(zonePoint.x - currentPoint.x)/distanceFromZone;
		currentPoint.y += Player.MAX_SPEED*(zonePoint.y - currentPoint.y)/distanceFromZone;
		Utils.makePointValid(currentPoint);
		if(currentPoint == zonePoint)
			hasMovedToFirstZone = true;
		return currentPoint;
	}
	
	public Point getMoveBasedOnZone(Point[] sheepToMove, Zone z){

		int idOfClosestSheep = -1;
		double mindistance = 100.00;
		int numSheepInZone = 0;
		//System.out.println("zone " + dogNum + " sheeps: ");
		for (int i = 0; i < sheepToMove.length; i++){
			Point currentSheep = sheepToMove[i];
			if (z.isInZone(currentSheep)){
				//System.out.println("sheep " + i + " at " + sheeps[i].x + "," + sheeps[i].y); 
				if (Player.computeDistance(currentSheep, myPoint)<mindistance){
					idOfClosestSheep = i;
					mindistance = Player.computeDistance(currentSheep, myPoint);
				}
			}
		}
		if (idOfClosestSheep != -1){
			if (z.getInnerRadius()!=0){
				Point goalPoint = new Point(-1, -1);
				if(z.isTopEdgeZone()){
					goalPoint = new Point(50.0+z.getInnerRadius()-5,50);	
				}
				else if (z.isBottomEdgeZone()){
					goalPoint = new Point(50.0+z.getInnerRadius()-5,50);
				}
				else {
					goalPoint = new Point(50.0+z.getInnerRadius()-5,50); //TODO: fix
				}
				return chaseSheepTowardGoal(idOfClosestSheep, sheepToMove, goalPoint);
			}
			else{
				return chaseSheepTowardGoal(idOfClosestSheep, sheepToMove, Player.GATE);
			}
		}	
		else{
			System.out.println("no sheep in range");
			return myPoint;
		}
	}
	
	public Point chaseSheepTowardGoal(int sheepID, Point[] sheepToMove, Point goalPoint){
		Point sheepPoint = sheepToMove[sheepID];
		sheepPoint = anticipateSheepMovement(sheepPoint);		
        	double angleGapToSheep = Utils.getAngleOfTrajectory(goalPoint, sheepPoint);
        	Point idealLocation = Utils.getMoveInDirection(sheepPoint, angleGapToSheep, 1.0);
        	Point moveLocation = Utils.getMoveTowardPoint(myPoint, idealLocation);
        	Utils.makePointValid(moveLocation);
        	return moveLocation;
	}
	
	public Point anticipateSheepMovement(Point targetSheep) {
        	double angleDogToSheep = Utils.getAngleOfTrajectory(myPoint, targetSheep);
        	if (Utils.withinRunDistance(targetSheep, myPoint)) {
            		targetSheep = Utils.getMoveInDirection(targetSheep, angleDogToSheep, 1.0/*SHEEP_RUN_SPEED*/);
        	}
        	else if (Utils.withinWalkDistance(targetSheep, myPoint)) {
            		targetSheep = Utils.getMoveInDirection(targetSheep, angleDogToSheep, .1 /*sheep walk*/);
        	}
        	return targetSheep;
    	}

}
