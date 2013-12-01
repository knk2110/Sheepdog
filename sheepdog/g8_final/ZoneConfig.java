package sheepdog.g8_final;

import sheepdog.sim.Point;

import java.util.ArrayList;
import java.util.Arrays;

public class ZoneConfig {

    final static double ZONE_H_LENGTH = 25.0;
    final static double ZONE_V_LENGTH = 33.333;

    final static double ZONE_GOAL_V_LENGTH = ZONE_V_LENGTH / 2;

    Zone Z1;
    Zone Z2;
    Zone Z3;
    Zone Z4;
    Zone Z5;
    Zone Z6;

    Zone ZGOAL_TOP;
    Zone ZGOAL_BOT;

    /*
    Zones are | 1 | 2 |
              | 3 | 4 |
              | 5 | 6 |
     */

    public ZoneConfig() {
        Point Z1_UL = new Point(50, 0);
        Point Z1_UL_GOAL= new Point(62.5, 35);

        Point Z2_UL = new Point(75, 0);
        Point Z2_UL_GOAL= new Point(87.5, 35);

        Point ZGOAL_TOP_UL = new Point(50, 33.333);
        Point ZGOAL_TOP_GOAL= new Point(50, 50); //The gap location

        Point ZGOAL_BOT_UL = new Point(50, 50);
        Point ZGOAL_BOT_GOAL= new Point(50, 50); //The gap location

        Point Z4_UL = new Point(75, 33.333);
        Point Z4_UL_GOAL= new Point(73, 50);

        Point Z5_UL = new Point(50, 66.666);
        Point Z5_UL_GOAL= new Point(62.5, 65);

        Point Z6_UL = new Point(75, 66.666);
        Point Z6_UL_GOAL= new Point(87.5, 65);

        this.Z1 = new Zone(Z1_UL, Z1_UL_GOAL, ZONE_H_LENGTH, ZONE_V_LENGTH);
        this.Z2 = new Zone(Z2_UL, Z2_UL_GOAL, ZONE_H_LENGTH, ZONE_V_LENGTH);
        this.ZGOAL_TOP = new Zone(ZGOAL_TOP_UL, ZGOAL_TOP_GOAL, ZONE_H_LENGTH, ZONE_GOAL_V_LENGTH);
        this.ZGOAL_BOT = new Zone(ZGOAL_BOT_UL, ZGOAL_BOT_GOAL, ZONE_H_LENGTH, ZONE_GOAL_V_LENGTH);
        this.Z3 = Zone.addZones(ZGOAL_TOP, ZGOAL_BOT);
        this.Z4 = new Zone(Z4_UL, Z4_UL_GOAL, ZONE_H_LENGTH, ZONE_V_LENGTH);
        this.Z5 = new Zone(Z5_UL, Z5_UL_GOAL, ZONE_H_LENGTH, ZONE_V_LENGTH);
        this.Z6 = new Zone(Z6_UL, Z6_UL_GOAL, ZONE_H_LENGTH, ZONE_V_LENGTH);
    }


    /**
     * Partitions the field into different zones depending on the number of dogs
     */
    public ArrayList<Zone> getConfiguration(int numDogs) {
        ArrayList<Zone> config = new ArrayList<Zone>();
        if (numDogs == 1) {
            config.add(Zone.addZones(Z1, Z2, Z3, Z4, Z5, Z6));
        } else if (numDogs == 2) {
            Zone leftHalf = Zone.addZones(Z1, Z3, Z5);
            Zone rightHalf = Zone.addZones(Z2, Z4, Z6);
            config.addAll(Arrays.asList(leftHalf, rightHalf));
        } else if (numDogs == 3) {
            Zone middle = Zone.addZones(Z3, Z4);
            Zone top = Zone.addZones(Z1, Z2);
            Zone bottom = Zone.addZones(Z5, Z6);
            config.addAll(Arrays.asList(middle, top, bottom));
        } else if (numDogs == 4 || numDogs == 5) {
            Zone top = Zone.addZones(Z1, Z2);
            Zone bottom = Zone.addZones(Z5, Z6);
            config.addAll(Arrays.asList(Z3, Z4, top, bottom));
        } else if (numDogs == 6) {
            config.addAll(Arrays.asList(Z3, Z4, Z1, Z2, Z5, Z6));
        } else if (numDogs >= 7) {
            config.addAll(Arrays.asList(ZGOAL_TOP, ZGOAL_BOT, Z4, Z1, Z2, Z5, Z6));
        }
        return config;
    }
}

