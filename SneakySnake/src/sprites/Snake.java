package sprites;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import main.*;

public class Snake extends JPanel{
	
	//needed for now...might change/disappear in refactoring
	private Game game;
	
	//all of the body segments are here... please note that the head is separate from the rest of the body
	private int headX, headY;
	private ArrayList<Integer> xSet, ySet;
	
	//just some useful values like how big dis boi is and what input it last ate
	private int size;
	private int lastKey;
	
	//how dis boi movin
	private boolean up, down, left, right;
	
	public Snake(Game g) {
		game = g;
		reset();
	}
	
	public void reset() {
		headX = Constants.headStart_x;
		headY = Constants.headStart_y;
		
		xSet = new ArrayList<Integer>();
		ySet = new ArrayList<Integer>();
		
		size = 3;
		lastKey = 0;
		
		up = false;
		down = false;
		left = false;
		right = true;
	}
	
	//change dis boi's location based on where he goin
	public void move() {
		//updating the tail stuff
		xSet.add(headX);
		ySet.add(headY);
		if(xSet.size() >= size) {
			xSet.remove(0);
			ySet.remove(0);
		}
		
		//changing the head position
		if(up) {
			headY -= 1;
		}
		else if(down) {
			headY += 1;
		}
		else if(left) {
			headX -= 1;
		}
		else if(right) {
			headX += 1;
		}
		
		//did we hit the apple?? 
		if(headX == game.apple.getPoint()[0] && headY == game.apple.getPoint()[1]) {
			size += Constants.lengthIncrease;
			game.apple.move();
			while(inTail(game.apple.getPoint())) {
				game.apple.move();
			}
		}
		
		//did we crash??
		if(crash()) {
			game.gameOver();
		}
	}
	
	//is dere a boot in dis snake??
	public boolean inTail(int[] apple) {
		boolean ret = false;
		ret = headX == game.apple.getPoint()[0] && headY == game.apple.getPoint()[1] ? true : ret;
		for(int i = 0; i < xSet.size(); i++) {
			ret = xSet.get(i) == game.apple.getPoint()[0] && ySet.get(i) == game.apple.getPoint()[1] ? true : ret;
		}
		return ret;
	}
	
	//did we hit something?? LETS LOOP AND FIND OUT
	public boolean crash() {
		boolean ret = headX >= Constants.boardSize || headX < 0 || headY >= Constants.boardSize || headY < 0;
		for(int i = 0; i < xSet.size(); i++) {
			ret = headX == xSet.get(i) && headY == ySet.get(i) ? true : ret;
		}
		return ret;
	}
	
	//make dis boi appear on screen
	public void paint(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(headX * (Constants.cellSize + Constants.bufferSize), headY * (Constants.cellSize + Constants.bufferSize), Constants.cellSize, Constants.cellSize);
		for(int i = 0; i < xSet.size(); i++) {
			g.fillRect(xSet.get(i) * (Constants.cellSize + Constants.bufferSize), ySet.get(i) * (Constants.cellSize + Constants.bufferSize), Constants.cellSize, Constants.cellSize);
		}
	}
	
	//how to get keyboard inputs
	//using WASD controls
	//activates whenever a new key is pressed
	public void keyPressed(KeyEvent e) {
		//compares new and old inputs to see if a change is needed (fixes zoomin bug)
		//validIn is used to make sure the user isnt spamming non-WASD keys to get a speed boost
		boolean validIn = true;
		if(lastKey != e.getKeyCode()) {
			//reassign booleans for directions
			switch(e.getKeyCode()) {
				case KeyEvent.VK_W:
					up = !down;
					left = false;
					right = false;
					break;
				case KeyEvent.VK_A:
					left = !right;
					up = false;
					down = false;
					break;
				case KeyEvent.VK_S:
					down = !up;
					left = false;
					right = false;
					break;
				case KeyEvent.VK_D:
					right = !left;
					up = false;
					down = false;
					break;
				default:
					validIn = false;
					break;
			}
			if(validIn) {
				move();
				game.noInput = false;
				lastKey = e.getKeyCode();
			}
		}
	}
}
