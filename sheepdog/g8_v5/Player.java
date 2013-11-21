package sheepdog.dumb;

import sheepdog.sim.Point;

public class Player extends sheepdog.sim.Player {
    private int nblacks;
    private static int gameplay=1;
    private static Point dog_pos=new Point(0.0,0.0);
    private static Point moved=new Point(0.0,0.0); 
    private boolean mode;
    static boolean flag= false;
    public static Point back_pos = new Point(50,50);
    static int count =0;
    static int sheep_id;
    static double Hop = .001;
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
        
    	Point current = dogs[id-1];
        //Point sheep_pos = sheeps[];
    	int numSheeps = sheeps.length;
        Point[] sheep = new Point[numSheeps];
        for (int i = 0; i < numSheeps; i++) {
            sheep[i] = new Point(sheeps[i]);
        }
        
      
    	if (flag == false && identity==0 && current.x < 50){
    		current.x=current.x+2;
    		return current;
    	}
    	else if (current.x>=50)
    	{
    		flag=true;
    		//identity++;
    	}
             
        if(flag==true && identity==0)
        {
        
        sheep_id=get_sheep(current,sheep);
        
        identity++;
        flag=false;
        }
        
        //return moved;
                
        switch (gameplay) {
		case 1:
			dog_pos=return_pos(current,sheep[sheep_id]);
	        moved = move_pos(current, dog_pos );
	        
	        if(moved.x==(dog_pos.x) && moved.y==(dog_pos.y))
	        {
	        	gameplay=0;
	        	//continue;
	        	
	        }
			return moved;
			//break;

		case 0:
			System.out.println("helloooooooooooooooooooooo");
			Point back_to_pos=return_pos(current,back_pos);
        	Point back_to_gate=move_pos(current,back_to_pos);
        	return back_to_gate;
			//break;
		}
        
              
        return current;
    }
    
    
    
    public static Point return_pos(Point dog, Point dest_sheep)
    {
    	double dist_x = dest_sheep.x-dog.x;
    	System.out.println("sheep's x position"+dest_sheep.x);
    	System.out.println("sheep's y position"+dest_sheep.y);
        double dist_y = dest_sheep.y-dog.y;
        double dist = Math.sqrt(Math.abs((dist_x*dist_x)+(dist_y*dist_y)));
        System.out.println("dist"+dist);
        
        double theta = dist_y/dist_x;
        //double cos = dist_y/dist;
        
            double pos = Math.atan(Math.abs(theta));
            //double pos1= 4/dist;
            System.out.println("pos"+pos);
            double x = dest_sheep.x + Math.cos(pos);
            double y = dest_sheep.y + Math.sin(pos);
            /*double x = dest_sheep.x + Math.cos(pos);
            double y = dest_sheep.y + Math.sin(pos);*/
            if (x > 100)
                x = 100;
            if (y > 100)
                y = 100;
            System.out.println("dog's Projected x position"+x);
            System.out.println("dog's Projected y position"+y);
            return new Point(x, y);
    
    }
    
    
    
    public static Point move_pos(Point dog, Point dest_sheep)
    {
    	double x2,y2;
    	double dist_x = dest_sheep.x-dog.x;
    	System.out.println("dog's x position"+dog.x);
    	System.out.println("dog's y position"+dog.y);
        double dist_y = dest_sheep.y-dog.y;
        double dist = Math.sqrt(Math.abs((dist_x*dist_x)+(dist_y*dist_y)));
        System.out.println("dist"+dist);
        
        
		if (dist < 2)
		{
        	/*x2 = dest_sheep.x+.4*(Math.abs(dist_x/dist));
        	y2 = dest_sheep.y+.4*(Math.abs(dist_y/dist));
        	if (x2 > 100)
                x2 = 100;
            if (y2 > 100)
                y2 = 100;
            return new Point(x2,y2);*/
			return dest_sheep;
		}
        else {
            double pos = (2-Hop)/dist;
            //System.out.println("pos"+pos);
            double x = dog.x + pos *dist_x;
            double y = dog.y + pos *dist_y;
            if (x > 100)
                x = 100;
            if (y > 100)
                y = 100;
            //System.out.println("dogs next position"+x);
            return new Point(x, y);
    }
    }
    
    
    
    
    public static int get_sheep(Point current, Point[] sheep)
    {
    	  int max_sheep = -1;	
    	  double max = 0.0;
          for (int i = 0; i < sheep.length; i++) {
              if (sheep[i].x > 50) {
              	double dist = Math.sqrt(Math.pow(Math.abs(current.x-sheep[i].x),2)+Math.pow(Math.abs(current.y-sheep[i].y), 2));
                  if (dist > max && dist != 0) { // ignore overlapping dog
                      max = dist;
                      max_sheep = i;
                  }
              }
          }
          System.out.println(max_sheep+"   "+sheep[max_sheep].x+"ok"+sheep[max_sheep].y);
          return max_sheep;  	    	
    }
}
    


