package asteroidGame;

import java.awt.Polygon;

public class Asteroid extends Polygon{
	
	private int rockType;
	
	public Asteroid(int[] x, int[] y, int points, int type){
		super(x,y,points);
		rockType = type;
		
	}
	
	public void die(){
		rockType--;
		if(rockType > 0){
			scaleDown();
		}
		
	}
	
	public void scaleDown(){
		
	}

}
