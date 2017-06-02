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
	
	public Shape transformed;
	public Shape transformedLine;
	
	public Point p1;
	public Point p2;
	public Point center;
	public Point p4;
	
	public final int WIDTH = 1400;
	public final int HEIGHT = 800;
	
	public Ship ship;

	
	public AffineTransform transform = new AffineTransform();
	
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
	    
	    p1 = new Point(400,400);
	    p2 = new Point(380, 460);
	    center = new Point(400,440);//center
	    p4 = new Point(420, 460);
	    
		ship = new Ship(xPoints, yPoints, 4, 0);
		transformed = transform.createTransformedShape(ship);
		
		timer.start();
		
	}
	
	public void repaint(Graphics g){
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
	
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(Color.WHITE);
		g2d.draw(transformed);
		
		
		/*
		g2d.draw(r2);
	    Path2D.Double path = new Path2D.Double();
	    path.append(r, false);
	    AffineTransform t = new AffineTransform();
	    t.rotate(Math.toRadians(45));
	    path.transform(t);
	    g2d.draw(path);
		
		
		Rectangle test = new Rectangle(WIDTH/2, HEIGHT/2, 200, 100);
		Rectangle test2 = new Rectangle(WIDTH/2, HEIGHT/2, 200, 100);
		
		g2d.draw(test2);
		AffineTransform at = AffineTransform.getTranslateInstance(100, 100);
		g2d.rotate(Math.toRadians(45));
		g2d.draw(test);
		*/
		
		
		
		
	}
	

	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (right){
			ship.right();
			Point2D test = new Point(100, 100);
			Point2D test2 = new Point(400, 400);
			
			transform.rotate(Math.toRadians(turnRight), ship.getCenterX(), ship.getCenterY());
			//System.out.println(ship.getCenterY());
			
		}
		
		else if (left){
			ship.left(); 
			
			transform.rotate(Math.toRadians(turnLeft), ship.getCenterX(), ship.getCenterY());

		}
		if (go){
			ship.go();
			
			//ship.x += Math.sin(Math.toRadians(angle)) * 5;
			//ship.y
			/*
			ship.x += (int) Math.sin(Math.toRadians(angle));
			ship.y += (int) Math.cos(Math.toRadians(angle));
			*/
			//System.out.println(Math.sin(Math.toRadians(ship.angle)) * 5 + "y" + Math.cos(Math.toRadians(ship.angle)) * 5);

		}
		else if (back){
			ship.reverse();
		}
		ship.move();
		//ship.decrement();
		/*
		if (transformed.getBounds2D().getX() > 1000){
			
			int[] xCor = {800, 780, 800, 820};
			int[] yCor = {400, 460, 440, 460}; 
			ship.xpoints = xCor;
			ship.ypoints = yCor;
			
			 
			ship.xpoints = xCor;
			ship.ypoints = yCor;
		}
		*/
		transformed = transform.createTransformedShape(ship);
		//System.out.println("X: " + transformed.getBounds2D().getX());
		//System.out.println("Y: " + transformed.getBounds2D().getY());
		//System.out.println(transformed.getBounds2D().getMinX());
		renderer.repaint();
		
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
