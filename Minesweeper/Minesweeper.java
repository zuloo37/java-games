//make it generate on the first click
//make settings changeable and new games possible

import javax.imageio.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Random;

public class Minesweeper 
	extends Canvas
	implements MouseListener
{
	
	public Minesweeper() {
		init();
	}
	
	boolean start = true;
	boolean lost = false;
	int length = 8;
	int height = 8;
	byte[][] numbers = new byte[length][height];
		//0-8 - actual numbers
		//9 - unclicked
		//10 - bomb
		//11 - flag (wrong)
		//12 - flag (right)
		//13 - unknown (normal)
		//14 - unknown (bomb)
	int numMines = 5;
	final Color gray = new Color(200,200,200);
	Image flag;
	
	public void paint(Graphics g)
	{	
		g.drawRect(20, 20, 50, 20);
		g.drawString("length", 25, 35);
		g.drawRect(75, 20, 50, 20);
		g.drawString("height", 80, 35);
		g.drawRect(130, 20, 50, 20);
		g.drawString("mines", 135, 35);
		
		
		for (int i = 0; i < numbers.length; i++)
		{
			for (int j = 0; j < numbers[i].length; j++)
			{
				if (numbers[i][j] == 9)
				{
					g.setColor(gray);
					g.fillRect(20+i*20, 60+j*20, 20, 20);
				}
				else if (numbers[i][j] == 10)
				{
					g.setColor(gray);
					if (lost)
						g.setColor(Color.darkGray);
					g.fillRect(20+i*20, 60+j*20, 20, 20);
				}
				else if (numbers[i][j] == 11 || numbers[i][j] == 12)
				{
					g.drawImage(flag, 20+i*20, 60+j*20, this);
				}
				else if (numbers[i][j] == 13 || numbers[i][j] == 14)
				{
					g.setColor(gray);
					g.fillRect(20+i*20, 60+j*20, 20, 20);
					g.setColor(Color.black);
					g.drawString("?", 28+i*20, 75+j*20);
				}
				else if (numbers[i][j] == 0)
				{
					g.setColor(Color.white);
					g.fillRect(20+i*20, 60+j*20, 20, 20);
				}
				else
				{
					g.setColor(Color.white);
					g.fillRect(20+i*20, 60+j*20, 20, 20);
					g.setColor(Color.black);
					g.drawString(""+numbers[i][j]+"", 28+i*20, 75+j*20);
					
				}
			}
		}
		
		g.setColor(Color.black);
		for (int i = 0;i <= height;i++)
		{
			g.drawLine(20, 60+i*20, 20+20*length, 60+i*20);
		}
		for (int i = 0;i <= length;i++)
		{
			g.drawLine(20+i*20, 60, 20+i*20, 60+height*20);
		}
	}
	
	public void init()
	{
		addMouseListener(this);
		try {
			flag = ImageIO.read(new File("flag.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < length; i++)
		{
			for (int j = 0; j < height; j++)
			{
				numbers[i][j] = 9;
			}
		}
	}
	
	public static void main( String[] args )
	{
		JFrame f = new JFrame("Minesweeper");
		f.setSize(800,600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Minesweeper canvas = new Minesweeper();
		f.add(canvas);
		f.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) 
	{
		int px = e.getX();
		int py = e.getY();
		
		if (20 < px & px < 70 && 20 < py && py < 40)
		{
			length = getInput("Length will be?");
			if (length == -1) length = numbers.length;
			if (length > 24) length = 24;
			numbers = new byte[length][height];
			
			for (int i = 0; i < length; i++)
			{
				for (int j = 0; j < height; j++)
				{
					numbers[i][j] = 9;
				}
			}
			
			lost = false;
			start = true;
			repaint();
		}
		if (75 < px && px < 125 && 20 < py && py < 40)
		{
			height = getInput("Height will be?");
			if (height == -1) height = numbers[0].length;
			if (height > 24) height = 24;
			numbers = new byte[length][height];
			
			for (int i = 0; i < length; i++)
			{
				for (int j = 0; j < height; j++)
				{
					numbers[i][j] = 9;
				}
			}
			
			lost = false;
			start = true;
			repaint();
		}
		if (130 < px && px < 180 && 20 < py && py < 40)
		{
			int n = getInput("Number of mines will be?");
			if (n == -1) n = numMines;
			if (n > length*height - 1)
				numMines = length*height - 15;
			
			for (int i = 0; i < length; i++)
			{
				for (int j = 0; j < height; j++)
				{
					numbers[i][j] = 9;
				}
			}
			
			lost = false;
			start = true;
			repaint();
		}
		
		if (20 < px && px < 20+length*20 && 60 < py && py < 60+height*20)
		{
			//clicking inside the box...
			int x = (px-20)/20;
			int y = (py-60)/20;
			
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				if (start || lost)
				{
					repaint();
					Random r = new Random();
					
					int count = 0;
					for (int i = 0; i < numMines; i++)
					{
						boolean valid = false;
						
						while (!valid)
						{
							int rx = r.nextInt(length);
							int ry = r.nextInt(height);
							
							if (numbers[rx][ry] != 10 && !(rx == x && ry == y) )
							{
								valid = true;
								numbers[rx][ry] = 10;
							}
						}
					}
					
					start = false;
					lost = false;
				}
				
				
				if (numbers[x][y] == 9)
				{
					check(x, y);
					if (numbers[x][y] == 0)
						checkAdjacents(x, y);
					if (checkWin())
						win();
					repaint();
				}
				else if (numbers[x][y] == 10)
				{
					lost = true;
					repaint();
					showMessage("You lose. Loser.");
					
					for (int i = 0; i < length; i++)
					{
						for (int j = 0; j < height; j++)
						{
							numbers[i][j] = 9;
						}
					}
					
					lost = false;
					start = true;
					repaint();
				}
			}
			//switching between flags, ?s, and normal with right click
			else if (e.getButton() == MouseEvent.BUTTON3)
			{
				if (numbers[x][y] == 9)
				{
					numbers[x][y] = 11;
					repaint();
				}
				else if (numbers[x][y] == 10)
				{
					numbers[x][y] = 12;
					if (checkWin())
						win();
					repaint();
				}
				else if (numbers[x][y] == 11)
				{
					numbers[x][y] = 13;
					repaint();
				}
				else if (numbers[x][y] == 12)
				{
					numbers[x][y] = 14;
					repaint();
				}
				else if (numbers[x][y] == 13)
				{
					numbers[x][y] = 9;
					repaint();
				}
				else if (numbers[x][y] == 14)
				{
					numbers[x][y] = 10;
					if (checkWin())
						win();
					repaint();
				}
			}
		}
	}
	
	public void check(int x, int y)
	{
		byte adj = 0;
		
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (x+i >= 0 && x+i < length && y+j >= 0 && y+j < height)
				{
					if (numbers[x+i][y+j] == 10 || numbers[x+i][y+j] == 12 || numbers[x+i][y+j] == 14) 
						adj++;
				}
			}
		}
		
		numbers[x][y] = adj;
	}
	
	public void checkAdjacents(int x, int y)
	{
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (x+i >= 0 && x+i < length && y+j >= 0 && y+j < height)
				{
					if (numbers[x+i][y+j] == 9)
					{
						check(x+i, y+j);
						if (numbers[x+i][y+j] == 0)
							checkAdjacents(x+i, y+j);
					}
				}
			}
		}
	}
	
	public boolean checkWin()
	{
		int count = 0;
		int bad = 0;
		for (int i = 0; i < length; i++)
		{
			for (int j = 0; j < height; j++)
			{
				if (numbers[i][j] == 9 || numbers[i][j] == 11 || numbers[i][j] == 13 || numbers[i][j] == 14)
					bad++;
			}
		}
		
		if (bad == 0)
			return true;
		
		return false;
	}
	
	public void win()
	{
		repaint();
		showMessage("You win! Yay!");
		
		for (int i = 0; i < length; i++)
		{
			for (int j = 0; j < height; j++)
			{
				numbers[i][j] = 9;
			}
		}
		
		lost = false;
		start = true;
	}
	
	//shows JOptionPane for integer input
	public int getInput(String msg)
	{
		String input = JOptionPane.showInputDialog(msg);
		int n = 0;
		try
		{
     		n = Integer.parseInt(input);
     		if (n < 5) 
     		{
	     		n = 5;
     		}
		}
 		catch (NumberFormatException e)
 		{
 			n = -1;
		}
		return n;
	}
	
	//loads an image
	public Image loadImage( String location ) 
	{
		try
		{
			Image img = ImageIO.read( new File(location) );
			return img;
		}
		catch ( Exception e )
		{
			System.exit(1);
		}

		return null;
	}
	
	//pops up a window with a message
	public void showMessage(String msg)
	{
		JOptionPane.showMessageDialog(new Frame(), msg);
	}
	
	//useless shit
	public void mouseEntered(MouseEvent e) 
	{
	}

	public void mouseExited(MouseEvent e) 
	{
	}

	public void mousePressed(MouseEvent e) 
	{
	}

	public void mouseReleased(MouseEvent e) 
	{
	}
}
