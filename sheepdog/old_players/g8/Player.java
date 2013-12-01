package sheepdog.g8;

import sheepdog.sim.Point;

import java.util.*;

public class Player extends sheepdog.sim.Player {
    private int nblacks;
    private boolean mode;

    private int vertexNumber = 0;
    private boolean pushingSheep = true;

    private boolean calculateNewHull = true;
    private List<Point> convexHull = null;

    protected static final Point GAP = new Point(50, 50);

    public void init(int nblacks, boolean mode) {
        this.nblacks = nblacks;
        this.mode = mode;
    }
    
    // Return: the next position
    // my position: dogs[id-1]
    public Point move(Point[] dogs, // positions of dogs
                      Point[] sheeps) { // positions of the sheeps
        Point current = dogs[id-1];

        // Move dogs toward gate
        if (current.x < 50) {
            return Functions.getMoveTowardPoint(current, GAP);
        // Move sheep towards center
        } else {
            if (calculateNewHull) {
                HashMap<Integer, Point> undeliveredSheep = Functions.undeliveredWhiteSheep(sheeps);
                convexHull = GrahamScan.getConvexHull(new ArrayList<Point>(undeliveredSheep.values()));
                calculateNewHull = false;
            }
            
           
            if (pushingSheep) {
                Point pointToMoveTo = Functions.getMoveTowardPoint(current, convexHull.get(vertexNumber));
                if (Functions.dist(current, pointToMoveTo) < 1) {
                    vertexNumber++;
                    if (vertexNumber > convexHull.size()) {
                        vertexNumber = 0;
                        calculateNewHull = true;
                    }
                }
                return pointToMoveTo;
            }
        }

        return current;
    }

}
