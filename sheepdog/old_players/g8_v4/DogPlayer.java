package sheepdog.g8_v4;

import sheepdog.sim.Point;

public class DogPlayer {
    private int nblacks;
    private boolean mode;
    private static int phase = 0;
    private int firstDownSteps = 9;
    private int secondDownSteps = 9;
    private int leftSteps = 26;
    private int upSteps = 18;
    private int rightSteps = 26;
    private int firstDownCount = 0;
    private int secondDownCount = 0;
    private int leftCount = 0;
    private int upCount = 0;
    private int rightCount = 0;
    private static int dir = 0;
    private static int nextPhase = -1;
    private static int previousPhase = 0;
    private int totalLoops = 0; 
    private boolean dogsInPosition = false;
	private int dogID = 0;
	
	//note: dogIDs start at 0.
	public DogPlayer(int dI){
		dogID = dI;
	}
  
    
    // Return: the next position
    // my position: dogs[id-1]
    public Point getMoveForDog(Point[] dogs, // positions of dogs
                      Point[] sheeps) { // positions of the sheeps
    	Point current = dogs[dogID];
	System.out.println("Current phase: " + phase);
	System.out.println("FirstDownSteps count: " + firstDownSteps);
	System.out.println("FirstDownCount count: " + firstDownCount);
	
	/*	if(phase == -1){
		if (dogID == 1){
			if (current.y > 15){
				current.y -=2;
				if (current.y == 15 && dogs[2].y == 85)
					phase = 0;
			}
		}
		if (dogID == 3){
			if (current.y < 85){
				current.y +=2;
				if (current.y == 85 && dogs[0].y == 15)
					phase = 0;
			}

		}
		return current;
	}*/
	if (dogsInPosition == false && dogs[2].y < 85 && dogs[0].y > 15){
		if (dogID == 0)
			current.y -=2;
		else if (dogID == 2)
			current.y +=2;
		return current;
	}
	//phase 0: move dog to correct side of field
	if (phase == 0){
		dogsInPosition = true;
		if(current.x < 48){
			current.x+=2;
			return current;
		}
		else if (current.x>=48){
			current.x+=1;
			phase = 1;
			return current;
		}
	}
	//phase 1: move dog down
	else if (phase == 1)//move dog down
	{
		if(firstDownCount < firstDownSteps){
			current.y+=2;
			firstDownCount++;
			if (firstDownCount == firstDownSteps){
				phase = 2;
			}
			return current;
		}

	}
	//phase 2: move dog from L to R across bottom
	else if (phase == 2) //move dog right
	{
		if (rightCount < rightSteps){
			current.x+=2;
			rightCount++;
			if (rightCount == rightSteps){
				phase = 100;
				dir = 1;
				nextPhase = 3;
				rightCount = 0;
			}
			return current;
		}
	}
	
	else if (phase == 3) //move dog left
	{
		if (rightCount < rightSteps){
			current.x-=2;
			rightCount++;
			if (rightCount == rightSteps){
				phase = 100;
				dir = 1;
				nextPhase = 4;
				rightCount = 0;
			}
			return current;
		}
	}
	else if (phase == 4) //move dog right
	{
		if (rightCount < rightSteps){
			current.x+=2;
			rightCount++;
			if (rightCount == rightSteps){
				phase = 5;
				upSteps -=1;
			}
			return current;
		}
	}
	else if (phase == 5) //move dog up
	{
		if (upCount < upSteps){
		current.y -=2;
			upCount++;
			if (upCount == upSteps)
				phase = 6;
			return current;
		}
	}
	else if (phase == 6) //move dog left
        {
		if (leftCount < leftSteps){
			current.x -=2;
			leftCount++;
			if (leftCount == leftSteps)
				phase = 7;
			return current;
		}
	}
	else if (phase == 7) //move dog back to original spot
	{
		if (secondDownCount < secondDownSteps){
			current.y += 2;
			secondDownCount++;
			if (secondDownCount == secondDownSteps){
				totalLoops++;
				phase = 15;
				previousPhase = 7;
			}
			return current;
		}
	}
	else if (phase == 8){
		if (secondDownCount < secondDownSteps){
			current.y -=2;
			secondDownCount++;
			if (secondDownCount == secondDownSteps)
				phase = 9;
			return current;
		}
	}
	else if (phase == 9){
		if (leftCount < leftSteps){
			current.x +=2;
			leftCount++;
			if (leftCount == leftSteps){
				leftCount = 0;
				phase = 100;
				dir = -1;
				nextPhase = 10;
				
			}
			return current;
		}
	}
	else if (phase == 10){
		if (leftCount < leftSteps){
			current.x -=2;
			leftCount++;
			if (leftCount == leftSteps){
				leftCount = 0;
				phase = 100;
				dir = -1;
				nextPhase = 11;
				
			}
			return current;
		}
	}
	else if (phase == 11){
		if (leftCount < leftSteps){
			current.x +=2;
			leftCount++;
			if (leftCount == leftSteps){
				phase = 12;
				upCount -=1; 	
			}
			return current;
		}
	}
	else if (phase == 12){
		if (upCount < upSteps){
			current.y +=2;
			upCount++;
			if (upCount == upSteps)
				phase = 13;
			return current;
		}
	}
	else if (phase == 13){
		if (rightCount < rightSteps){
			current.x-=2;
			rightCount++;
			if (rightCount == rightSteps){
				phase = 14;
			}
		}
	}
	else if (phase == 14){
		if(firstDownCount < firstDownSteps){
			current.y-=2;
			firstDownCount++;
			if (firstDownCount == firstDownSteps){
				totalLoops++;
				phase = 15;
				previousPhase = 14;
			}
			return current;
		}
	}
	else if (phase == 15){
		firstDownCount = 0;
		secondDownCount = 0;
		leftCount = 0;
		rightCount = 0;
		upCount = 0;
		if(totalLoops % 2 == 0){
			firstDownSteps--;
			secondDownSteps--;
		}
		rightSteps--;
		leftSteps--;
		upSteps--;
		if (previousPhase == 7){
			current.y += 1;
			current.x += 1;
			phase = 8;
		}
		else{
			current.y -= 1;
			current.x+=1;
			phase = 1;
		}

		if (firstDownSteps < 6)
			phase = 21;
		//reverse = !reverse;
		return current;
	}
	else if (phase == 21){

		if (current.x > 50){
			current.x -= 2;
			return current;
		}
		else if (current.y < 99){
			current.y += 2;
			if (current.y >= 99)
				phase = 22;
			return current;			
		}

	}
	else if (phase == 22){ //have all dogs move to positions
		if (dogID == 0){
			if (dogs[2].x >= 100)
				phase = 23;
			return current;
		}
		else if (dogID == 1){
			if (current.x < 74)
				current.x += 2;
			else if (current.x == 74)
				current.x += 1;
			if (dogs[2].x >= 100)
				phase = 23;			
			return current;
		}
		else if (dogID == 2){
			if (current.x < 100){
				current.x += 2;
				if (current.x >= 100)
					phase = 23;
			}
		}

	}
	else if (phase == 23){
		if (current.y >60){
			current.y -= 2;
			if (current.y <=60)
				phase = 24;
			return current;
		}
		
	}
	
	else if (phase == 100){
		return moveOne(current);
	}

	//don't do anything if we don't meet the criteria
	System.out.println("Code shouldn't go here");
	return current;
    }
	//dir = 1 = move up, dir = -1 = move down
    public static  Point moveOne(Point current){
		if (dir == 1){
			current.y-=1;
		}
		else if (dir == -1){
			current.y += 1;
		}	
		phase = nextPhase;
		return current;
	}


}
