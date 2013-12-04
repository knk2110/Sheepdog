package sheepdog.g8_sweep;
//import sheepdog.g8_final.Calculator;
import sheepdog.g8_sweep.Zone;
import sheepdog.g8_sweep.Calculator;
import sheepdog.sim.Point;

public class Player extends sheepdog.sim.Player {
    private int nblacks;
    private boolean mode;
    private static int modes=0;
    private static int numdogs;
    private int count =0;
    private Point Gate =new Point(50.0,50.0); 
    public static final double WIDTH = 50;
   // private Point Gate =new Point(50.0,50.0);
    final static double MAX_SPEED = 1.99;
    final static double MIN_SPEED = 0.4;
    final static double SLOW_SPEED = 0.2;
    public void init(int nblacks, boolean mode) {
        this.nblacks = nblacks;
        this.mode = mode;
    }
    
    // Return: the next position
    // my position: dogs[id-1]
    public Point move(Point[] dogs, // positions of dogs
                      Point[] sheeps) { // positions of the sheeps
        Point current = dogs[id-1];
        numdogs = dogs.length;
        Double dist = 100.0/(numdogs-1);
        
        Point[] Dogs = new Point[numdogs];
        for (int i = 0; i < numdogs; i++) {
            Dogs[i] = new Point(dogs[i]);
        }
        
        if (current.x<50)
        {
        	 return moveDogTowardGate(current);
        }
        
        else if (numdogs>=32 && current.x>=50)
        {
		System.out.println("modes: " + modes);        	
        	switch (modes)
        	{
        	case 0:   
        	{
           			Double y_i =(id-1)*dist;
           			System.out.println(id+"id"+y_i);
           			if (id==numdogs)
        			{
        				//System.out.println("okkkkk");
        				
        				y_i=99.75;
        			}
        			Point temp=new Point(100.0,y_i);
        			
        			if(current.x==100.00)
        			{
        				//System.out.println("ok");
        				count=0;
        			 for (int i = 0; i < numdogs; i++) {
        		            if(Dogs[i].x==100.00)
        		            {
        		            	//System.out.println(count);
        		            	count++;
        		            }
        		        }   
        			 if(count==numdogs)
        			 {
        				 
        				 System.out.println("hello");
        				modes=1; 
        			 }
        			return current;	
        			
        			}
        			else
        			{
        			return moveDogTowardEnd(current,temp);
        			}
        	}		
        	case 1:
        	{
        		Double x_i =54.50;
        		Point back_temp=new Point(x_i,current.y);	
        		if (current.x==54.50)
        		{
        			//System.out.println("help");
        			modes=2;  
        			return current;
        		}
        		
			 else
			 {
        		return moveDogTowardSweep(current,back_temp);
        	 }
        	}//return     
        	
        	
        	case 2:
        		if (id>0 && id<3)
        		{
        			double x_plc = 50.0+((id-1)*2.5);
        			double y_plc = 0.0;
        			Point temp_plc = new Point(x_plc,y_plc);
        			int count1 =0;  		            
        			if(current.x==x_plc)
        			{
        				for (int i = 0; i < 2; i++) {
        					if (Dogs[i].y==y_plc)
        					{
        						System.out.println("please");
        						count1++;
        					}
        				}
        				for (int i = numdogs-2; i < numdogs; i++) {
        					if (Dogs[i].y==100.0)
        					{
        						System.out.println("please");
        						count1++;
        					}
        				}
        					if(count1==4)
        					{
        						modes=3;
        						return current;
        					}
        					else
        						return current;
        			
        			}
        			
        		else
        			return moveDogTowardSweep(current,temp_plc);
        		}
        		if (id>numdogs-2 && id<=numdogs)
        		{
        			double x_plc = 50.0+((id-numdogs+1)*2.5);
        			double y_plc = 100.0;
        			Point temp_plc = new Point(x_plc,y_plc);
        			if(current.x==x_plc)
        			{
        				int count1=0;
        				for (int i = numdogs-2; i < numdogs; i++) {
        					if (Dogs[i].y==y_plc)
        					{
        						System.out.println("please");
        						count1++;
        					}
        				}
        				for (int i = 0; i < 2; i++) {
        					if (Dogs[i].y==0.0)
        					{
        						System.out.println("please");
        						count1++;
        					}
        				}
        					if(count1==4)
        					{
        						modes=3;
        						return current;
        					}
        					else
        						return current;
        			}
        			else
        			return moveDogTowardSweep(current,temp_plc);
        		}
        		else
        		{
        			Double dist1 = 100.0/(numdogs-5);
        			Double y_i =(id-3)*dist1;
           			System.out.println(id+"id"+y_i);
           			if (id==numdogs-2)
        			{
        				//System.out.println("okkkkk");
        				
        				y_i=100.0;
        			}
        			Point temp=new Point(current.x,y_i);
        			if(current.y==y_i)
        			{
        				modes=3;
        				return current;
        			}
        			else
        			return moveDogTowardSweep(current,temp);
        		}
        		
        	case 3:
        		if (id>0 && id<3)
        		{
        			//double x_plc = 50.0+((id-1)*2.5);
        			double y_plc = 48.0;
        			Point temp_plc = new Point(current.x,y_plc);
        			return moveDogTowardSweep(current,temp_plc);
        		}
        		if (id>numdogs-2 && id<=numdogs)
        		{
        			//double x_plc = 50.0+((id-numdogs+1)*2.5);
        			double y_plc = 52.0;
        			Point temp_plc = new Point(current.x,y_plc);
        			/*if(current.x==x_plc)
        			{
        				modes=3;
        				return current;
        			}
        			else*/
        			return moveDogTowardSweep(current,temp_plc);
        		}
        		else
        		{
        		if(Dogs[0].y==48.0 && Dogs[numdogs-1].y==52.0)        			
        		{
        			
        			Double x_pc=50.0;
        		Point temp_pc=new Point(x_pc,current.y);	
        		return moveDogTowardSlow(current, temp_pc);	
        			
        		}
        		else
        			return current;
        		
        		}
        	}
        	}
        
        
        
        return current;
    }

    
    public Point moveDogTowardGate(Point dogPoint) {
        double distanceFromGate = Calculator.dist(dogPoint, Gate);
        if (distanceFromGate<MAX_SPEED){
            dogPoint.x += distanceFromGate*(Gate.x-dogPoint.x)/distanceFromGate;
            dogPoint.y += distanceFromGate*(Gate.y-dogPoint.y)/distanceFromGate;
            return dogPoint;
        }
        dogPoint.x += MAX_SPEED*(Gate.x-dogPoint.x)/distanceFromGate;
        dogPoint.y += MAX_SPEED*(Gate.y-dogPoint.y)/distanceFromGate;
        return dogPoint;
    }
    
    
    public Point moveDogTowardEnd(Point dogPoint, Point wall) {
        double distanceFromGate = Calculator.dist(dogPoint, wall);
        if (distanceFromGate<MAX_SPEED){
            dogPoint.x += distanceFromGate*(wall.x-dogPoint.x)/distanceFromGate;
            dogPoint.y += distanceFromGate*(wall.y-dogPoint.y)/distanceFromGate;
            return dogPoint;
        }
        dogPoint.x += MAX_SPEED*(wall.x-dogPoint.x)/distanceFromGate;
        dogPoint.y += MAX_SPEED*(wall.y-dogPoint.y)/distanceFromGate;
        return dogPoint;
    }
    
    
    public Point moveDogTowardSweep(Point dogPoint, Point wall) {
        double distanceFromGate = Calculator.dist(dogPoint, wall);
        if (distanceFromGate<MIN_SPEED){
            dogPoint.x += distanceFromGate*(wall.x-dogPoint.x)/distanceFromGate;
            dogPoint.y += distanceFromGate*(wall.y-dogPoint.y)/distanceFromGate;
            return dogPoint;
        }
        dogPoint.x += MIN_SPEED*(wall.x-dogPoint.x)/distanceFromGate;
        dogPoint.y += MIN_SPEED*(wall.y-dogPoint.y)/distanceFromGate;
        return dogPoint;
    }
    
    
    public Point moveDogTowardSlow(Point dogPoint, Point wall) {
        double distanceFromGate = Calculator.dist(dogPoint, wall);
        if (distanceFromGate<SLOW_SPEED){
            dogPoint.x += distanceFromGate*(wall.x-dogPoint.x)/distanceFromGate;
            dogPoint.y += distanceFromGate*(wall.y-dogPoint.y)/distanceFromGate;
            return dogPoint;
        }
        dogPoint.x += SLOW_SPEED*(wall.x-dogPoint.x)/distanceFromGate;
        dogPoint.y += SLOW_SPEED*(wall.y-dogPoint.y)/distanceFromGate;
        return dogPoint;
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
       
}
