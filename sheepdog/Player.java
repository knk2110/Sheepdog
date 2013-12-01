package sheepdog.g8_final;

import sheepdog.sim.Point;

import java.util.*;

public class Player extends sheepdog.sim.Player {

    /*
    Possible improvements:

    If dogs >= xx then do wall strategy

    More zones, different configurations
    How to decide when to move dogs to different zones? Load balancing

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
        id = id - 1;
    }
    
    public Point move(Point[] dogs,
                      Point[] sheeps) {

        Point moveLocation;

        if (zones.isEmpty())
            zones = new ZoneConfig().getConfiguration(dogs.length);

        this.dogs = dogs;
        this.sheeps = sheeps;

        Point currentDogPoint = dogs[id];

        //move dogs through gate
        if (currentDogPoint.x < 50){
            return moveDogTowardGate(currentDogPoint);
        }

        for (int i = 0; i < dogs.length; i++) {
            int zoneID = i % (zones.size());
            dogToZone.put(i, zoneID);
        }

        moveLocation = getNextPositionBasedOnZone(id);
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

        // If there are more than 6 dogs, then there will be multiple dogs will be more than 6 dogs. Therefore we will be assigning
        // multiple dogs to a zone, and tier determines which sheep they target. Tier = 0 means that they target the farthest sheep in that zone,
        // tier = 1 means they will get the second farthest sheep, etc.
        int tier = (int) Math.floor(dogNum / zones.size());

        ArrayList<Integer> sortedSheep = getDistanceSortedIndices(myZone.getGoal(), myZone.getSheepIndices(this.sheeps));

        if (myZone.hasSheep(this.sheeps)) {
            if (Calculator.pointsEqual(myZone.goalPoint, Zone.GATE)) {
                return chaseSheepTowardGoal(dogNum, sortedSheep.get(tier), myZone.getGoal());
            } else {
                return chaseSheepTowardGoal(dogNum, sortedSheep.get(tier), myZone.getGoal());
            }
        } else { // The dog's zone is currently empty, reassign the dog's zone
            // Find the zone with the fewest number of sheep that is > 0 and move the dog to that location
            int zoneWithFewestSheep = 0;
            int minSheep = sheeps.length;

            for (int i = 0; i < zones.size(); i++) {
                int numSheep = zones.get(i).numSheep(sheeps);
                if (numSheep < minSheep && numSheep > 0) {
                    minSheep = numSheep;
                    zoneWithFewestSheep = i;
                }
            }


            if (Calculator.pointsEqual(zones.get(zoneWithFewestSheep).goalPoint, Zone.GATE)) {
                // if the dog is assigned to move to a goal zone, don't do it because it clogs it up
                return dogs[dogNum];
            } else {
                // resassign the dog's zone and move it towards the goal of the newly decided zone
                dogToZone.put(dogNum, zoneWithFewestSheep);
                return Calculator.getMoveTowardPoint(dogs[dogNum], zones.get(zoneWithFewestSheep).getGoal());
            }
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

    // Sorts the list of sheep based on their distance away from pt, farthest first
    protected ArrayList<Integer> getDistanceSortedIndices(final Point pt, ArrayList<Integer> sheepToCheck ) {
        Collections.sort(sheepToCheck, new Comparator<Integer>() {
            @Override
            public int compare(Integer arg0, Integer arg1) {
                return (int) Math.signum(Calculator.dist(sheeps[arg1], pt) - Calculator.dist(sheeps[arg0], pt));
            }
        });
        return sheepToCheck;
    }
}
