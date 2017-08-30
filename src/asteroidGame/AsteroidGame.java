package asteroidGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;
import java.awt.Polygon;
import java.awt.geom.Area;


//import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.Timer;



public class AsteroidGame implements ActionListener, KeyListener{
	
	public int testInt;
	
	public static AsteroidGame game;
	public Renderer renderer;
	
	public boolean gameOver = false;
	
	public ArrayList<Laser> lasers;
	public ArrayList<Shape> transformedLasers;
	
	public ArrayList<Asteroid> asteroids;
	public ArrayList<Shape> transformedAsteroids;
	
	public final int WIDTH = 1400;
	public final int HEIGHT = 800;
	
	public boolean keyDown = false;
	public int playerAngle = 0;
	public int score = 0;
	
	public boolean left = false;
	public boolean right = false;
	public boolean go = false;
	public boolean back = false;
	public boolean still = true;
	public double angle = 0;
	public int turnRight = 5;
	public int turnLeft = -5;
	
	public Ship ship;
	public Shape transformed;
	
	public Rectangle shipHead;
	public Shape shipHeadTrans;
	public Point headPoint;
	public java.util.Timer cooldown;
	public boolean readyToFire = true;
	
	public AffineTransform transform = new AffineTransform();
	public AffineTransform lasTransform = new AffineTransform();
	public AffineTransform headTransform = new AffineTransform();
	public AffineTransform asteroidTransform = new AffineTransform();
	
	//temp variables to help tell where the new asteroid should be
	private int spawnX;
	private int spawnY;

	
	public AsteroidGame(){
		JFrame jframe = new JFrame();
		
		javax.swing.Timer timer = new Timer(20, this);
		cooldown = new java.util.Timer();
		
		renderer = new Renderer();
		
		jframe.add(renderer);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.setVisible(true);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		
		int xPoints[] = {800, 780, 800, 820};
	    int yPoints[] = {400, 460, 440, 460}; 
	    
	    //(800, 400) is the initial location of the 'tip' of the ship'.
	    headPoint = new Point(800, 400);
	    
	    lasers = new ArrayList<Laser>();
	    transformedLasers = new ArrayList<Shape>();
	    
		ship = new Ship(xPoints, yPoints, 4, 0);
		transformed = transform.createTransformedShape(ship);
		 
		shipHead = new Rectangle(headPoint);
		shipHeadTrans = transform.createTransformedShape(shipHead);
		
		asteroids = new ArrayList<Asteroid>();
		transformedAsteroids = new ArrayList<Shape>();
		
	
		
		timer.start();
		
	}
	
	public void repaint(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	
		Graphics2D g2d = (Graphics2D)g;
		
		//drawing the ship
		g2d.setColor(Color.WHITE);
		g2d.draw(transformed);
		
		//drawing lasers
		g2d.setColor(Color.RED);
		for (int i = 0; i < transformedLasers.size(); i++){
			g2d.fill(transformedLasers.get(i));
		}

		//drawing asteroids
		g2d.setColor(Color.WHITE.brighter().brighter());
		for (int i = 0; i < transformedAsteroids.size(); i++){
			g2d.fill(transformedAsteroids.get(i));
		}
		
		
	}
	

