package sheepdog.g8_v6;

import sheepdog.sim.Point;
import java.util.ArrayList;

public class Player extends sheepdog.sim.Player {
    private int nblacks;
    private boolean mode;
    static int count =0;
    static int sheep_id;
    static Point gate = new Point(50, 50);
    final static double MAX_SPEED = 1.99;//2.0;
    final static double ZONE_H_LENGTH = 25.0;
    final static double ZONE_V_LENGTH = 33.333;
    static Point zone1UL = new Point(50, 0);
    static Point zone1goal = new Point(62.5, 35);
    static Point zone2UL = new Point(75, 0);
    static Point zone2goal = new Point(93.75, 35);
    static Point zone3UL = new Point(75, ZONE_V_LENGTH);
    static Point zone3goal = new Point(75, 50);
    static Point zone4UL = new Point(50, 2*ZONE_V_LENGTH);
    static Point zone4goal = new Point(75, 65);
    static Point zone5UL = new Point(75, 2*ZONE_V_LENGTH);
    static Point zone5goal = new Point(62.5, 65);
    static Zone zone1 = new Zone(zone1UL, ZONE_H_LENGTH, ZONE_V_LENGTH, zone1goal);
    static Zone zone2 = new Zone(zone2UL, ZONE_H_LENGTH, ZONE_V_LENGTH, zone2goal);
    static Zone zone3 = new Zone(zone3UL, ZONE_H_LENGTH, ZONE_V_LENGTH, zone3goal);
    static Zone zone4 = new Zone(zone4UL, ZONE_H_LENGTH, ZONE_V_LENGTH, zone4goal);
    static Zone zone5 = new Zone(zone5UL, ZONE_H_LENGTH, ZONE_V_LENGTH, zone5goal);
    static ArrayList<Zone> zones = new ArrayList<Zone>();
    static int zoneAssignmentCount = 0;
    private Point[] dogs;
    private Point[] sheeps;
    private boolean dog1StartedFromGoal = false;
    private boolean dog2StartedFromGoal = false;
    private boolean dog3StartedFromGoal = false;
    private boolean dog4StartedFromGoal = false;
    private boolean dog5StartedFromGoal = false;

    public void init(int nblacks, boolean mode) {
        this.nblacks = nblacks;
        this.mode = mode;
	zones.add(zone1);
	zones.add(zone2);
	zones.add(zone3);
	zones.add(zone4);
	zones.add(zone5);

    
        
    }
    
    // Return: the next position
    // my position: dogs[id-1]
    public Point move(Point[] dogs, // positions of dogs
                      Point[] sheeps) { // positions of the sheeps
	
	this.dogs = dogs;
	this.sheeps = sheeps;


/*	System.out.println("zone 1 empty: " + zoneOneEmpty);
	System.out.println("zone 2 empty: " + zoneTwoEmpty);
	System.out.println("zone 3 empty: " + zoneThreeEmpty);
	System.out.println("zone 4 empty: " + zoneFourEmpty);
	System.out.println("zone 5 empty: " + zoneFiveEmpty);
*/	
	Point currentDogPoint = dogs[id-1];

	//move dogs through gate
	if (currentDogPoint.x < 50){
		return moveDogTowardGate(currentDogPoint);
	}
	for (int i = 0; i < zones.size();i++){
		System.out.println("zone " + i + " is " + zones.get(i).isEmpty());
	}
	boolean zone1Empty = true;
	boolean zone2Empty = true;
	boolean zone3Empty = true;
	boolean zone4Empty = true;
	boolean zone5Empty = true;	
	for (int i = 0; i < sheeps.length; i++){
		Point currentSheep = sheeps[i];
		if (zones.get(0).isInZone(currentSheep))
			zone1Empty = false;
		else if (zones.get(1).isInZone(currentSheep))
			zone2Empty = false;
		else if (zones.get(2).isInZone(currentSheep))
			zone3Empty = false;
		else if (zones.get(3).isInZone(currentSheep))
			zone4Empty = false;
		else if (zones.get(4).isInZone(currentSheep))
			zone5Empty = false;
	}
	
	//if no more sheep in the zones
	if (zone1Empty && zone2Empty&&zone3Empty&&zone4Empty&&zone5Empty){
		System.out.println("ALL ZONES EMPTY");
		return push(id); 	//happens to coordinate with zone		

	}
	//otherwise move sheep into the zones	
	if (dogs.length <= 5 && zoneAssignmentCount < dogs.length){
		zones.get(id-1).assignDog(id-1);
		zoneAssignmentCount++;
	}
	return getNextPositionBasedOnZone(id-1);
    }

