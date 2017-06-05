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
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Polygon;


import javax.swing.JFrame;
import javax.swing.Timer;

public class AsteroidGame implements ActionListener, KeyListener{
	
	public static AsteroidGame game;
	public Renderer renderer;
	
	public boolean keyDown = false;
	public int playerAngle = 0;
	
	public boolean left = false;
	public boolean right = false;
	public boolean go = false;
	public boolean back = false;
	public boolean still = true;
	public double angle = 0;
	public int turnRight = 5;
	public int turnLeft = -5;
	
	public ArrayList<Laser> lasers;
	public ArrayList<Shape> transformedLasers;
	
	public final int WIDTH = 1400;
	public final int HEIGHT = 800;
	
	public Ship ship;
	public Shape transformed;
	
	public Rectangle shipHead;
	public Shape shipHeadTrans;
	public Point headPoint;

	
	public AffineTransform transform = new AffineTransform();
	public AffineTransform lasTransform = new AffineTransform();
	public AffineTransform headTransform = new AffineTransform();
	
	public AsteroidGame(){
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);
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
		//shipHeadTrans.getBounds2D().
		
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
		g2d.setColor(Color.YELLOW.brighter());
		for (int i = 0; i < transformedLasers.size(); i++){
			g2d.fill(transformedLasers.get(i));
		}
		
		
		
	}
	

	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		/*The for if and else if statements are just to send the ship
		 * to the other side of the canvas if it ever leaves the screen
		 */
		if (transformed.getBounds2D().getMinX() > WIDTH){
			double tempAng = ship.getAng();
			double diff = 90-tempAng;
			
			transform.rotate(Math.toRadians(diff), ship.getCenterX(), ship.getCenterY());
			transform.translate(0,WIDTH);
			transform.rotate(Math.toRadians(-diff), ship.getCenterX(), ship.getCenterY());
			
			headTransform.rotate(Math.toRadians(diff), shipHead.getCenterX(), shipHead.getCenterY());
			headTransform.translate(0,WIDTH);
			headTransform.rotate(Math.toRadians(-diff), shipHead.getCenterX(), shipHead.getCenterY());
			
		}
		
		else if (transformed.getBounds2D().getX() < 0){
			double tempAng = ship.getAng();
			double diff = 90-tempAng;
			
			transform.rotate(Math.toRadians(diff), ship.getCenterX(), ship.getCenterY());
			transform.translate(0,-WIDTH);
			transform.rotate(Math.toRadians(-diff), ship.getCenterX(), ship.getCenterY());
			
			headTransform.rotate(Math.toRadians(diff), shipHead.getCenterX(), shipHead.getCenterY());
			headTransform.translate(0,-WIDTH);
			headTransform.rotate(Math.toRadians(-diff), shipHead.getCenterX(), shipHead.getCenterY());
		}
		
		else if (transformed.getBounds2D().getY() > HEIGHT){
			double tempAng = ship.getAng();
			double diff = 180-tempAng;
			
			transform.rotate(Math.toRadians(diff), ship.getCenterX(), ship.getCenterY());
			transform.translate(0,HEIGHT);
			transform.rotate(Math.toRadians(-diff), ship.getCenterX(), ship.getCenterY());
			
			headTransform.rotate(Math.toRadians(diff), shipHead.getCenterX(), shipHead.getCenterY());
			headTransform.translate(0,HEIGHT);
			headTransform.rotate(Math.toRadians(-diff), shipHead.getCenterX(), shipHead.getCenterY());
			
			
		}
		
		else if (transformed.getBounds2D().getY() < 0){
			double tempAng = ship.getAng();
			double diff = 180-tempAng;
			transform.rotate(Math.toRadians(diff), ship.getCenterX(), ship.getCenterY());
			transform.translate(0,-HEIGHT);
			transform.rotate(Math.toRadians(-diff), ship.getCenterX(), ship.getCenterY());
			
			headTransform.rotate(Math.toRadians(diff), shipHead.getCenterX(), shipHead.getCenterY());
			headTransform.translate(0,-HEIGHT);
			headTransform.rotate(Math.toRadians(-diff), shipHead.getCenterX(), shipHead.getCenterY());
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
		
		//moving and shaping each individual laser that had been shot
		for (int i = 0; i < transformedLasers.size(); i++){
			lasers.get(i).move();
			
			lasTransform = new AffineTransform();
			System.out.println(lasers.get(i).getAng());
			lasTransform.rotate(Math.toRadians(lasers.get(i).getAng()), lasers.get(i).getRotX(), lasers.get(i).getRotY());
			transformedLasers.set(i, lasTransform.createTransformedShape(lasers.get(i)));

		}
		
		//moving the ship
		ship.move();
		
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
		
	
		
		renderer.repaint();
		
	}
	
	//defining a new laser
	public void fireLaser(){
		Laser tempLaser = new Laser((int)shipHeadTrans.getBounds2D().getX(), (int)shipHeadTrans.getBounds2D().getY(), 5, 20, ship.getAng());
		lasers.add(tempLaser);
		
		lasTransform = new AffineTransform();
		lasTransform.rotate(Math.toRadians(tempLaser.getAng()), shipHeadTrans.getBounds2D().getX(), shipHeadTrans.getBounds2D().getY());
		
		lasers.get(lasers.size()-1).setX(shipHeadTrans.getBounds2D().getX());
		lasers.get(lasers.size()-1).setY(shipHeadTrans.getBounds2D().getY());
		
		transformedLasers.add(lasTransform.createTransformedShape(tempLaser));
		
	}
	
	public static void main(String[] args){
		game = new AsteroidGame();
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
	
	
}
