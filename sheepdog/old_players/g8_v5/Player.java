package sheepdog.g8_v5;

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
        
    	Point current = dogs[id-1];
    	//temp=current;
        //Point sheep_pos = sheeps[];
    	int numSheeps = sheeps.length;
        Point[] sheep = new Point[numSheeps];
        //temp=sheep;
        for (int i = 0; i < numSheeps; i++) {
            sheep[i] = new Point(sheeps[i]);
        }
        
      
    	if (flag == false && identity==0 && current.x < 50){
    		current.x=current.x+2;
    		return current;
    	}
    	else if (current.x>=50 && identity==0)
    	{
    		flag=true;
    		//gameplay=0;
    		//identity++;
    	}
             
        /*if(flag==true && identity==0)
        {
        
        sheep_id=get_sheep(current,sheep);
        
        identity++;
        flag=false;
        }*/
        
        //return moved;
                
        switch (gameplay) {
        
        case 0:
        	sheep_id=get_sheep(current,sheep);
        	identity++;
        	System.out.println("wassuppppppp");
        	//flag=false;
        	gameplay=1; 
		case 1:
			dog_pos=return_pos(current,sheep[sheep_id]);
	        moved = move_pos(current, dog_pos );
	        double sheep_x = sheep[sheep_id].x - back_pos.x;
	        double sheep_y = sheep[sheep_id].y - back_pos.y;
	        double dog_x = current.x - back_pos.x;
	        double dog_y = current.y - back_pos.y;
	        
	        dist1 = Math.sqrt((sheep_x*sheep_x)+(sheep_y*sheep_y));
	        dist2 = Math.sqrt((dog_x*dog_x)+(dog_y*dog_y));
	        System.out.println("distance of sheep from gate"+dist1);
	        System.out.println("distance of dog from gate"+dist2);
	        if(dist2>dist1)
	        //if(moved.x>(sheep[sheep_id].x))
	        {
	        	gameplay=2;
	        	//continue;
	        	
	        }
			return moved;
			//break;

		case 2:
			System.out.println("helloooooooooooooooooooooo");
			Point back_to_pos=return_pos(back_pos,sheep[sheep_id]);
        	Point back_to_gate=back_pos(current,back_to_pos);
        	if(sheep[sheep_id].x<=50)
        	{
        		gameplay=0;
        	}
        	
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
        double dist = Math.sqrt((dist_x*dist_x)+(dist_y*dist_y));
        System.out.println("dist"+dist);
        //double r = 4/dist;
        //double theta = dist_y/dist_x;
        //double cos = dist_y/dist;
        //double m = Math.abs(dist_y/dist_x);
        
        double pos = Math.atan(dist_y/dist_x);
//        double pos = Math.atan2(dist_y,dist_x);
        if(dist_x==0)
			pos=Math.PI/2;
            //double pos = Math.atan(Math.abs(theta));
            //double pos1= 4/dist;
            //System.out.println("pos"+pos);
        double x = dest_sheep.x + 2*Math.cos(pos);
        double y = dest_sheep.y + 2*Math.sin(pos);
            //double x = (1-r)*dest_sheep.x + r*dog.x;
            //double y = (1-r)*dest_sheep.y + r*dog.y;
            /*double x = dest_sheep.x + Math.cos(pos);
            double y = dest_sheep.y + Math.sin(pos);*/
            if (x > 100)
                x = 100;
            if (y > 100)
                y = 100;
            if (y<0)
            	y=0;
            System.out.println("dog's Projected x position"+x);
            System.out.println("dog's Projected y position"+y);
            return new Point(x, y);
    
    }
    
    
    
    
    public static Point move_pos(Point dog, Point dest_sheep)
    {
    	double x2,y2;
    	double dist_x = dest_sheep.x-dog.x;
    	System.out.println("dog's current x position"+dog.x);
    	System.out.println("dog's current y position"+dog.y);
    	System.out.println("dog's's x proj position"+dest_sheep.x);
    	System.out.println("dog's y proj position"+dest_sheep.y);
        double dist_y = dest_sheep.y-dog.y;
        double dist_move = Math.sqrt((dist_x*dist_x)+(dist_y*dist_y));
        System.out.println("dist"+dist_move);       
		if (dist_move < 2)
		{
			System.out.println("badddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
			return dest_sheep;
		}
        else {
            double pos = (2-Hop)/dist_move;
            //System.out.println("pos"+pos);
            double x = dog.x + pos *dist_x;
            double y = dog.y + pos *dist_y;
            if (x > 100)
                x = 100;
            if (y > 100)
                y = 100;
            if (y<0)
            	y=0;
            //System.out.println("dogs next position"+x);
            System.out.println("dogs moved x position"+x);
            System.out.println("dogs moved y position"+y);
            return new Point(x, y);
        	}
    }
    
    
    
    public static Point back_pos(Point dog, Point dest_sheep)
    {
    	double x2,y2;
    	double dist_x = dest_sheep.x-dog.x;
    	System.out.println("dog's current x back position"+dog.x);
    	System.out.println("dog's current y back position"+dog.y);
        double dist_y = dest_sheep.y-dog.y;
        double dist = Math.sqrt(Math.abs((dist_x*dist_x)+(dist_y*dist_y)));
        System.out.println("dist"+dist);
        
        
            double pos = (1-Hop)/dist;
            //System.out.println("pos"+pos);
            double x = dog.x + pos *dist_x;
            double y = dog.y + pos *dist_y;
            if (x > 100)
                x = 100;
            if (y > 100)
                y = 100;
            if (y<0)
            	y=0;
            //System.out.println("dogs next position"+x);
            System.out.println("dogs moved x back position"+x);
            System.out.println("dogs moved y back position"+y);
            return new Point(x, y);
        	
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
    


