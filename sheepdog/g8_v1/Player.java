package sheepdog.g8_v1;

import sheepdog.sim.Point;

public class Player extends sheepdog.sim.Player {
    private int nblacks;
    private boolean mode;
    private boolean flag= false;
    private int vertical_radius = 12;
    private int horizontal_radius = 24;
    
    //a and b are the center of the ellipsis
    private int a=75;
    private int b=0;

    //counts for "rounding up" the sheep
    private int count = 0;
    private int circle_count = 0;

    private int max=0;
    private int phase=0;
    private boolean firstMove = true;

    public void init(int nblacks, boolean mode) {
        this.nblacks = nblacks;
        this.mode = mode;
        
        
    }
    
    // Return: the next position
    // my position: dogs[id-1]
    public Point move(Point[] dogs, // positions of dogs
                      Point[] sheeps) { // positions of the sheeps
    	

	Point current = dogs[id-1];
        int[] sheepCounts = new int[4];
	
	if (phase == 0) //move dog to the opening of the gate.
	{
		if (firstMove) //if it's the first round of the game, compute number of sheep in each section
		{
    	   		for(int i = 0; i < sheeps.length; i++)
      			{
      				Point p = sheeps[i];
      				if (p.x<=100 && p.x>=50){
      					if (p.y<25&&p.y>0){
      						sheepCounts[0]++;
      					}
      					else if (p.y>=25 && p.y<50){
      						sheepCounts[1]++;
      					}
      					else if(p.y>=50 && p.y<75){
      						sheepCounts[2]++;
      					}	
      					else{
      						sheepCounts[3]++;
      					}
      				}
      			}	
      	
			//find section with the most sheep
    			max = 0;	
      			for (int j=0;j<4;j++)
      			{
      				System.out.println("Value"+sheepCounts[j]);
      				if(sheepCounts[j]>sheepCounts[max])
      				{
      					max=j;
      				}	
      			}
			firstMove = false; 
		}
			
		
	
		//now, move dog to center of the other side of the field
		if(current.x < 74){
			current.x+=2;
			//if we get to 50, go to next phase
			if (current.x >= 74){
				phase = 1;
			}
			return current;			
		}
	}
	else if (phase == 1){ //now, move dog to the center of the section with the most sheep.
  		if(max==0)
              	{	
              		while(current.y>15)
              		{
              			current.y-=2;
              			b=13;
				if (current.y <=15)
					phase = 2;
              			return current;
              		}
              	}
		else if (max==1)
              	{	
              		if(current.y>39)
              		{
              			current.y-=2;
              			b=37;
				if (current.y <=39)
					phase = 2;
              			return current;
              		}
              	}
              	
              	else if(max==2)
              	{	
              		if(current.y<60)
              		{
              			current.y+=2;
              			b=62;
				if (current.y>=60)
					phase = 2;
              			return current;
              		}
              	}
              	
              	else if(max==3)
              	{	
              		if(current.y<85)
              		{
              			current.y+=2;
              			b=87;
				if (current.y>=85)
					phase = 2;
              			return current;
              		}
              	}
 
	}
	else if (phase == 2) { //circle the dogs in this section based on the dimensions of the section and an ellipsis shape
		b=(int)current.y;
          	if(count<32 && circle_count<10)
          	{	
          		double t = 2 * Math.PI * count/32;
          		current.y= b + vertical_radius*Math.sin(t);
          		current.x= a + horizontal_radius*Math.cos(t);
          		count++;
          		if (count==32)
          		{
          			count=0;
          			circle_count++;
          			if (circle_count%2==0 && circle_count!=0)
          			{
          				vertical_radius-=2;
          				horizontal_radius-=2;
          			}
          		}
			if (circle_count==10 && count==32)
				phase = 3;
          		return current;
          	}
	}



	//the code should never get here, but if we have to return something and no conditions are met, don't move	
        return current;

    }

}
