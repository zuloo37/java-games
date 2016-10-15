/* To do:
 * Computer making two moves, sometimes not making a move
 * Make hard difficulty. 
*/


//import antigravity;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class TicTacToe 
	extends Canvas
	implements MouseListener
{
	final int DIST = 30; //The distance to the board.
	byte[][] moves = { {0,0,0}, {0,0,0}, {0,0,0} }; //the moves. 0 = null, 1 = player, 2 = computer
	byte winner = 0; //0 = null, 1 = player, 2 = computer, 3 = none
	boolean gameStarted = false;
	boolean playingX = true;
	boolean randStart = true;
	boolean turn = true;
	byte difficulty = 0; //0 = easy, 1 = normal, 2 = hard
	
	
	public void paint( Graphics g)
	{
		//To match the images/buttons
		g.setColor(Color.white);
		g.fillRect(DIST, DIST, 300, 300);
		
		
		if (playingX && !randStart) g.setColor(Color.blue);
		g.fillRect(5, DIST+10, 20, 80);
		if (playingX && !randStart) g.setColor(Color.white);
		
		if (!playingX && !randStart) g.setColor(Color.blue);
		g.fillRect(5, DIST+110, 20, 80);
		if (!playingX && !randStart) g.setColor(Color.white);
		
		if (randStart) g.setColor(Color.blue);
		g.fillRect(5, DIST+210, 20, 80);
		if (randStart) g.setColor(Color.white);
		
		if (difficulty == 0) g.setColor(Color.blue);
		g.fillRect(DIST+5, DIST+310, 90, 30);
		if (difficulty == 0) g.setColor(Color.white);
		
		if (difficulty == 1) g.setColor(Color.blue);
		g.fillRect(DIST+105, DIST+310, 90, 30);
		if (difficulty == 1) g.setColor(Color.white);
		
		if (difficulty == 2) g.setColor(Color.blue);
		g.fillRect(DIST+205, DIST+310, 90, 30);
		if (difficulty == 2) g.setColor(Color.white);
		
		if (randStart && !gameStarted)
		{
			gameStarted = true;
			Random r = new Random();
			if (r.nextBoolean() == true)
			{
				turn = false;
				playingX = false;
			}
			else
			{
				turn = true;
				playingX = true;
			}
		}
		else if (!gameStarted)
		{
			gameStarted = true;
			System.out.println("turn: "+turn+". playingX: "+playingX);
		}
		
		
		//color a square blue for player or red for computer
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				Image img;
				
				if (moves[i][j] == 1 && playingX)
				{
					img = loadImage("x.png");
					g.drawImage(img,DIST+(i*100),DIST+(j*100),this);
				}
				
				if (moves[i][j] == 1 && !playingX)
				{
					img = loadImage("o.png");
					g.drawImage(img,DIST+(i*100),DIST+(j*100),this);
				}
				
				if (moves[i][j] == 2 && playingX)
				{
					img = loadImage("o.png");
					g.drawImage(img,DIST+(i*100),DIST+(j*100),this);
				}
				
				if (moves[i][j] == 2 && !playingX)
				{
					img = loadImage("x.png");
					g.drawImage(img,DIST+(i*100),DIST+(j*100),this);
				}
			}
		}
		
		//draw the grid and little stuff
		for (int i = 0; i <= 300; i+=100)
		{
			g.setColor(Color.black);
			g.drawLine(i+DIST, DIST, i+DIST, 300+DIST);
			g.drawLine(DIST, i+DIST, 300+DIST, i+DIST);
			g.drawString("Playing as... "+( (playingX) ? "X" : "O" ), 5, 20);
			
			g.drawRect(5, DIST+10, 20, 80);
			g.drawString("X", 12, DIST+50);
			g.drawRect(5, DIST+110, 20, 80); 
			g.drawString("O", 10, DIST+150);
			g.drawRect(5, DIST+210, 20, 80); 
			g.drawString("?", 10, DIST+250);
			
			g.drawRect(DIST+5, DIST+310, 90, 30);
			g.drawString("Easy", DIST+35, DIST+330);
			g.drawRect(DIST+105, DIST+310, 90, 30);
			g.drawString("Normal", DIST+130, DIST+330);
			g.drawRect(DIST+205, DIST+310, 90, 30);
			g.drawString("Hard", DIST+235, DIST+330);
		}
		
		//show the winning pop-up if applicable
		if (winner == 1)
			showMessage("You win! Yay!");
		else if (winner == 2)
			showMessage("You lose. Loser.");
		else if (winner == 3)
			showMessage("No winner.");
		
		if (winner == 0 && !turn)
		{
			
			//computer takes a turn
			go();
			check(false);
			turn = true;
			repaint();
		}
	}
	
	
	//pops up a window with a message
	public void showMessage(String msg)
	{
		JOptionPane.showMessageDialog(new Frame(), msg);
		resetGame();
	}

	public TicTacToe()
	{
		addMouseListener(this);
	}
	
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame("Tic-Tac-Toe");
		f.setSize(450,450);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TicTacToe canvas = new TicTacToe();
		f.add(canvas);
		f.setVisible(true);
	}
	

	public void mouseClicked(MouseEvent e) 
	{
		int px = e.getX();
		int py = e.getY();
		
		//main playing field
		if (DIST < px && px < 300+DIST && DIST < py && py < 300+DIST && winner == 0 && turn)
		{
			if (moves[(px-DIST)/100][(py-DIST)/100] == 0)
			{
				System.out.println("You: "+( (px-DIST)/100 )+", "+( (py-DIST)/100) );
				moves[(px-DIST)/100][(py-DIST)/100] = 1;
				//checks for three in a row. true = player, false = computer
				check(true);
				turn = false;
				repaint();
			}
		}
		
		//difficulty
		if (DIST+310 < py && py < 340+DIST)
		{
			if (DIST+5 < px && px < 95+DIST)
			{
				difficulty = 0;
				resetGame();
				System.out.println("Difficulty changed to easy.");
			}
			if (DIST+105 < px && px < 195+DIST)
			{
				difficulty = 1;
				resetGame();
				System.out.println("Difficulty changed to normal.");
			}
			if (DIST+205 < px && px < 295+DIST)
			{
				difficulty = 2;
				resetGame();
				System.out.println("Difficulty changed to hard.");
			}
			
			repaint();
		}
		
		//choosing what to start as
		if (5 < px && px < 25)
		{
			if (DIST+10 < py && py < DIST+90)
			{
				//x
				playingX = true;
				turn = true;
				randStart = false;
				resetGame();
				System.out.println("Playing as X");
			}
			if (DIST+110 < py && py < DIST+190)
			{
				//o
				playingX = false;
				turn = false;
				randStart = false;
				resetGame();
				System.out.println("Playing as O");
			}
			if (DIST+210 < py && py < DIST+290)
			{
				//?
				playingX = true; //null
				randStart = true;
				resetGame();
				System.out.println("Playing as random");
			}
		}
	}
	
	//resets the game
	public void resetGame()
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				moves[i][j] = 0;
			}
		}
		winner = 0;
		gameStarted = false;
		repaint();
		
		if (playingX)
		{
			turn = true;
		}
		if (!playingX)
		{
			turn = false;
		}
		
		if (randStart)
		{
			turn = true;
			playingX = true;
		}
	}
	
	//Computer takes a turn based on randomly generated number.
	//Since it is random, the chances of it making a smart decision are very low. (on easy)
	private void go() 
	{
		Random r = new Random();
		
		//get number of available spots
		int available = 0;
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				if (moves[i][j] == 0)
					available++;
			}
		}
		
		if (difficulty == 0) //easy
		{
			if (available != 0)
			{
				//just choose randomly
				chooseRandom(r, available);
			}
		}
		else if (difficulty == 1) //normal
		{
			//block any 2-in-a-rows with a high chance. Otherwise random.
			if (r.nextInt(10) == 0)
			{
				System.out.println("Computer decided to be derpy");
				chooseRandom(r, available);
			}
			else
			{
				//assigning spaces to variables for ease of access
				byte[] needed = {-1,-1};
				byte a = moves[0][0]; byte b = moves[1][0]; byte c = moves[2][0];
				byte d = moves[0][1]; byte e = moves[1][1]; byte f = moves[2][1];
				byte g = moves[0][2]; byte h = moves[1][2]; byte i = moves[2][2];
				
				//all of this changed to make it just a LITTLE bit more random
				int iter = 0;
				while (needed[0] == -1 && needed[1] == -1)
				{
					int start = r.nextInt(7);
					if (start == 0 || start == 2 || start == 5 || start == 7)
					{
						int extra = r.nextInt(2);
						
						if (start == 0) //a
						{
							if (extra == 0)
								if (a == b && a != 0 && b != 0) {needed[0] = 2; needed[1] = 0;} //c
							if (extra == 1)
								if (a == e && a != 0 && e != 0) {needed[0] = 2; needed[1] = 2;} //i
							if (extra == 2)
								if (a == d && a != 0 && d != 0) {needed[0] = 0; needed[1] = 2;} //g
						}
						if (start == 2) //c
						{
							if (extra == 0)
								if (c == b && c != 0 && b != 0) {needed[0] = 0; needed[1] = 0;} //a
							if (extra == 1)
								if (c == e && c != 0 && e != 0) {needed[0] = 0; needed[1] = 2;} //g
							if (extra == 2)
								if (c == f && c != 0 && f != 0) {needed[0] = 2; needed[1] = 2;} //i
						}
						if (start == 5) //g
						{
							if (extra == 0)
								if (g == d && g != 0 && d != 0) {needed[0] = 0; needed[1] = 0;} //a
							if (extra == 1)
								if (g == e && g != 0 && e != 0) {needed[0] = 2; needed[1] = 0;} //c
							if (extra == 2)
								if (g == h && g != 0 && h != 0) {needed[0] = 2; needed[1] = 2;} //i
						}
						if (start == 7) //i
						{
							if (extra == 0)
								if (i == f && i != 0 && f != 0) {needed[0] = 2; needed[1] = 0;} //c
							if (extra == 1)
								if (i == e && i != 0 && e != 0) {needed[0] = 0; needed[1] = 0;} //a
							if (extra == 2)
								if (i == h && i != 0 && h != 0) {needed[0] = 0; needed[1] = 2;} //g
						}
						
					}
					//odd ones out
					if (start == 1) //b
						if (b == e && b != 0 && e != 0) {needed[0] = 1; needed[1] = 2;} //h
					if (start == 3) //d
						if (d == e && d != 0 && e != 0) {needed[0] = 2; needed[1] = 1;} //f
					if (start == 4) //f
						if (f == e && f != 0 && e != 0) {needed[0] = 0; needed[1] = 1;} //d
					if (start == 6) //h
						if (h == e && h != 0 && e != 0) {needed[0] = 1; needed[1] = 0;} //b
						
					iter++; System.out.println("iter: "+iter);
					if (iter > 15)
					{
						break;
					}
				}
				
				if (needed[0] == -1 || needed[1] == -1)
				{
					chooseRandom(r, available);
				}
				else if (moves[ needed[0] ][ needed[1] ] == 0)
				{
					System.out.println("Computer: "+needed[0]+", "+needed[1]);
					moves[ needed[0] ][ needed[1] ] = 2;
				}
				else chooseRandom(r, available);
			}
		}
		else if (difficulty == 2) //hard
		{
			boolean trololol = true; //access
			
			
			byte a = moves[0][0]; byte b = moves[1][0]; byte c = moves[2][0];
			byte d = moves[0][1]; byte e = moves[1][1]; byte f = moves[2][1];
			byte g = moves[0][2]; byte h = moves[1][2]; byte i = moves[2][2];
			
			//playing?
			if (!playingX)
			{
				//playing as X
				if (available == 9)
				{
					//choose a corner if going first
					int corner = r.nextInt(4);
					if (corner == 0)
						moves[0][0] = 2;
					if (corner == 1)
						moves[2][0] = 2;
					if (corner == 2)
						moves[0][2] = 2;
					if (corner == 3)
						moves[2][2] = 2;
				}
				else if (available == 7)
				{
					//in the center
					if (moves[1][1] == 1)
					{
						if (moves[0][0] == 2) moves[2][2] = 2;
						if (moves[0][2] == 2) moves[2][0] = 2;
						if (moves[2][0] == 2) moves[0][2] = 2;
						if (moves[2][2] == 2) moves[0][0] = 2;
					}
					//on an edge
					else if (moves[1][0] == 1 || moves[0][1] == 1 || moves[1][2] == 1 || moves[2][1] == 1)
						moves[1][1] = 2;
					//in a corner
					else if (moves[0][0] == 1 || moves[0][2] == 1 || moves[2][0] == 1 || moves[2][2] == 1)
					{
						if (moves[0][0] == 2 || moves[2][2] == 2)
						{
							if (moves[0][2] == 0) moves[0][2] = 2;
							if (moves[2][0] == 0) moves[2][0] = 2;
						}
						if (moves[2][0] == 2 || moves[0][2] == 2)
						{
							if (moves[0][0] == 0) moves[0][0] = 2;
							if (moves[2][2] == 0) moves[2][2] = 2;
						}
					}
				}
				else
				{
					goAsdf(a,b,c,d,e,f,g,h,i,r,available);
				}
						
			}
			else if (playingX)
			{
				//playing as O
				if (available == 8)
				{
					if (moves[1][1] == 1)
					{
						int corner = r.nextInt(4);
						if (corner == 0)
							moves[0][0] = 2;
						if (corner == 1)
							moves[2][0] = 2;
						if (corner == 2)
							moves[0][2] = 2;
						if (corner == 3)
							moves[2][2] = 2;
					}
					else
					{
						moves[1][1] = 2;
					}
				}
				else if (available == 6)
				{
					//diagonals
					if ( (moves[0][0] == 1 && moves[2][2] == 1) || (moves[2][0] == 1 && moves[0][2] == 1) )
					{
						int edge = r.nextInt(3);
						if (edge == 0)
							moves[1][0] = 2;
						else if (edge == 1)
							moves[0][1] = 2;
						else if (edge == 2)
							moves[2][1] = 2;
						else if (edge == 3)
							moves[1][2] = 2;
					}
					//edge diagonals
					else if (moves[1][0] == 1 && moves[0][1] == 1)
						moves[0][0] = 2;
					else if (moves[1][0] == 1 && moves[2][1] == 1)
						moves[2][0] = 2;
					else if (moves[0][1] == 1 && moves[1][2] == 1)
						moves[0][2] = 2;
					else if (moves[2][1] == 1 && moves[1][2] == 1)
						moves[2][2] = 2;
					//that weird thing that I think of as an L
					else if (moves[0][0] == 1 && (moves[1][2] == 1 || moves[2][1] == 1) )
						moves[2][2] = 2;
					else if (moves[2][0] == 1 && (moves[0][1] == 1 || moves[1][2] == 1) )
						moves[0][2] = 2;
					else if (moves[0][2] == 1 && (moves[1][0] == 1 || moves[2][1] == 1) )
						moves[2][0] = 2;
					else if (moves[2][2] == 1 && (moves[0][1] == 1 || moves[1][0] == 1) )
						moves[0][0] = 2;
					//block three in a row
					else
					{
						goAsdf(a,b,c,d,e,f,g,h,i,r,available);
					}
				}
				else
				{
					goAsdf(a,b,c,d,e,f,g,h,i,r,available);
				}
			}
		}
		
	}
	
	public void chooseRandom(Random r, int available)
	{
		int choice = 1+r.nextInt(available);
		int cnt = 0;
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				if (moves[i][j] == 0)
					cnt++;
				if (cnt == choice && moves[i][j] == 0)
				{
					System.out.println("Computer: "+i+", "+j);
					moves[i][j] = 2;
				}
			}
		}
	}
	
	public void goAsdf(byte a, byte b, byte c, byte d, byte e, byte f, byte g, byte h, byte i, Random r, int available)
	{
		boolean goAsdfaaaa = false; //access
		//pasted code
		//broken vertical
		if (a == g && a != 0 && (moves[0][1] == 0)) { moves[0][1] = 2;} //d
		else if (b == h && b != 0 && (moves[1][1] == 0)) { moves[1][1] = 2;} //e
		else if (c == i && c != 0 && (moves[2][1] == 0)) { moves[2][1] = 2;} //f
		//broken horizontal
		else if (a == c && a != 0 && (moves[1][0] == 0)) { moves[1][0] = 2;} //b
		else if (d == f && d != 0 && (moves[1][1] == 0)) { moves[1][1] = 2;} //e
		else if (g == i && g != 0 && (moves[1][2] == 0)) { moves[1][2] = 2;} //h
		//broken diagonal
		else if (a == i && a != 0 && (moves[1][1] == 0)) { moves[1][1] = 2;} //e
		else if (c == g && c != 0 && (moves[1][1] == 0)) { moves[1][1] = 2;} //e
		//a
		else if (a == b && a != 0 && (moves[2][0] == 0) ) { moves[2][0] = 2;} //c
		else if (a == e && a != 0 && (moves[2][2] == 0)) { moves[2][2] = 2;} //i
		else if (a == d && a != 0 && (moves[0][2] == 0)) { moves[0][2] = 2;} //g
		//b
		else if (b == e && b != 0 && (moves[1][2] == 0)) { moves[1][2] = 2;} //h
		//c
		else if (c == b && c != 0 && (moves[0][0] == 0)) { moves[0][0] = 2;} //a
		else if (c == e && c != 0 && (moves[0][2] == 0)) { moves[0][2] = 2;} //g
		else if (c == f && c != 0 && (moves[2][2] == 0)) { moves[2][2] = 2;} //i
		//d
		else if (d == e && d != 0 && (moves[2][1] == 0)) { moves[2][1] = 2;} //f
		//f
		else if (f == e && f != 0 && (moves[0][1] == 0)) { moves[0][1] = 2;} //d
		//g
		else if (g == d && g != 0 && (moves[0][0] == 0)) { moves[0][0] = 2;} //a
		else if (g == e && g != 0 && (moves[2][0] == 0)) { moves[2][0] = 2;} //c
		else if (g == h && g != 0 && (moves[2][2] == 0)) { moves[2][2] = 2;} //i
		//h
		else if (h == e && h != 0 && (moves[1][0] == 0)) { moves[1][0] = 2;} //b
		//i
		else if (i == f && i != 0 && (moves[2][0] == 0)) { moves[2][0] = 2;} //c
		else if (i == e && i != 0 && (moves[0][0] == 0)) { moves[0][0] = 2;} //a
		else if (i == h && i != 0 && (moves[0][2] == 0)) { moves[0][2] = 2;} //g
		else chooseRandom(r, available);
	}
	

	//checks for three in a row
	private void check(boolean player) 
	{
		boolean winning = false;
		
		//Fun! assigning the spaces to variables for ease of access
		byte a = moves[0][0]; byte b = moves[1][0]; byte c = moves[2][0];
		byte d = moves[0][1]; byte e = moves[1][1]; byte f = moves[2][1];
		byte g = moves[0][2]; byte h = moves[1][2]; byte i = moves[2][2];
		
		//multiplied together... Interesting, isn't it? 
		//All of them must be 1 for the outcome to be 1, and the same goes for 2 with 8!
		
		//check rows...
		if (a*b*c == 1 || a*b*c == 8)
			winning = true;
		else if (d*e*f == 1 || d*e*f == 8)
			winning = true;
		else if (g*h*i == 1 || g*h*i == 8)
			winning = true;
		
		//check columns...
		if (a*d*g == 1 || a*d*g == 8)
			winning = true;
		else if (b*e*h == 1 || b*e*h == 8)
			winning = true;
		else if (c*f*i == 1 || c*f*i == 8)
			winning = true;
		
		//check diagonals...
		if (a*e*i == 1 || a*e*i == 8)
			winning = true;
		else if (c*e*g == 1 || c*e*g == 8)
			winning = true;
		
		//make them win
		if (winning)
		{
			winner = (player) ? (byte)1 : (byte)2;
			System.out.println("Winner value: "+winner);
		}
		//see if there is no winner
		else
		{
			//get available spaces
			int available = 0;
			for (int x = 0; x < 3; x++)
			{
				for (int y = 0; y < 3; y++)
				{
					if (moves[x][y] == 0)
						available++;
				}
			}
			if (available == 0)
			{
				winner = 3; //no winner
			}
		}
	}
	
	public Image loadImage(String location)
	{
		try
		{
			Image image = ImageIO.read( new File(location) );
			return image;
		}
		catch (IOException ex)
		{
			System.err.println("Error in loading image.");
		}
		return null;
	}
	
	//useless stuff
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}
