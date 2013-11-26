package sheepdog.g8_one;

import sheepdog.sim.Point;

public class Player extends sheepdog.sim.Player {
    private int nblacks;
    private static int gameplay=0;
    private static Point dog_pos=new Point(0.0,0.0);
    private static Point moved=new Point(0.0,0.0); 
    private boolean mode;
    //private static double travelled =0.0;
    private static double dist1 =0.0;
    private static double dist2 =0.0;
    static boolean flag= false;
    //private static Point gate_pos= new Point(48.372,51.162);
    public static Point back_pos = new Point(50,50);
    static int count =0;
    static int sheep_id;
   // private static Point[] temp;
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
        //init();
    	Point current = dogs[id-1];
    	//temp=current;
    	System.out.println("nblacks is"+nblacks);
        //Point sheep_pos = sheeps[];
    	int numSheeps = sheeps.length;
        Point[] sheep = new Point[nblacks];
        //temp=sheep;
        for (int i = 0; i < nblacks; i++) {
            sheep[i] = new Point(sheeps[i]);
        }
        
      
    	if (flag == false && identity==0 && current.x < 50){
    		double ydist = current.y-back_pos.y;
    		double xdist=current.x-back_pos.x;
    		double poss = Math.atan(Math.abs(ydist/xdist));
    		//double distance = Math.sqrt(((current.x-back_pos.x)*(current.x-back_pos.x)) + ((current.x-back_pos.x)*(current.x-back_pos.x)));
    		
    		if(xdist<0)
    		{
    			current.x=current.x+ 1.99*Math.cos(poss);
    		}
    		else
    		{
    			current.x=current.x-1.99*Math.cos(poss);
    		}
    		
    		if(ydist<0)
    		{
    			current.y=current.y+ 1.99*Math.sin(poss);
    		}
    		else
    		{
    			current.y=current.y-1.99*Math.sin(poss);
    		}
    		
    		//current.x=current.x+ 1.99*Math.cos(poss);
    		//current.y=current.y+ 1.99*Math.sin(poss);
    		//current.x=current.x+2;
    		return current;
    	}
    	else if (current.x>=50 && identity==0)
    	{
    		flag=true;
    		//gameplay=0;
    		//identity++;
    	}

                
        switch (gameplay) {
        
        case 0:
        	sheep_id=get_sheep(current,sheep);
        	identity++;
        	//System.out.println("wassuppppppp");
        	//flag=false;
        	gameplay=1; 
		case 1:
			dog_pos=return_pos(current,sheep[sheep_id]);
	       // moved = move_pos(current, dog_pos );
	        double sheep_x = sheep[sheep_id].x - back_pos.x;
	        double sheep_y = sheep[sheep_id].y - back_pos.y;
	        double dog_x = current.x - back_pos.x;
	        double dog_y = current.y - back_pos.y;
	        
	        dist1 = Math.sqrt((sheep_x*sheep_x)+(sheep_y*sheep_y));
	        dist2 = Math.sqrt((dog_x*dog_x)+(dog_y*dog_y));

	        if(dist2>dist1)
	        
	        {
	        	gameplay=2;
	        	//continue;
	        	
	        }
	        else
	        {
			 return dog_pos;
	        }
			 //break;

		case 2:
			Point back_to_pos=return_pos2(current,sheep[sheep_id]);

        	if(sheep[sheep_id].x<=50)
        	{
        		gameplay=0;
        	}
        	
        	return back_to_pos;
		}             
        return current;
    }
    

    
    public static Point return_pos(Point dog, Point dest_sheep)
    {
    	double dist_x = dog.x-dest_sheep.x;
    	double x,y;
        double dist_y = dog.y-dest_sheep.y;
        double dist = Math.sqrt((dist_x*dist_x)+(dist_y*dist_y));
        dist=dist+1;
        
        System.out.println("dist"+dist);
        
        
        double pos = Math.atan(Math.abs(dist_y/dist_x));

        if(dist_x==0)
			pos=Math.PI/2;
        double xdist=1.99*Math.cos(pos);
        double ydist = 1.99*Math.sin(pos);
  
        if(dist_x<0)
		{
			x=dog.x+xdist;
		}
		else
		{
			x=dog.x-xdist;
		}
		
		if(dist_y<0)
		{
			y=dog.y+ydist;
		}
		else
		{
			y=dog.y-ydist;
		}
		

            if (x > 100)
                x = 100;
            if (y > 100)
                y = 100;
            if (y<0)
            	y=0;
            return new Point(x, y);
    
    }
    
    
    public static Point return_pos2(Point dog, Point dest_sheep)
    {
    	double dist_x = dest_sheep.x-back_pos.x;
        double dist_y = dest_sheep.y-back_pos.y;
        Point temp = new Point();
        Point mv = new Point();
        double dist = Math.sqrt((dist_x*dist_x)+(dist_y*dist_y));
        System.out.println("dist"+dist);
        dist=dist+1;

        double pos = Math.atan(dist_y/dist_x);
//        double pos = Math.atan2(dist_y,dist_x);
        if(dist_x==0)
			pos=Math.PI/2;

        temp.x = back_pos.x + dist*Math.cos(pos);
        temp.y = back_pos.y + dist*Math.sin(pos);
          mv = return_pos1(dog,temp);
            return mv;
    }
    
    public static Point return_pos1(Point dog, Point dest_sheep)
    {
    	double dist_x = dog.x-dest_sheep.x;
    	double x,y;
        double dist_y = dog.y-dest_sheep.y;
        double dist = Math.sqrt((dist_x*dist_x)+(dist_y*dist_y));
        double pos = Math.atan(Math.abs(dist_y/dist_x));

        if(dist_x==0)
			pos=Math.PI/2;
        double xdist=0.99*Math.cos(pos);
        double ydist = 0.99*Math.sin(pos);
        if(dist_x<0)
		{
			x=dog.x+xdist;
		}
		else
		{
			x=dog.x-xdist;
		}
		
		if(dist_y<0)
		{
			y=dog.y+ydist;
		}
		else
		{
			y=dog.y-ydist;
		}

            if (x > 100)
                x = 100;
            if (y > 100)
                y = 100;
            if (y<0)
            	y=0;
            return new Point(x, y);
    
    }
    

    
    public static int get_sheep(Point current, Point[] sheep)
    {
    	  int max_sheep = -1;	
    	  double max = 0.0;
          for (int i = 0; i < sheep.length; i++) {
              if (sheep[i].x > 50) {
              	double dist = Math.sqrt(Math.pow(Math.abs(current.x-sheep[i].x),2)+Math.pow(Math.abs(current.y-sheep[i].y), 2));
                  if (dist > max && dist != 0) { 
                      max = dist;
                      max_sheep = i;
                  }
              }
          }
          System.out.println(max_sheep+"   "+sheep[max_sheep].x+"ok"+sheep[max_sheep].y);
          return max_sheep;  	    	
    }
}
    


