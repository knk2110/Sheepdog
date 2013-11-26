package sheepdog.g8_v8;

import sheepdog.sim.Point;
import java.util.ArrayList;

public class Player extends sheepdog.sim.Player {
    private int nblacks;
	private int numberOfTurns = 0;
    private boolean mode;
	private static Point[] sheepToMove; //will consist of just black sheep in advanced mode, all sheep in basic mode
	private static ArrayList<Zone> zones = new ArrayList<Zone>();
	private static ArrayList<Dog> myDogs = new ArrayList<Dog>();
	final static Point GATE = new Point(50,50);
	final static double MAX_SPEED = 1.80;

    public void init(int nblacks, boolean mode) {
        this.nblacks = nblacks;
        this.mode = mode;
    }
    
    // Return: the next position
    // my position: dogs[id-1]
    public Point move(Point[] dogs, // positions of dogs
                      Point[] sheeps) { // positions of the sheeps
        /*Point current = dogs[id-1];
        return current;*/
		
		//initialize dog players and zones only once
		if (numberOfTurns == 0){
			int numZones = dogs.length; 
		
			//if we have more than 7 dogs, just make 7 zones.
			if (dogs.length > 7){
				numZones = 7;
			}	
		
			//construct zones
			double zoneRadius = 50.0/(numZones-2);
			for (int i = 0; i < numZones; i++){
				if (i == 0){
					zones.add(new Zone(0, zoneRadius+5.0, false, false));
				}
				else if (i > 0 && i < (numZones-2)){
					zones.add(new Zone(zoneRadius*i-5.0, zoneRadius*(i+1)+5.0, false, false));
				}
				else if (i == (numZones-2)){//make top edge zone
					zones.add(new Zone(zoneRadius*i-5.0, Integer.MAX_VALUE, true, false));
				}
				else{	//final zone, make bottom edge zone
					zones.add(new Zone(zoneRadius*(i-1)-5.0, Integer.MAX_VALUE, false, true));
				}
			}
			
			//assign all dogs to a zone. first make my dogs
			for (int i = 0; i < dogs.length; i++){
				myDogs.add(new Dog(i, dogs[i]));
			}
		
			//now assign dogs to zone
			int dogCount = 0;
			while (dogCount < myDogs.size()){
				for (int i = zones.size()-1; i >=0&&dogCount< myDogs.size(); i--){
					myDogs.get(dogCount).setZone(i);
					dogCount++;
				}
			}
		
		}
		numberOfTurns++;
		/*end of initialization for turn 0*/
		
		if (mode == false){//basic mode
			sheepToMove = new Point[sheeps.length];
			for (int i = 0; i < sheeps.length; i++){
				sheepToMove[i] = sheeps[i];
			}
		}
		else{	//advanced mode
			sheepToMove = new Point[nblacks];
			for (int i = 0; i < nblacks; i++){
				sheepToMove[i] = sheeps[i];
			}
		}
		
		//reset dog points for new round
		for (int i = 0; i < myDogs.size(); i++){
			myDogs.get(i).setMyPoint(dogs[i]);
		}
		
		//check to see if there are sheep in each dog's zone. if not, reassign.
		for (int i = 0; i < myDogs.size(); i++){
			int currentZoneIndex = myDogs.get(i).getMyZoneNum();
			Zone currentZone = zones.get(currentZoneIndex);
			while (isEmpty(currentZone) && currentZoneIndex>0){
				currentZoneIndex -= 1;
				currentZone = zones.get(currentZoneIndex);
			}
		}

		System.out.println("FINAL DOG/ZONE PAIRINGS");
		for (int i = 0; i < myDogs.size(); i++){

			int j = i+1;
			System.out.println("dog #: " + j + " has zone " + myDogs.get(i).getMyZoneNum());
		}

		//now we need to actually make moves
		Dog currentDog = myDogs.get(id-1);
		
		//phase 0: move dogs from left side to gate
		if (currentDog.getHasMovedToGate()==false){
				return currentDog.moveTowardGate();
		}
		//if dogs are past gate, move to sheep in zone farthest from goal
		else if ((currentDog.getHasMovedToGate()==true) && (currentDog.getHasMovedToFirstZone()==false)){
				System.out.println("dog is moving toward zone");
				return currentDog.moveTowardZone(zones.get(currentDog.getMyZoneNum()));
		}
		else{	//get sheep closest to dog within the dog's zone and move it into the next zone
				System.out.println("dog is getting closest sheep");
				return currentDog.getMoveBasedOnZone(sheepToMove, zones.get(currentDog.getMyZoneNum()));
		}
	
	}
	
	public boolean isEmpty(Zone z){
		for (int i = 0; i < sheepToMove.length; i++){
			Point currentSheep = sheepToMove[i];
			if (z.isInZone(currentSheep))
				return false;
		}
		return true;
	}
	
	public static double computeDistance(Point x, Point y){
		return Math.sqrt(Math.pow((x.x-y.x),2)+Math.pow((x.y-y.y),2));
    }
	
}
