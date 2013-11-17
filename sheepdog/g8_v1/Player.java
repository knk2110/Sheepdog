package sheepdog.dumb;

import sheepdog.sim.Point;

public class Player extends sheepdog.sim.Player {
    private int nblacks;
    private boolean mode;
    static boolean flag= false;
    static int count =0;
    static int circle_count=0;
    private int quandrant=0;
    static int identity=0;
    static int identity1=0;
    int radius = 12;
    int radius1 =24;
    int a=75;
    int b=0;
    static int max=0;
    public void init(int nblacks, boolean mode) {
        this.nblacks = nblacks;
        this.mode = mode;
        
        
    }
    
    // Return: the next position
    // my position: dogs[id-1]
    public Point move(Point[] dogs, // positions of dogs
                      Point[] sheeps) { // positions of the sheeps
        identity++;
    	Point current = dogs[id-1];
        //Point sheep_pos = sheeps[];
        int[] coord1sheeps = new int[4];
        
        
        if(identity==1)
      	{
      	
      	for(int i = 0; i < sheeps.length; i++)
      	{
      		Point p = sheeps[i];
      		if (p.x<=100 && p.x>=50){
      			if (p.y<25&&p.y>0){
      				coord1sheeps[0]++;
      			}
      			else if (p.y>=25 && p.y<50){
      				coord1sheeps[1]++;
      			}
      			else if(p.y>=50 && p.y<75){
      				coord1sheeps[2]++;
      			}
      			else{
      				coord1sheeps[3]++;
      			}
      		}
      	}
      	
    	max=0;
      	for (int j=0;j<4;j++)
      	{
      		System.out.println("Value"+coord1sheeps[j]);
      		if(coord1sheeps[j]>coord1sheeps[max])
      		{
      			max=j;
      		}
      	}
      	
      	}
      	
        
        
          	if (flag == false && current.x < 73){
          		current.x=current.x+2;
          		return current;
          	}
          	else if (current.x>=73)
          	{
          		flag=true;
          	}
          	
          	
                   	
          	if(flag==true)
          	{	
           	
          		//identity1++;
          		if (identity1==0)
          		{
          		if(max==0)
              	{	
              		while(current.y>15)
              		{
              		current.y-=2;
              		b=13;
              		return current;
              		}
              		identity1++;
              	}
              	else if(max==1)
              	{	
              		while(current.y>39)
              		{
              		current.y-=2;
              		b=37;
              		return current;
              		}
              		identity1++;
              	}
              	
              	else if(max==2)
              	{	
              		while(current.y<=60)
              		{
              		current.y+=2;
              		b=62;
              		return current;
              		}
              		identity1++;
              	}
              	
              	else if(max==3)
              	{	
              		while(current.y<=85)
              		{
              		current.y+=2;
              		b=87;
              		return current;
              		}
              		identity1++;
              	}
          		}
              	//b=(int)current.y;
          		//current.y=current.y-2;
          		while(count<32 && circle_count<10)
          		{	
          			double t = 2 * Math.PI * count/32;
          			System.out.println(t);
          			current.y=  b+ radius*Math.sin(t);
          			current.x=a+ radius1*Math.cos(t);
          			count++;
          			if (count==32)
          					{
          				count=0;
          				circle_count++;
          				if (circle_count%2==0 && circle_count!=0)
          				{
          					radius=radius-2;
          					radius1=radius1-2;
          				}
          					}
          			return current;
          		}
           		
          	}
        return current;
    }

}
