package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import sprites.*;

public class RefactoredGame extends JPanel{
	
	private RefactoredSnake snake;
	private Apple apple;
	
	public RefactoredGame() {
		
		setBackground(Color.DARK_GRAY);
		
		apple = new Apple();
		snake = new RefactoredSnake();
		
		snake.setApple(apple.getPoint());
		
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				
				snake.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		setFocusable(true);
	}
	
	public void paint(Graphics g) {
		
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		apple.paint(g2);
		snake.paint(g2);
	}
	
	//head collision works but the apple is choosing to be a little bitch for no reason >:( it stays in place sometimes and it irritates me
	public void move() {
		if(snake.move()) {
			do {
				apple.move();
			} while(snake.inTail(true) || (snake.head()[0] == apple.getPoint()[0] && snake.head()[1] == apple.getPoint()[1]));
			snake.setApple(apple.getPoint());
		}
	}
	
	public void gameOver() {
		JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}
	
	public static void main(String[] args) throws InterruptedException{
		
		JFrame frame = new JFrame("Snake");
		RefactoredGame game = new RefactoredGame();
		
		game.setPreferredSize(new Dimension(Constants.boardSize * Constants.cellSize + Constants.bufferSize * (Constants.boardSize - 1), Constants.boardSize * Constants.cellSize + Constants.bufferSize * (Constants.boardSize - 1)));
		frame.getContentPane().add(game);
		frame.pack();
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		while(!game.snake.crash()) {
			if(game.snake.noInput()) {
				game.move();
			}
			game.snake.setNoInput(true);
			game.repaint();
			Thread.sleep(Constants.tick);
		}
		
		game.gameOver();
	}
}