    public Point moveDogTowardGate(Point dogPoint){
	double distanceFromGate = computeDistance(dogPoint, gate);
	if (distanceFromGate<MAX_SPEED){
		dogPoint.x += distanceFromGate*(gate.x-dogPoint.x)/distanceFromGate;
		dogPoint.y += distanceFromGate*(gate.y-dogPoint.y)/distanceFromGate;
		return dogPoint;
	}
	dogPoint.x += MAX_SPEED*(gate.x-dogPoint.x)/distanceFromGate;
	dogPoint.y += MAX_SPEED*(gate.y-dogPoint.y)/distanceFromGate;
	return dogPoint;
    }

    public double computeDistance(Point x, Point y){
	return Math.sqrt(Math.pow((x.x-y.x),2)+Math.pow((x.y-y.y),2));
	
    }

    public Point getNextPositionBasedOnZone(int dogNum){
	Zone myZone = zones.get(dogNum);
   //     if (!allSheepsOnGoal(myZone)){
		int idOfFarthestSheep = -1;
		double maxdistance = -1.00;
		int numSheepInZone = 0;
		System.out.println("zone " + dogNum + " sheeps: ");
		for (int i = 0; i < sheeps.length; i++){
			Point currentSheep = sheeps[i];
			if (myZone.isInZone(currentSheep)){
				System.out.println("sheep " + i + " at " + sheeps[i].x + "," + sheeps[i].y); 
				if (computeDistance(currentSheep, myZone.getGoal())>maxdistance){
					idOfFarthestSheep = i;
					maxdistance = computeDistance(currentSheep, myZone.getGoal());
				}
			}
		}
		if (idOfFarthestSheep != -1)
			return chaseSheepTowardGoal(dogNum, idOfFarthestSheep, myZone.getGoal());
		else{
	//		return dogs[dogNum];
			System.out.println("marking zone: " + dogNum + " empty");
			zones.get(dogNum).setEmpty(true);
			return dogs[dogNum];
		}
//	}
//	return dogs[dogNum];
    }

    public Point chaseSheepTowardGoal(int dogNum, int sheepnum, Point goal){
	Point dogPoint = dogs[dogNum];
	Point sheepPoint = sheeps[sheepnum];
	sheepPoint = anticipateSheepMovement(dogPoint, sheepPoint);		
        double angleGapToSheep = Calculator.getAngleOfTrajectory(goal, sheepPoint);
        Point idealLocation = Calculator.getMoveInDirection(sheepPoint, angleGapToSheep, 1.0);
        Point moveLocation = Calculator.getMoveTowardPoint(dogPoint, idealLocation);
        makePointValid(moveLocation);
        return moveLocation;
 	
   }

    private Point anticipateSheepMovement(Point me, Point targetSheep) {
        double angleDogToSheep = Calculator.getAngleOfTrajectory(me, targetSheep);
        if (Calculator.withinRunDistance(targetSheep, me)) {
            targetSheep = Calculator.getMoveInDirection(targetSheep, angleDogToSheep, 1.0/*SHEEP_RUN_SPEED*/);
        }
        else if (Calculator.withinWalkDistance(targetSheep, me)) {
            targetSheep = Calculator.getMoveInDirection(targetSheep, angleDogToSheep, .1 /*sheep walk*/);
        }
        return targetSheep;
    }

    public void makePointValid(Point pt) {
      	if (pt.x > 100) { pt.x = 100; }
      	else if (pt.x < 0) { pt.x = 0; }
      	if (pt.y > 100) { pt.y = 100; }
      	else if (pt.y < 0) { pt.y = 0; }
    }

