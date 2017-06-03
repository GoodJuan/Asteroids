package asteroidGame;

import java.awt.Polygon;
import java.util.Arrays;

public class Ship extends Polygon{
	
	/**
	 * 
	 */
	private double currSpeed = 0;
	
	private static final long serialVersionUID = 1L;
	public double angle;
	public int[] midX;
	public int[] midY;
	
	public Ship(int[] x, int[] y, int points, double angle){
		super(x, y, points);
		midX = x;
		midY = y;
		
		this.angle= angle;
	}
	
	public void right(){
		angle += 5;
	}
	public void left(){
		angle -= 5;
	}
	
	public void move(){
		for (int i = 0; i < super.ypoints.length; i++){
			super.ypoints[i] -= currSpeed;
			//System.out.println(super.ypoints[i]);
			//System.out.println(super.xpoints[i]);
		}
		//System.out.println(Arrays.toString(super.ypoints));
		
		
		
	}
	
	
	public void reverse(){
		if (currSpeed  > -15) currSpeed -= 0.2;
	}
	
	public void go(){
		if (currSpeed < 25) currSpeed += 0.5;
		
	}
	
	public int getCenterX(){
		return super.xpoints[2];
	}
	public int getCenterY(){
		return super.ypoints[2];
	}
	
	public double getAng(){
		return angle;
	}
	public void test(){
		for (int x = 0; x < super.ypoints.length; x++){
			super.ypoints[x] += 1000;
		}
	}
	
	/*
	public void decrement(){
		if(currSpeed == 0){}
		
		else if (currSpeed > 0 && currSpeed < 15){
			currSpeed -= 0.05;

		}
		else if (currSpeed < 0 && currSpeed > -15){
			currSpeed += 0.05;

		}
		System.out.println("losing speed");
	}
	*/
}
