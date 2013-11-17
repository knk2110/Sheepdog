package sheepdog.dumb;

import sheepdog.sim.Point;

public class Player extends sheepdog.sim.Player {
    private int nblacks;
    private boolean mode;
    private int phase = 0;
    private int firstDown = 99;
    private int secondDown = 50;
    private int right = 99;
    private int left = 51;
    private int up = 1;

    public void init(int nblacks, boolean mode) {
        this.nblacks = nblacks;
        this.mode = mode;
    }
    
    // Return: the next position
    // my position: dogs[id-1]
    public Point move(Point[] dogs, // positions of dogs
                      Point[] sheeps) { // positions of the sheeps
        Point current = dogs[id-1];
	if (phase == 0){
		if(current.x < 50){
			current.x+=2;
			return current;
		}
		else if (current.x<51){
			current.x+=1;
			phase = 1;
			return current;
		}
	}
	else if (phase == 1)//move dog down
	{
		if(current.y < firstDown){
			current.y+=2;
			if (current.y >= firstDown){
				phase = 2;
			}
			return current;
		}

	}
	else if (phase == 2) //move dog right
	{
		if (current.x < right){
			current.x+=2;
			if (current.x >= right)
				phase = 3;
			return current;
		}
	}
	else if (phase == 3) //move dog up
	{
		if (current.y > up){
			current.y -=2;
			if (current.y <= up)
				phase = 4;
			return current;
		}
	}
	else if (phase == 4) //move dog left
        {
		if (current.x > left){
			current.x -=2;
			if (current.x <= left)
				phase = 5;
			return current;
		}
	}
	else if (phase == 5) //move dog back to original spot
	{
		if (current.y < secondDown){
			current.y += 2;
			return current;
		}
		else if	(current.y >= secondDown){
			current.y += 2;
			current.x += 2;
			phase = 1;
			firstDown -= 2;
			secondDown -= 2;
			left += 2;
			up += 4;
			right -= 2;
			return current;
		}
	}
	//code should not get here
	System.out.println("Code should not get here");
	return current;
    }

}
