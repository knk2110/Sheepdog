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

    public static double dist(Point p1, Point p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    public static HashMap<Integer, Point> undeliveredWhiteSheep(Point[] sheep) {
        HashMap<Integer, Point> undelivered = new HashMap<Integer, Point>();
        for (int i = 0; i < sheep.length; i++) {
            if (sheep[i].x >= FIELD_SIZE * 0.5) {
                undelivered.put(i, sheep[i]);
            }
        }
        return undelivered;
    }

}