    public Point push(int dogID){
	if (dogID == 1){
		Point myGoal = zone1goal;
		Point myPoint = dogs[dogID-1];
		if (dog1StartedFromGoal == false && computeDistance(dogs[dogID-1],myGoal)>0){
			if (computeDistance(myPoint,myGoal)<=MAX_SPEED){
				myPoint.x += computeDistance(myPoint, myGoal)*(myGoal.x-myPoint.x)/computeDistance(myPoint, myGoal);
				myPoint.y += computeDistance(myPoint, myGoal)*(myGoal.y-myPoint.y)/computeDistance(myPoint, myGoal);
				dog1StartedFromGoal = true;
				return myPoint;
			}
			myPoint.x += MAX_SPEED*(myGoal.x-myPoint.x)/computeDistance(myPoint, myGoal);
			myPoint.y += MAX_SPEED*(myGoal.y-myPoint.y)/computeDistance(myPoint, myGoal);
			return myPoint;
		}
		return new Point(dogs[dogID-1].x, dogs[dogID-1].y+1.99);
	}
	else if (dogID == 2){
		Point myGoal = zone2goal;
		Point myPoint = dogs[dogID-1];
		if (dog2StartedFromGoal == false && computeDistance(dogs[dogID-1],myGoal)>0){
			if (computeDistance(myPoint,myGoal)<=MAX_SPEED){
				myPoint.x += computeDistance(myPoint, myGoal)*(myGoal.x-myPoint.x)/computeDistance(myPoint, myGoal);
				myPoint.y += computeDistance(myPoint, myGoal)*(myGoal.y-myPoint.y)/computeDistance(myPoint, myGoal);
				dog2StartedFromGoal = true;
				return myPoint;
			}
			myPoint.x += MAX_SPEED*(myGoal.x-myPoint.x)/computeDistance(myPoint, myGoal);
			myPoint.y += MAX_SPEED*(myGoal.y-myPoint.y)/computeDistance(myPoint, myGoal);
			return myPoint;
		}
		myPoint.x-=Math.sqrt(1.99);
		myPoint.y += Math.sqrt(1.99);
		return myPoint;
	
	}
	else if (dogID == 3){
		Point myGoal = zone3goal;
		Point myPoint = dogs[dogID-1];
		if (dog3StartedFromGoal == false && computeDistance(dogs[dogID-1],myGoal)>0){
			if (computeDistance(myPoint, myGoal)<=MAX_SPEED){
				myPoint.x += computeDistance(myPoint, myGoal)*(myGoal.x-myPoint.x)/computeDistance(myPoint, myGoal);
				myPoint.y += computeDistance(myPoint, myGoal)*(myGoal.y-myPoint.y)/computeDistance(myPoint, myGoal);
				dog3StartedFromGoal= true;
				return myPoint;
			}
			myPoint.x += MAX_SPEED*(myGoal.x-myPoint.x)/computeDistance(myPoint, myGoal);
			myPoint.y += MAX_SPEED*(myGoal.y-myPoint.y)/computeDistance(myPoint, myGoal);
			return myPoint;
		}	
		myPoint.x -= 1.99;
		return myPoint;
	}
	else if (dogID == 4){
		Point myGoal = zone4goal;
		Point myPoint = dogs[dogID-1];
		if (dog4StartedFromGoal == false && computeDistance(dogs[dogID-1],myGoal)>0){
			if (computeDistance(myPoint, myGoal)<=MAX_SPEED){
				myPoint.x += computeDistance(myPoint, myGoal)*(myGoal.x-myPoint.x)/computeDistance(myPoint, myGoal);
				myPoint.y += computeDistance(myPoint, myGoal)*(myGoal.y-myPoint.y)/computeDistance(myPoint, myGoal);
				dog4StartedFromGoal= true;
				return myPoint;
			}
			myPoint.x += MAX_SPEED*(myGoal.x-myPoint.x)/computeDistance(myPoint, myGoal);
			myPoint.y += MAX_SPEED*(myGoal.y-myPoint.y)/computeDistance(myPoint, myGoal);
			return myPoint;
		}
		myPoint.x -= Math.sqrt(1.99);
		myPoint.y -= Math.sqrt(1.99);
		return myPoint;	
	
	}
	else{ //dogID = 5
		Point myGoal = zone5goal;
		Point myPoint = dogs[dogID-1];
		if (dog5StartedFromGoal == false && computeDistance(dogs[dogID-1],myGoal)>0){
			if (computeDistance(myPoint,myGoal)<=MAX_SPEED){
				myPoint.x += computeDistance(myPoint, myGoal)*(myGoal.x-myPoint.x)/computeDistance(myPoint, myGoal);
				myPoint.y += computeDistance(myPoint, myGoal)*(myGoal.y-myPoint.y)/computeDistance(myPoint, myGoal);
				dog5StartedFromGoal= true;
				return myPoint;
			}
			myPoint.x += MAX_SPEED*(myGoal.x-myPoint.x)/computeDistance(myPoint, myGoal);
			myPoint.y += MAX_SPEED*(myGoal.y-myPoint.y)/computeDistance(myPoint, myGoal);
			return myPoint;
		}
	
		return new Point(myPoint.x, myPoint.y-1.99);
	}

    }
 

   
}
    


