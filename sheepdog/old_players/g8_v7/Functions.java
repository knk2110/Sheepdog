package sheepdog.g8;

import java.util.ArrayList;
import java.util.HashMap;

import sheepdog.sim.Point;

public class Functions {
    public static final double RUN_DISTANCE = 2.0;
    public static final double WALK_DISTANCE = 10.0;
    public static final double FIELD_SIZE = 100.0;
    public static final double DOG_MAX_SPEED = 1.95;
    public static final double OPEN_LEFT = 49.0;
    public static final double OPEN_RIGHT = 51.0;

    public static final double EPSILON = 1e-6;

    public static double getAngleOfTrajectory(Point cur, Point dest) {
        return Math.atan2(dest.y - cur.y,dest.x - cur.x);
    }

    public static Point getMoveTowardPoint(Point pos, Point dest) {
        double angle = getAngleOfTrajectory(pos, dest);
        double dist;
        if (dist(pos, dest) < DOG_MAX_SPEED) {
            dist = dist(pos, dest);
        }
        else {
            dist = DOG_MAX_SPEED;
        }
        return getMoveInDirection(pos, angle, dist);
    }

    public static Point getMoveInDirection(Point pos, double angle, double distance) {
        double x = pos.x + Math.cos(angle) * distance;
        double y = pos.y + Math.sin(angle) * distance;
        return new Point(x, y);
    }

    // Given two points, this functions finds a point that is distance away from outerPoint
    // that is collinear with the two given points
    public static Point pointAlongLine(Point centerPoint, Point outerPoint, double distance) {
        double slope = (outerPoint.y - centerPoint.y) / (outerPoint.x - centerPoint.x);
        double x = 0.0;

        if (outerPoint.x > Player.CENTER_POINT.x) {
            x = outerPoint.x - distance/Math.sqrt(1 + Math.pow(slope, 2));
        } else {
            x = outerPoint.x + distance/Math.sqrt(1 + Math.pow(slope, 2));
        }

        double y = slope * (x - outerPoint.x) + outerPoint.y;

        return new Point(x, y);
    }

    public static boolean arrivedAtDestination(Point sheep, Point destination) {
        if (sheep.x > destination.x -.5 &&
            sheep.x < destination.x + .5 &&
            sheep.y > destination.y -.5 &&
            sheep.y < destination.y + .5) {
            return true;
        }
        return false;
    }

    public static double dist(Point p1, Point p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    public static boolean withinRunDistance(Point sheep, Point position) {
        return dist(sheep, position) < RUN_DISTANCE;
    }

    public static boolean withinWalkDistance(Point sheep, Point position) {
        return dist(sheep, position) < WALK_DISTANCE;
    }

    public static boolean pointsEqual(Point p1, Point p2) {
        if (Math.abs(p1.x - p2.x) < EPSILON && Math.abs(p1.y - p2.y) < EPSILON) {
            return true;
        }
        return false;
    }

    public static HashMap<Integer, Point> undeliveredSheep(Point[] sheep) {
        HashMap<Integer, Point> undelivered = new HashMap<Integer, Point>();
        for (int i = 0; i < sheep.length; i++) {
            if (sheep[i].x >= FIELD_SIZE * 0.5) {
                undelivered.put(i, sheep[i]);
            }
        }
        return undelivered;
    }

    public static void makePointValid(Point current, Point destination) {
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

}
