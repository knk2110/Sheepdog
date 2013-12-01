package sheepdog.g8;

import sheepdog.sim.Point;

import java.util.*;

public class Player extends sheepdog.sim.Player {
    private int nblacks;
    private boolean mode;

    private int tick;

    private int vertexNumber = 0;
    private boolean calculateDestination = true;

    private boolean calculateNewHull = true;
    private List<Point> convexHull = null;
    private Iterator<Integer> hullIterator = null;

    private LinkedHashMap<Integer, Point> hullSheepMap;

    private int chosenSheepIndex;
    private Point targetSheep;
    private Point destination;

    private boolean pickSheep = true;

    protected static final Point GAP = new Point(50, 50);
    protected static final Point CENTER_POINT = new Point(75, 50);

    public void init(int nblacks, boolean mode) {
        this.nblacks = nblacks;
        this.mode = mode;
    }
    
    // Return: the next position
    // my position: dogs[id-1]
    public Point move(Point[] dogs, // positions of dogs
                      Point[] sheeps) { // positions of the sheeps
        Point current = dogs[id-1];
        tick++;

        // Move dogs toward gate
        if (current.x < 50) {
            return Functions.getMoveTowardPoint(current, GAP);
        // Move sheep towards center
        } else if (tick > 1400) {
            if (pickSheep) {
                HashMap<Integer, Point> undeliveredSheep = Functions.undeliveredSheep(sheeps);

                double maxDistance = 500;
                int maxIndex = 0;

                ArrayList<Integer> sheepDistances = getDistanceSortedIndices(sheeps, current, new ArrayList<Integer>(undeliveredSheep.keySet()));

                if (id < sheepDistances.size()) {
                    chosenSheepIndex = sheepDistances.get(id);
                } else {
                    Point moveLocation = Functions.getMoveTowardPoint(current, new Point(100, 100));
                    return moveLocation;
                }
                pickSheep = false;
            }


            if (sheeps[chosenSheepIndex].x < 50.00) {
                pickSheep = true;
            }

            targetSheep = sheeps[chosenSheepIndex];
            targetSheep = anticipateSheepMovement(current, targetSheep);
            double angleGapToSheep = Functions.getAngleOfTrajectory(GAP, targetSheep);
            Point idealLocation = Functions.getMoveInDirection(targetSheep, angleGapToSheep, 1.0);
            Point moveLocation = Functions.getMoveTowardPoint(current, idealLocation);

            // make this more robust, crossing gate and distance check
            Functions.makePointValid(current, moveLocation);

            return moveLocation;

        } else {
            // The code below creates a mapping of sheep indices to positions. We use hullIterator to traverse the convex hull in order

            // after you finish hull, find the closest point to the hull
            if (calculateNewHull) {
                calculateNewHull = false;

                HashMap<Integer, Point> undeliveredSheep = Functions.undeliveredSheep(sheeps);
                hullSheepMap = new LinkedHashMap<Integer, Point>();

                convexHull = GrahamScan.getConvexHull(new ArrayList<Point>(undeliveredSheep.values()));

//                double closestDistance = 500;
//                Point closestPoint = null;

//                for (int i = 0; i < convexHull.size(); i++) {
//                    double distance = Functions.dist(convexHull.get(i), current);
//                    if (distance < closestDistance) {
//                        closestDistance = distance;
//                        closestPoint = convexHull.get(i);
//                    }
//                }
//
//                int rotateIndex = 0;
//                for (int i = 0; i < convexHull.size(); i++) {
//                    if (Functions.pointsEqual(closestPoint, convexHull.get(i))) {
//                        rotateIndex = i;
//                        break;
//                    }
//                }
//
                Random generator = new Random();
                Collections.rotate(convexHull, generator.nextInt(convexHull.size()));

                for (Point p: convexHull) {
                    for (int i = 0; i < sheeps.length; i++) {
                        if (Functions.pointsEqual(p, sheeps[i])) {
                            hullSheepMap.put(i, p);
                        }
                    }
                }

                hullIterator = hullSheepMap.keySet().iterator();
                chosenSheepIndex = hullIterator.next();
            }

            if (targetSheep!= null && Functions.arrivedAtDestination(targetSheep, destination)) {
                calculateDestination = true;

                if (hullIterator.hasNext()) {
                    chosenSheepIndex = hullIterator.next();
                } else {
                    calculateNewHull = true;
                }
            }

            targetSheep = sheeps[chosenSheepIndex];
            targetSheep = anticipateSheepMovement(current, targetSheep);

            if (calculateDestination) {
                double distanceToCenter = Functions.dist(CENTER_POINT, targetSheep);
                double pushDistance = 0.0;

                if (distanceToCenter > 20) {
                    pushDistance = 5;
                } else if (distanceToCenter > 10 && distanceToCenter < 20) {
                    pushDistance = 2;
                } else if (distanceToCenter < 10) {
                    pushDistance = .5;
                }

                destination = Functions.pointAlongLine(CENTER_POINT, targetSheep, pushDistance);
                calculateDestination = false;
            }


            double angleGapToSheep = Functions.getAngleOfTrajectory(destination, targetSheep);
            Point idealLocation = Functions.getMoveInDirection(targetSheep, angleGapToSheep, 1.0);
            Point moveLocation = Functions.getMoveTowardPoint(current, idealLocation);

            // make this more robust, crossing gate and distance check
            Functions.makePointValid(current, moveLocation);

            return moveLocation;
        }
    }

    protected ArrayList<Integer> getDistanceSortedIndices(final Point[] sheeps, final Point pt, ArrayList<Integer> sheepToCheck ) {
        Collections.sort(sheepToCheck, new Comparator<Integer>() {
            @Override
            public int compare(Integer arg0, Integer arg1) {
                return (int) Math.signum(Functions.dist(sheeps[arg0], pt) - Functions.dist(sheeps[arg1], pt));
            }

        });
        return sheepToCheck;
    }

    private Point anticipateSheepMovement(Point me, Point targetSheep) {
        double angleDogToSheep = Functions.getAngleOfTrajectory(me, targetSheep);
        if (Functions.withinRunDistance(targetSheep, me)) {
            targetSheep = Functions.getMoveInDirection(targetSheep, angleDogToSheep, 1.0);
        }
        else if (Functions.withinWalkDistance(targetSheep, me)) {
            targetSheep = Functions.getMoveInDirection(targetSheep, angleDogToSheep, 0.1);
        }
        return targetSheep;
    }
}
