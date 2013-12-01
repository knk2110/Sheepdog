package sheepdog.g8_v10;

import sheepdog.sim.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Player extends sheepdog.sim.Player {

    /*
    Possible improvements:

    If dogs >= xx then do wall strategy

    Moving dogs into zones depending on how many sheep there: Dynamic Load Balancing
    More zones, different configurations
    Dynamic zones, changing zones depending on the sheep density


     */
    private int nblacks;
    private boolean mode;

    final static double MAX_SPEED = 1.99;//2.0;

    private Point[] dogs;
    private Point[] sheeps;

    ArrayList<Zone> zones= new ArrayList<Zone>();
    HashMap<Integer, Integer> dogToZone = new HashMap<Integer, Integer>();

    public void init(int nblacks, boolean mode) {
        this.nblacks = nblacks;
        this.mode = mode;
    }
    
    public Point move(Point[] dogs,
                      Point[] sheeps) {

        Point moveLocation;

        if (zones.isEmpty())
            zones = new ZoneConfig().getConfiguration(dogs.length);

        this.dogs = dogs;
        this.sheeps = sheeps;

        Point currentDogPoint = dogs[id-1];

        //move dogs through gate
        if (currentDogPoint.x < 50){
            return moveDogTowardGate(currentDogPoint);
        }

        for (int i = 0; i < dogs.length; i++) {
            int zoneID = i % (zones.size());
            dogToZone.put(i, zoneID);
        }

        moveLocation = getNextPositionBasedOnZone(id-1);
        makePointValid(currentDogPoint, moveLocation);

        return moveLocation;
    }

    public Point moveDogTowardGate(Point dogPoint) {
        double distanceFromGate = Calculator.dist(dogPoint, Zone.GATE);
        if (distanceFromGate<MAX_SPEED){
            dogPoint.x += distanceFromGate*(Zone.GATE.x-dogPoint.x)/distanceFromGate;
            dogPoint.y += distanceFromGate*(Zone.GATE.y-dogPoint.y)/distanceFromGate;
            return dogPoint;
        }
        dogPoint.x += MAX_SPEED*(Zone.GATE.x-dogPoint.x)/distanceFromGate;
        dogPoint.y += MAX_SPEED*(Zone.GATE.y-dogPoint.y)/distanceFromGate;
        return dogPoint;
    }

    public Point getNextPositionBasedOnZone(int dogNum){
        int zoneNumber = dogToZone.get(dogNum);
        Zone myZone = zones.get(zoneNumber);
		int idOfFarthestSheep = -1;
        int idOfClosestSheep = -1;

		double maxDistance = -1.0;
        double minDistance = 100.0;

        ArrayList<Integer> sortedSheep = getDistanceSortedIndices(this.sheeps, this.dogs[dogNum], myZone.getSheepIndices(this.sheeps));
//        Collections.reverse(sortedSheep); // MAX DISTANCE CURRENTLY DOES NOT WORK

        ArrayList<Integer> undeliveredBlackSheep = Calculator.undeliveredBlackSheep(this.sheeps, this.nblacks);
        // If a sheep is in a zone that's close to the gate, just deliver the sheep
        if (Calculator.pointsEqual(myZone.goalPoint, Zone.GATE)) {
            for (int i = 0; i < sheeps.length; i++) {
                Point currentSheep = sheeps[i];

                if (myZone.containsSheep(currentSheep)) {
                    if (this.mode) {
                        if (!undeliveredBlackSheep.contains(i)) {
                            continue;
                        }
                    }

                    double tmpDistance = Calculator.dist(currentSheep, Zone.GATE);
                    if (tmpDistance < minDistance) {
                        minDistance = tmpDistance;
                        idOfClosestSheep = i;
                    }
                }
            }
            if (idOfClosestSheep != -1) {
                return chaseSheepTowardGoal(dogNum, idOfClosestSheep, Zone.GATE);
            }
        }

		for (int i = 0; i < sheeps.length; i++){
			Point currentSheep = sheeps[i];
			if (myZone.containsSheep(currentSheep)){
				if (Calculator.dist(currentSheep, myZone.getGoal())>maxDistance){
					idOfFarthestSheep = i;
					maxDistance = Calculator.dist(currentSheep, myZone.getGoal());
				}
			}
		}

//        int sheepIndex = (int) Math.floor(dogNum / zones.size());
//        idOfFarthestSheep = sortedSheep.get(0);

		if (idOfFarthestSheep != -1) {
            return chaseSheepTowardGoal(dogNum, idOfFarthestSheep, myZone.getGoal());
        } else {
			System.out.println("marking zone: " + dogNum + " empty");
			zones.get(dogNum).setEmpty(true);
			return dogs[dogNum];
		}
    }

    public Point chaseSheepTowardGoal(int dogNum, int sheepNum, Point goal) {
        Point dogPoint = dogs[dogNum];
        Point sheepPoint = sheeps[sheepNum];
        sheepPoint = anticipateSheepMovement(dogPoint, sheepPoint);

        double angleGapToSheep = Calculator.getAngleOfTrajectory(goal, sheepPoint);
        Point idealLocation = Calculator.getMoveInDirection(sheepPoint, angleGapToSheep, 1.0);
        Point moveLocation = Calculator.getMoveTowardPoint(dogPoint, idealLocation);

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

    public static void makePointValid(Point current, Point destination) {
        // prevent crossing the fence
        if (current.x > 50.0 && destination.x < 50.0) {
            destination.x = 50.01;
        } else if (current.x < 50.0 && destination.x > 50.0) {
            destination.x = 49.99;
        }
        if (destination.x > 100) { destination.x = 100; }
        else if (destination.x < 0) { destination.x = 0; }
        if (destination.y > 100) { destination.y = 100; }
        else if (destination.y < 0) { destination.y = 0; }
    }

    protected ArrayList<Integer> getDistanceSortedIndices(final Point[] sheeps, final Point pt, ArrayList<Integer> sheepToCheck ) {
        Collections.sort(sheepToCheck, new Comparator<Integer>() {
            @Override
            public int compare(Integer arg0, Integer arg1) {
                return (int) Math.signum(Calculator.dist(sheeps[arg0], pt) - Calculator.dist(sheeps[arg1], pt));
            }
        });
        return sheepToCheck;
    }

//    public void reassignZones() {
//        ArrayList<Integer> emptyZones = new ArrayList<Integer>();
//        ArrayList<Integer> zonesThatNeedDogs = new ArrayList<Integer>();
//
//        for (int i = 0; i < zones.size(); i++) {
//            Zone z = zones.get(i);
//            if (z.hasNoSheep(this.sheeps)) {
//                emptyZones.add(i);
//            } else if (z.hasNoDogs(this.dogs) && )
//        }
//    }
}
    


