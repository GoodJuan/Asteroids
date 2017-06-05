package asteroidGame;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Laser extends Rectangle{
	
	private double angle;
	private double rotatedX;
	private double rotatedY;
	
	public Laser(int x, int y , int width, int height, double ang){
		super(x, y, width, height);
		angle = ang;
		Rectangle tst = new Rectangle(); 
		
		
	}
	
	public void setX(double x){
		rotatedX = x;
	}
	
	public void setY(double y){
		rotatedY = y;
	}
	
	public double getRotX(){
		return rotatedX;
	}
	
	public double getRotY(){
		return rotatedY; 
	}
	
	public void move(){
		super.y -= 35;
	}
	
	public double getAng(){
		return angle;
	}
	
	public boolean intersects (Rectangle2D r){
		//if intersects
		if (super.intersects(r)){
			return true;
		}
		else{
			return false;
		}
		 
	}

	
}
