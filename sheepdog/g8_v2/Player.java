package sheepdog.dumb1;

import sheepdog.sim.Point;

public  class Player extends sheepdog.sim.Player {
    public int id; // id of the dog, 1,2,3...ndog
    static boolean flag=false;
    static int count = 0;
    public Player() {}
    
    public void init(int nblacks, boolean mode){
    
    }
    
    // Return: the next position
    // my position: dogs[id-1]
    public Point move(Point[] dogs, // positions of dogs
                               Point[] sheeps){ // positions of the sheeps
    
    	Point current = dogs[0];
    	int indexOfClosestSheep = 0;
    	//move dog to center on the other side
    	if (flag == false && current.x < 73){
    		current.x=current.x+2;
    		return current;
    	}
    	else if (current.x>=73)
    	{
    		flag=true;
    	}
    
    	if (flag == true && count == 0){
    		double mindistance = 0.0;
    		
    		for (int i = 0; i < sheeps.length; i++){
    			Point sheep = sheeps[i];
    			if(i==0)
    				mindistance = Math.sqrt(Math.pow(Math.abs(current.x-sheep.x),2)+Math.pow(Math.abs(current.y-sheep.y), 2));
    			else{
    				double newdistance = Math.sqrt(Math.pow(Math.abs(current.x-sheep.x),2)+Math.pow(Math.abs(current.y-sheep.y), 2));
    				if (newdistance < mindistance){
    					mindistance = newdistance;
    					indexOfClosestSheep = i;
    				}
    			}
    		}
    		count = 1;
    	}
    	Point currentSheep = sheeps[indexOfClosestSheep];
    	if(sheeps[indexOfClosestSheep].y==50){
    		while(current.x<=currentSheep.x){
    			current.x+=2;
    			return current;
    		}
    	}
    	else if (sheeps[indexOfClosestSheep].y>50)
    	{
    		while(current.x<=currentSheep.x){
    			current.x+=2;
    			return current;
    		}
    		if(current.x != currentSheep.x){
    			current.x = currentSheep.x;
    			return current;
    		}
    		
    		while(current.y<=currentSheep.y+4){
    			current.y+=2;
    			return current;
    		}
    	}
    	else if (sheeps[indexOfClosestSheep].y<50){
    		while(current.x<=currentSheep.x){
    			current.x+=2;
    			return current;
    		}
    		if(current.x != currentSheep.x){
    			current.x = currentSheep.x;
    			return current;
    		}
    		while(current.y<=currentSheep.y-4){
    			current.y+=2;
    			return current;
    		}
    	}
    	
    	return current;
    	
    
    }

}
