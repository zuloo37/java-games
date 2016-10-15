/* Finally done. Made by Sean O'Neil.

Notes:
	Dealer wins ties
	If both players stay, the game should be over.
*/

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;

public class BlackJack
	extends Canvas
		implements MouseListener
{
	static BlackJack canvas;

	//for dealing two starting cards or redrawing what is there
	boolean start = true;
	
	//for random
	Random r = new Random();
	int rcard; int rsuit; 
		
	//useful stuff	
	int i; int j; //iteration
	int h_c = 0; //player height
	int h_d = 0; //dealer height

	//varibles for printing
	String n = ""; String n1 = ""; String n2 = "";
	
	//varibles for summation
	int ts = 0;
	int[] sum = new int[2]; int[] sum2 = new int[2];
	boolean use_sumL1 = false; boolean use_sum2L1 = false;
	boolean bust = false; boolean bust2 = false;
	int finsum = 0; int finsum2 = 0;
	String winner = "";
	//Aces (Because they are 1 or 11)
	boolean ace = false; boolean ace2 = false;
	int ace_n = 0; int ace_n2 = 0;
	boolean aces_added = false; boolean aces_added2 = false;
	
	//variables for betting
	int coins = 10; 
	int bet = 0;
	int numwins = 0;
	boolean numwins_added = false;

	//to see whether they hit or stay
	boolean hit = false;
	boolean stay = false; boolean stay2 = false;

	//tables of cards and their color to load
	String[] card = new String[13]; String[] card2 = new String[13];
	Color[] scol = new Color[13]; Color[] scol2 = new Color[13];

	//tables for used cards and their names
	String[][] deck = { {"A S","A H","A C","A D"}, {"2 S","2 H","2 C","2 D"}, {"3 S","3 H","3 C","3 D"}, {"4 S","4 H","4 C","4 D"}, {"5 S","5 H","5 C","5 D"}, {"6 S","6 H","6 C","6 D"}, {"7 S","7 H","7 C","7 D"}, {"8 S","8 H","8 C","8 D"}, {"9 S","9 H","9 C","9 D"}, {"10S","10H","10C","10D"}, {"J S","J H","J C","J D"}, {"Q S","Q H","Q C","Q D"}, {"K S","K H","K C","K D"} };
	boolean[][] used = { {false,false,false,false}, {false,false,false,false}, {false,false,false,false}, {false,false,false,false}, {false,false,false,false}, {false,false,false,false}, {false,false,false,false}, {false,false,false,false}, {false,false,false,false}, {false,false,false,false}, {false,false,false,false}, {false,false,false,false}, {false,false,false,false} };
	Image[][] imgs = new Image[12][3]; 
		
	//Main paint function
	public void paint( Graphics g )
	{
		//Words and buttons
		g.setFont(new Font("System", Font.BOLD, 60));
		g.drawString("Blackjack",270,60);
		g.setFont(new Font("System", Font.BOLD, 20));
		g.drawString("Your cards",220,90);
		g.drawString("Dealer's cards",510,90);
		g.drawString("Coins: " + coins,40,40);
		g.drawString("Bet: " + bet,40,70);
		
		//The boxes for hit, stay, and bet
		g.setColor(Color.white);
		g.fillRoundRect(25,150,100,100,10,10);
		g.fillRoundRect(25,350,100,100,10,10);
		g.fillRoundRect(25,500,100,100,10,10);
		
		//hit, stay, and bet
		g.setColor(Color.black);
		g.drawRoundRect(25,150,100,100,10,10);
		g.drawString("Hit",62,208);
		g.drawRoundRect(25,350,100,100,10,10);
		g.drawString("Stay",54,408);
		g.drawRoundRect(25,500,100,100,10,10);
		g.drawString("Bet",58,558);

		//redraw
		if (!start)
		{
			//line 187	public void drawCard( Graphics g, String number, Color suitcolor, int height, boolean player, boolean hide )

			for (i = 0; i <= h_c; i++) 
			{
				drawCard(g, card[i], scol[i], i, true, false);
			}
			for (i = 0; i <= h_d; i++)
			{
				if (i == h_d) {drawCard(g, card2[i], scol2[i], i, false, true);}
				else {drawCard(g, card2[i], scol2[i], i, false, false);}
			}
		}
		else
		{
			//choose two cards for the player and dealer and draw them
			//line 247 public void chooseCard( Random r, int h, boolean player )
			
			//layer 0
			h_c = 0;
			chooseCard(r, h_c, true);
			drawCard(g, card[h_c], scol[h_c], h_c, true, false);

			h_d = 0;
			chooseCard(r, h_d, false);
			drawCard(g, card2[h_d], scol2[h_d], h_d, false, false);


			//layer 1
			h_c++;
			chooseCard(r, h_c, true);
			drawCard(g, card[h_c], scol[h_c], h_c, true, false);

			h_d++;
			chooseCard(r, h_d, false);
			drawCard(g, card2[h_d], scol2[h_d], h_d, false, true);
			//The last card that the dealer has is hidden

			//line 449 public void sum()
			sum();

			start = false;
		}

		//Visual sum
		g.setColor(Color.black);
		g.drawString("Your sum: ",20,300);
		if (sum[1] > 21)
		{
			g.setColor(Color.red);
			g.drawString("" + sum[1] + "",123,300);
		}

		if (sum[1] < sum[0] && sum[0] > 21 && sum[1] <= 21)
		{
			g.setColor(Color.green);
			g.drawString("" + sum[1] + "",123,300);
		}

		if (sum[0] <= 21 && sum[1] <= 21)
		{
			g.setColor(Color.blue);
			g.drawString("" + sum[0] + "",123,300);
		}

		if (winner.equals("Player"))
		{
			drawWinBox(g, true);
		}

		if (winner.equals("Dealer"))
		{
			drawWinBox(g, false);
		}
	}

	public static void main( String[] args)
	{
		//create a window
		JFrame f = new JFrame("BlackJack");
		f.setSize(800,850);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas = new BlackJack();
		f.add(canvas);
		//ContentPane needed to get the background...
		Container con = f.getContentPane();  
		con.setBackground(new Color(190,190,190));   
		f.setVisible(true);
	}

	public void drawCard( Graphics g, String number, Color suitcolor, int height, boolean player, boolean hide )
	{
		//cx and cy are for card location
		int cx = 0;
		if (player == true) {cx = 200;}
		if (player == false) {cx = 500;}
		int cy = 100 + (45 * height);

		//border
		g.setColor(Color.white);
		g.fillRoundRect(cx + 1,cy + 1,149,224,16,17);
		
		Image cardImage;

		//draw the number, suit, and image
		//line 642 public Image loadImage( String location ) 
		g.setFont(new Font("System", Font.BOLD, 20));
		if (!hide)
		{
			n1 = number.substring(0,2);
			n2 = number.substring(number.length()-1);
			
			String tn1 = (n1.equals("10")) ? n1 : n1.substring(0,1);
			cardImage = loadImage(""+n2.toLowerCase()+tn1.toLowerCase()+".png");
			g.drawImage(cardImage,cx+15,cy+20,this);
			
			g.setColor(suitcolor);
			
			//for unicode
			String unisuit = "";
			if (n2.equals("C")) unisuit = "\u2663";
			if (n2.equals("S")) unisuit = "\u2660";
			if (n2.equals("H")) unisuit = "\u2665";
			if (n2.equals("D")) unisuit = "\u2666";

			g.drawString(n1, cx + 10, cy + 20);
			g.drawString(unisuit, cx + 10, cy + 40);
			
			if (n1.equals("10")) {g.drawString(n1, cx + 118, cy + 215);}
			else {g.drawString(n1, cx + 125, cy + 215);}
			
			g.drawString(unisuit, cx + 125, cy + 195);
			g.drawRoundRect(cx,cy,150,225,20,20);
		}
		//for the hidden card
		if (hide)
		{
			cardImage = loadImage("defaultcard.png");
			g.drawImage(cardImage,cx+15,cy+20,this);
			
			g.setColor(new Color(128,128,128));
			g.drawString("?", cx + 10, cy + 20);
			g.drawString("?", cx + 10, cy + 40);
			g.drawString("?", cx + 125, cy + 215);
			g.drawString("?", cx + 125, cy + 195);
			g.drawRoundRect(cx,cy,150,225,20,20);
		}
	}

	//chooses a card randomly based on what's left in the deck
	public void chooseCard( Random r, int h, boolean player )
	{
		//Don't let a card that's already out be chosen
		do
		{
			rcard = r.nextInt(13) + 0;
			rsuit = r.nextInt(4) + 0;

			if (used[rcard][rsuit] == false)
			{
				used[rcard][rsuit] = true;
			}
			else
			{
				used[rcard][rsuit] = false;
			}
		} while (used[rcard][rsuit] == false);

		//setting suits and scol[]/card[]
		if (player)
		{
			if (rsuit == 0 || rsuit == 2) {scol[h] = Color.black;}
			if (rsuit == 1 || rsuit == 3) {scol[h] = Color.red;}
			card[h] = deck[rcard][rsuit];
		}

		if (!player)
		{
			if (rsuit == 0 || rsuit == 2) {scol2[h] = Color.black;}
			if (rsuit == 1 || rsuit == 3) {scol2[h] = Color.red;}
			card2[h] = deck[rcard][rsuit];
		}
	}

	//constructor
	public BlackJack()
	{
		//for mouse functions
		addMouseListener(this);
	}

	public void mouseClicked( MouseEvent evt )
	{
		//location of click
		int px = evt.getX();
		int py = evt.getY();

		//bbox: 285,275,100,50
		if (px > 285 && px < 385 && py > 275 && py < 325 && !winner.equals("") && !start) //CONTINUE
		{
			//reset things to start a new game
			n = ""; n1 = ""; n2 = "";
			ts = 0;
			sum[0] = 0; sum[1] = 0; sum2[0] = 0; sum2[1] = 0;
			use_sumL1 = false; use_sum2L1 = false;
			bust = false; bust2 = false;
			finsum = 0; finsum2 = 0;
			winner = "";
			numwins_added = false;

			ace = false; ace2 = false;
			ace_n = 0; ace_n2 = 0;
			aces_added = false; aces_added2 = false;

			hit = false;
			stay = false; stay2 = false;

			for (i = 0; i <= h_c; i++)
			{
				card[i] = "";
				scol[i] = null;
			}
			for (i = 0; i <= h_d; i++)
			{
				card2[i] = "";
				scol2[i] = null;
			}

			i = 0; j = 0; 
			h_c = 0;
			h_d = 0; 

			start = true;
			bet = 0;
		}

		//bbox: 465,275,100,50
		if (px > 465 && px < 565 && py > 275 && py < 325 && !winner.equals("") && !start) //EXIT
		{
			System.exit(0);
		}

		//bbox: 25,500,100,100
		if (px > 25 && px < 125 && py > 500 && py < 600 && winner.equals("") && !start) //BET
		{
			if (h_c > 1 || h_d > 1)
			{
				//line 730 public void showMessage(String msg)
				canvas.showMessage("You can't bet after the game has started.");
			}
			else
			{
				//line 701 public void showBetWin()
				canvas.showBetWin();
			}
		}

		//bbox: 25,150,100,100
		if (px > 25 && px < 125 && py > 150 && py < 250 && winner.equals("") && !start) //HIT
		{
			//line 604 public void decide()
			//line 247 public void chooseCard( Random r, int h, boolean player )
			stay = false;
			//"hit" the player
			h_c++;
			chooseCard(r, h_c, true);

			decide();
			if (hit)
			{
				h_d++;
				chooseCard(r, h_d, false);
			}

			sum();

			finsum = use_sumL1 ? sum[1] : sum[0];
			finsum2 = use_sum2L1 ? sum2[1] : sum2[0];

			//decides whether it's a bust or not for each player.
			if (bust && bust2)
			{
				winner = "Dealer";
			}
			if (bust == true && bust2 == false)
			{
				winner = "Dealer";
			}
			if (bust == false && bust2 == true)
			{
				winner = "Player";
			}

			if (!hit && stay)
			{
				if (!bust && !bust2)
				{
					winner = finsum2 >= finsum ? "Dealer" : "Player";
				}
			}
		}

		//bbox: 25,350,100,100
		if (px > 25 && px < 125 && py > 350 && py < 450 && winner.equals("") && !start) //STAY
		{
			//line 604 public void decide()
			//line 247 public void chooseCard( Random r, int h, boolean player )
			stay = true;
			decide();
			if (hit)
			{
				h_d++;
				chooseCard(r, h_d, false);
			}

			sum();


			finsum = use_sumL1 ? sum[1] : sum[0];
			finsum2 = use_sum2L1 ? sum2[1] : sum2[0];

			if (bust && bust2)
			{
				winner = "Dealer";
			}
			if (bust && !bust2)
			{
				winner = "Dealer";
			}
			if (!bust && bust2)
			{
				winner = "Player";
			}

			if (!hit && stay)
			{
				if (!bust && !bust2)
				{
					winner = finsum2 >= finsum ? "Dealer" : "Player";
				}
			}
		}
		stay = false;
		//add/subtract the coins from the bet
		if (!winner.equals(""))
		{
			coins += winner.equals("Player") ? bet : -bet;
		}
		repaint();
	}

	//gets sums and whether they have busted
	public void sum() 
	{
		int[] to_sum = new int[20]; int[] to_sum2 = new int[20];
		sum[0] = 0; sum[1] = 0; sum2[0] = 0; sum2[1] = 0;

		ts = 0;

		ace = false; ace2 = false;
		ace_n = 0; ace_n2 = 0;
		aces_added = false; aces_added2 = false;

		for (i = 0; i <= h_c; i++)
		{
			n = "";
			ts = 0;
			n = card[i].substring(0,2);

			//convert Strings to ints
			if (n.equals("J ") || n.equals("Q ") || n.equals("K ") || n.equals("10")) {ts = 10;}
			if (n.equals("2 ")) {ts = 2;}
			if (n.equals("3 ")) {ts = 3;}
			if (n.equals("4 ")) {ts = 4;}
			if (n.equals("5 ")) {ts = 5;}
			if (n.equals("6 ")) {ts = 6;}
			if (n.equals("7 ")) {ts = 7;}
			if (n.equals("8 ")) {ts = 8;}
			if (n.equals("9 ")) {ts = 9;}

			if (n.equals("A "))
			{
				if (ace)
				{
					ts = 0;
					ace_n++;
				}
				if (!ace)
				{
					ts = 0;
					ace = true;
					ace_n++;
				}
			}
			to_sum[i] = ts;
		}

		for (i = 0; i <= h_d; i++)
		{
			n = "";
			ts = 0;

			n = card2[i].substring(0,2);

			//convert Strings to ints
			if (n.equals("J ") || n.equals("Q ") || n.equals("K ") || n.equals("10")) {ts = 10;}
			if (n.equals("2 ")) {ts = 2;}
			if (n.equals("3 ")) {ts = 3;}
			if (n.equals("4 ")) {ts = 4;}
			if (n.equals("5 ")) {ts = 5;}
			if (n.equals("6 ")) {ts = 6;}
			if (n.equals("7 ")) {ts = 7;}
			if (n.equals("8 ")) {ts = 8;}
			if (n.equals("9 ")) {ts = 9;}

			if (n.equals("A "))
			{
				if (ace2)
				{
					ts = 0;
					ace_n2++;
				}
				if (!ace2)
				{
					ts = 0;
					ace2 = true;
					ace_n2++;
				}
			}
			to_sum2[i] = ts;
		}

		sum[0] = 0;
		for (i = 0; i < to_sum.length; i++)
		{
			if (ace && !aces_added)
			{
				sum[0] += 11 + (1 * (ace_n - 1));
				sum[1] += 1 * ace_n;
				aces_added = true;
			}

			sum[0] += to_sum[i];
			sum[1] += to_sum[i];
		}

		sum2[0] = 0;
		for (i = 0; i < to_sum2.length; i++)
		{
			if (ace2 && !aces_added2)
			{
				sum2[0] += 11 + (1 * (ace_n2 - 1));
				sum2[1] += 1 * ace_n2;
				aces_added2 = true;
			}

			sum2[0] += to_sum2[i];
			sum2[1] += to_sum2[i];
		}

		if (!use_sumL1 || !use_sum2L1)
		{
			if (sum[0] > 21 && sum[1] <= 21){use_sumL1 = true;}

			if (sum2[0] > 21 && sum2[1] <= 21){use_sum2L1 = true;}
			if (sum[0] > 21 && sum[1] > 21)
			{
				bust = true;
			}
			if (sum2[0] > 21 && sum2[1] > 21)
			{
				bust2 = true;
			}
			if (sum[0] <= 21)
			{
				bust = false;
			}
			if (sum2[0] <= 21)
			{
				bust2 = false;
			}
		}
		if (use_sumL1)
		{
			if (sum[1] <= 21)
			{
				bust = false;
			}
			else
			{
				bust = true;
			}
		}
		if (use_sum2L1)
		{
			if (sum2[1] <= 21)
			{
				bust2 = false;
			}
			else
			{
				bust2 = true;
			}
		}
	}

	//uses probability and random numbers to decide whether the dealer should hit or stay
	public void decide()
	{
		hit = false;

		if (!use_sum2L1 && sum2[0] <= 11)
		{
			hit = true;
		}
		else if (use_sum2L1 && sum2[1] <= 11)
		{
			hit = true;
		}
		else
		{
			int sum_n = 0;
			int to_req;
			int req;

			if (sum2[1] < sum2[0] && use_sum2L1){to_req = sum2[1];}
			else {to_req = sum2[0];}

			req = 21 - to_req;

			for (i = 0; i <= req - 1; i++)
			{
				for (j = 0; j <= 3; j++)
				{
					if (!used[(req - 1) - i][j]){sum_n++;}
				}
			}

			double prob = (sum_n / 52.0) * 100.0;
			int probt = r.nextInt(100) + 1;
			hit = probt <= prob ? true : false;
		}
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

	//draws the window when each game is over.
	public void drawWinBox(Graphics g, boolean player)
	{
		if (!numwins_added)
		{
			numwins = player ? numwins + 1 : 0;
			numwins_added = true;
		}

		g.setColor(Color.lightGray);
		g.fillRoundRect(225,150,400,200,20,20);
		g.setColor(Color.black);
		g.drawRoundRect(225,150,400,200,20,20);

		g.setColor(Color.white);
		g.fillRect(285,275,100,50);
		g.fillRect(465,275,100,50);
		g.setColor(Color.black);
		g.drawRect(285,275,100,50);
		g.drawRect(465,275,100,50);

		g.setColor(Color.black);
		g.drawString("Exit",495,310);
		g.drawString("Again",308,310);

		String isPlural = numwins > 1 ? "s" : "";
		String WinMessage = player ? "You won! You've won "+numwins+" game"+isPlural+" in a row." : "You lost!";
		g.drawString(WinMessage,235,200);
		int SelSum = use_sum2L1 ? sum2[1] : sum2[0];
		g.drawString("The dealer had a sum of " + SelSum + ".",235,225);

		String changeType = player ? "earned" : "lost";
		if (coins > 0)
		{
			g.drawString("You "+changeType+" "+bet+" coins.", 235,250);
		}
		else
		{
			g.drawString("You have no coins left.", 235,250);
		}

	}

	//shows JOptionPane for betting
	public void showBetWin()
	{
		String input = JOptionPane.showInputDialog("How much are you betting?");
		int b = 0;
		try
		{
     		b = Integer.parseInt(input);
     		if (b < 0) 
     		{
	     		b = 0;
	     		showMessage("You can't bet negative coins.");
     		}
		}
 		catch (NumberFormatException e)
 		{
     		b = 0;
     		showMessage("That's not a number.");
		}
		if (b > coins)
		{
			bet = coins;
		}
		else
		{
			bet = b;
		}
	}

	//pops up a window with a message
	public void showMessage(String msg)
	{
		JOptionPane.showMessageDialog(new Frame(), msg);
	}

	//Useless functions
	public void mousePressed( MouseEvent evt ) {}
	public void mouseReleased( MouseEvent evt ) {}
	public void mouseEntered( MouseEvent evt ) {}
	public void mouseExited( MouseEvent evt ) {}
}

