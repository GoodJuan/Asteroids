package asteroidGame;

import java.awt.Polygon;

public class Asteroid extends Polygon{
	
	private int rockType;
	
	/*
	1: top left
	2: top right
	3: bottom left
	4: bottom right
	5: spawning from previously destroyed asteroid
	*/
	private int corner; 
	private double angle;
	
	private double rotatedX;
	private double rotatedY;
	
	private int speed;
	
	
	public Asteroid(int[] x, int[] y, int points, int type, int corn){
		super(x,y,points);
		rockType = type;
		corner = corn;
		speed = (int) (Math.random()*5)+2;
	}
	
	public void move(){
		for (int i = 0; i < super.ypoints.length; i++){
			ypoints[i] -= speed;
		}
	}
	
	
	public void setX(double x){
		rotatedX = x;
	}
	public void setY(double y){
		rotatedY = y;
	}
	
	public void setRandAngle(){
		//top left
		if (corner == 1){
			angle = (Math.random()*60)+105;
		}
		//top right
		else if (corner == 2){
			angle = (Math.random()*60)+195;
		}
		//bottom left
		else if (corner == 3){
			angle = (Math.random()*60)+15;
		}
		//bottom right
		else if (corner == 4){
			angle = (Math.random()*60)+285;
		}	
	}
	
	public double getAngle(){
		return angle;
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