	//////////////////////
	//the main game engine
	//////////////////////
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		/*The for if and else if statements are just to send the ship
		 * to the other side of the canvas if it ever leaves the screen
		 
		 */
		if (!gameOver){
			//exit right (1)
			if (transformed.getBounds2D().getMinX() > WIDTH){
				
				sendToOtherSide(1, WIDTH, null, ship.getAng(), -1, true, null);
			}
			//exit left (2)
			else if (transformed.getBounds2D().getX() < 0){
				sendToOtherSide(2, -WIDTH, null, ship.getAng(), -1, true,null);
			}
			
			//exit bot (3)
			else if (transformed.getBounds2D().getY() > HEIGHT){
				sendToOtherSide(3, HEIGHT, null, ship.getAng(), -1, true, null);
			}
			
			//exit top (4)
			else if (transformed.getBounds2D().getY() < 0){
				sendToOtherSide(4, -HEIGHT, null, ship.getAng(), -1, true, null);
			}
	
			if (right){
				ship.right();
				//rotating the ship
				transform.rotate(Math.toRadians(turnRight), ship.getCenterX(), ship.getCenterY());
				//rotating the 'tip' of the ship.
				headTransform.rotate(Math.toRadians(turnRight), ship.getCenterX(), ship.getCenterY());
			}
			
			else if (left){
				ship.left(); 
				//rotating the ship
				transform.rotate(Math.toRadians(turnLeft), ship.getCenterX(), ship.getCenterY());
				//rotating the 'tip' of the ship
				headTransform.rotate(Math.toRadians(turnLeft), ship.getCenterX(), ship.getCenterY());
			}
			if (go){
				ship.go();
			}
			else if (back){
				ship.reverse();
			}
			//moving the ship
			ship.move();
			
			//moving and shaping each individual laser that had been shot
			for (int i = 0; i < transformedLasers.size(); i++){
				lasers.get(i).move();
				
				lasTransform = new AffineTransform();
				lasTransform.rotate(Math.toRadians(lasers.get(i).getAng()), lasers.get(i).getRotX(), lasers.get(i).getRotY());
				transformedLasers.set(i, lasTransform.createTransformedShape(lasers.get(i)));
	
			}
			for (int i = 0; i < transformedAsteroids.size(); i++){
				//moving the specific asteroid
				asteroids.get(i).move();
				
				//new instance of rotation
				asteroidTransform = new AffineTransform();
				
				if (transformedAsteroids.get(i).getBounds2D().getMinX() > WIDTH){
					sendToOtherSide(1, WIDTH, asteroids.get(i), asteroids.get(i).getAngle(), i, false, asteroidTransform);
				}
				//exit left (2)
				else if (transformedAsteroids.get(i).getBounds2D().getX() < 0){
					sendToOtherSide(2, -WIDTH, asteroids.get(i), asteroids.get(i).getAngle(), i, false, asteroidTransform);
	
				}
				
				//exit bot (3)
				else if (transformedAsteroids.get(i).getBounds2D().getY() > HEIGHT){
					sendToOtherSide(3, HEIGHT, asteroids.get(i), asteroids.get(i).getAngle(), i, false, asteroidTransform);
	
				}
				
				//exit top (4)
				else if (transformedAsteroids.get(i).getBounds2D().getY() < 0){
					sendToOtherSide(4, -HEIGHT, asteroids.get(i), asteroids.get(i).getAngle(), i, false, asteroidTransform);
				}
				
				//rerotating and setting it to the new asteroid
				asteroidTransform.rotate(Math.toRadians(asteroids.get(i).getAngle()), asteroids.get(i).getBounds2D().getCenterX(), asteroids.get(i).getBounds2D().getCenterY());
				transformedAsteroids.set(i, asteroidTransform.createTransformedShape(asteroids.get(i)));
			}
			
			
			double velX = ship.getSpeed() * Math.cos(Math.toRadians(ship.getAng()));
			double velY = ship.getSpeed()* Math.sin(Math.toRadians(ship.getAng()));
			//moving the 'tip'
			double locX = 2 * velX;
			double locY = 2 * velY;
			
			headTransform.transform(headPoint, new Point((int)locX, (int)locY));
			shipHead.y -= ship.getSpeed();
	
			transformed = transform.createTransformedShape(ship);
			shipHeadTrans = headTransform.createTransformedShape(shipHead);
			//System.out.println("x: " + shipHeadTrans.getBounds2D().getX() + "y: " + shipHeadTrans.getBounds2D().getY());
			
			for (int i = 0; i < asteroids.size(); i++){
				asteroids.get(i).move();
			}
			
			for (int i = 0; i < transformedLasers.size(); i++){
				for (int k = 0; k < transformedAsteroids.size(); k++){
					if (intersection(transformedLasers.get(i), transformedAsteroids.get(k))){
						transformedLasers.remove(i);
						transformedAsteroids.remove(k);
						
						lasers.remove(i);
						asteroids.remove(k);
						i--;
						k--;
					}
				}
			}
			
			for (int i = 0; i < transformedAsteroids.size(); i++){	
				if (intersection(transformed, transformedAsteroids.get(i))){
					gameOver = true;
				}
			}
			
		}
		if (!readyToFire){
			System.out.println("not ready");
		}
		renderer.repaint();
		
	}
	
	
	
	///////////////////////
	//Spawning in sprites
	///////////////////////
	
	
	//defining a new laser
	public void fireLaser(){
		if (readyToFire){
			Laser tempLaser = new Laser((int)shipHeadTrans.getBounds2D().getX(), (int)shipHeadTrans.getBounds2D().getY(), 5, 20, ship.getAng());
			lasers.add(tempLaser);
			
			lasTransform = new AffineTransform();
			lasTransform.rotate(Math.toRadians(tempLaser.getAng()), shipHeadTrans.getBounds2D().getX(), shipHeadTrans.getBounds2D().getY());
			
			lasers.get(lasers.size()-1).setX(shipHeadTrans.getBounds2D().getX());
			lasers.get(lasers.size()-1).setY(shipHeadTrans.getBounds2D().getY());
			
			cooldown = new java.util.Timer();
			
			//this allows me to override having to create a new class to avoid
			//issues with static variables etc.
		    cooldown.scheduleAtFixedRate(new TimerTask() {
	
		        public void run() {
		        	testMethod();
	
		        }
		    }, 0, 250);
	
			transformedLasers.add(lasTransform.createTransformedShape(tempLaser));	
		}
	}
	
	public void testMethod(){
		testInt++;
		System.out.println(testInt);
		if (testInt == 1){
			readyToFire = false;
		}
		
		else if (testInt == 2){
			cooldown.cancel();
			readyToFire = true;
			testInt = 0;
		}
		
	}
	
	
	//this mimics the code of fireLaser()
	
	public void spawnAsteroid(){
		//defining the actual asteroid
		Asteroid tempAsteroid = new Asteroid(getRandXAst(), getRandYAst(), 6, 3, findCorner());
		tempAsteroid.setRandAngle();
		asteroids.add(tempAsteroid);
		
		//rotating the AffineTransformation instance of the asteroid
		asteroidTransform = new AffineTransform();
		asteroidTransform.rotate(Math.toRadians(tempAsteroid.getAngle()), tempAsteroid.getBounds2D().getCenterX(), tempAsteroid.getBounds2D().getCenterY());

		//resetting the points because of the rotation
		asteroids.get(asteroids.size()-1).setX(tempAsteroid.getBounds2D().getCenterX());
		asteroids.get(asteroids.size()-1).setY(tempAsteroid.getBounds2D().getCenterY());
		
		//actually creating the new transformed asteroid
		transformedAsteroids.add(asteroidTransform.createTransformedShape(tempAsteroid));
	}
	
	
	/////////////////////////////
	//Collision and other physics
	/////////////////////////////
	
	public boolean shipHitAst(){
		for (int i = 0; i < transformedAsteroids.size(); i++){
			if(transformed.getBounds2D().intersects(transformedAsteroids.get(i).getBounds2D())){
				return true; 
			}
		}
		
		//System.out.println("nothing");
		return false;
	}
	
	public boolean laserHitAst(){
		for (int i = 0; i < transformedLasers.size(); i++){
			for (int k = 0; k < transformedAsteroids.size(); k++){
				if (transformedLasers.get(i).getBounds2D().intersects(transformedAsteroids.get(k).getBounds2D())){
					
					lasers.remove(i);
					asteroids.remove(i);
					
					transformedLasers.remove(i);
					transformedAsteroids.remove(k);
					
					score++;
					
				}
			}
		}
		
		return false;
	}
	
	public static boolean intersection(Shape shapeA, Shape shapeB) {
		   Area areaA = new Area(shapeA);
		   areaA.intersect(new Area(shapeB));
		   return !areaA.isEmpty();
	}

	
	
	
	////////////////////////////////////////////
	//Methods for getting coordinates/movement//
	///////////////////////////////////////////
	
	//a bunch of stuff here was for testing
	public void sendToOtherSide(int side, int distance, Asteroid obj, double tempAng, int index, boolean isShip, AffineTransform test){
		if (isShip){
			double diff = 0.0;
			if (side == 1 || side == 2){
				diff = 90-tempAng;
			}
			else if (side == 3 || side == 4){
				diff = 180-tempAng;
			}
			
			
			transform.rotate(Math.toRadians(diff), ship.getCenterX(), ship.getCenterY());
			transform.translate(0,distance);
			transform.rotate(Math.toRadians(-diff), ship.getCenterX(), ship.getCenterY());
			
			headTransform.rotate(Math.toRadians(diff), shipHead.getCenterX(), shipHead.getCenterY());
			headTransform.translate(0,distance);
			headTransform.rotate(Math.toRadians(-diff), shipHead.getCenterX(), shipHead.getCenterY());
		}
		/*
		else {
			double diff = 0.0;
			if (side == 1 || side == 2){
				diff = 90-tempAng;
			}
			else if (side == 3 || side == 4){
				diff = 180-tempAng;
			}
			
			test.rotate(Math.toRadians(diff), transformedAsteroids.get(index).getBounds2D().getCenterX(), asteroids.get(index).getBounds2D().getCenterY());
			test.translate(0, distance);
			test.rotate(Math.toRadians(-diff), transformedAsteroids.get(index).getBounds2D().getCenterX(), asteroids.get(index).getBounds2D().getCenterY());
		}
		*/
	}
	
	
	public int[] getRandXAst(){
		int[] xCor = {30, 110, 140, 110, 30, 0};
		
		for (int i = 0; i < xCor.length; i++){
			xCor[i] += (int) (Math.random()*41)-20;
		}
		
		int corner = (int) (Math.random()*2);
		spawnX = corner;
		//if the rand number equals 1, spawn the asteroid on the right
		if (corner == 1){
			for (int i = 0; i < xCor.length; i++){
				xCor[i] += WIDTH - 140;
			}
		}
		
		return xCor;
	}
	
	
	public int[] getRandYAst(){
		
		int[] yCor = {10, 10, 70, 130, 130, 70};
		
		for (int i = 0; i < yCor.length; i++){
			yCor[i] += (int) (Math.random()*41)-20;
		}
		
		
		int corner = (int) (Math.random()*2);
		spawnY = corner;
		//if the rand number equals 1, spawn the asteroid on the bottom
		if (corner == 1){
			for (int i = 0; i < yCor.length; i++){
				yCor[i] += HEIGHT - 130;
			}
		}
		
		return yCor;
	}
	
	public int findCorner(){
		//top left
		if (spawnX == 0 && spawnY == 0){
			return 1;
		}
		//top right
		else if (spawnX == 1 && spawnY == 0){
			return 2;
		}
		//bottom left
		else if (spawnX == 0 && spawnY == 1){
			return 3;
		}
		//bottom right
		else if (spawnX == 1 && spawnY == 1){
			return 4;
		}
		return 5;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_RIGHT){
			right = true;
			keyDown = true;
		}else if (e.getKeyCode() == KeyEvent.VK_LEFT){
			left = true;
			keyDown = true;
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_UP){
			go = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_DOWN){
			back = true;
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_Q){
			spawnAsteroid();
		}
		
		//fire laser
		if (e.getKeyCode() == KeyEvent.VK_SPACE){
			fireLaser();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_RIGHT){
			right = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT){
			left = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP){
			go = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN){
			back = false;
		}
		still = true;
		keyDown = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		game = new AsteroidGame();
	}
	
}
