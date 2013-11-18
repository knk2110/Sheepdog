package sheepdog.g8_v4;

import sheepdog.sim.Point;

public class Player extends sheepdog.sim.Player {
    private int nblacks;
    private boolean mode;
    private static int phase = 0;
    private int firstDownSteps = 8;
    private int secondDownSteps = 8;
    private int leftSteps = 25;
    private int upSteps = 16;
    private int rightSteps = 25;
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
	private boolean dogPlayersExist = false;
	private static DogPlayer d0;
	private static DogPlayer d1;
	private static DogPlayer d2;
	
    public void init(int nblacks, boolean mode) {
        this.nblacks = nblacks;
        this.mode = mode;
	d0 = new DogPlayer(0);
	d1 = new DogPlayer(1);
	d2 = new DogPlayer(2);
    }
    
    // Return: the next position
    // my position: dogs[id-1]
    public Point move(Point[] dogs, // positions of dogs
                      Point[] sheeps) { // positions of the sheeps
	Point current = dogs[id-1];		
		if (dogs[0].x == current.x && dogs[0].y == current.y){
			return d0.getMoveForDog(dogs, sheeps);
		}
		else if (dogs[1].x == current.x && dogs[1].y == current.y){
			return d1.getMoveForDog(dogs, sheeps);
		}
		else if (dogs[2].x == current.x && dogs[2].y == current.y){
			return d2.getMoveForDog(dogs, sheeps);
		}
		return current;
	}
}
