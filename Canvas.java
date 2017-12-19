import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class Canvas extends JComponent {
	private static final long	serialVersionUID	= 1L;						// needed to prevent warning
	private BufferedImage		i3;												// Image, used for game over, restart, and quit screens
	private Dimension			d					= new Dimension(62, 200);	// stores origin coordinates of board
	private Dimension			size				= new Dimension(760, 930);	// size of the window
	static Tile[][]				board;											// 2D Array holding all the 'tiles'
	Font						f;												// Font imported to match the original game font. 
	private String				title;
	static int					freetiles			= 16;						// keep track of how many open spaces there are, so you don't 
	static int					ask					= 0;						// keeps track of user input when questioned. 0 is default, 1 is yes, 2 is no
	static int					qorr				= 0;						// (QuitORRestart) keeps track of which question is being asked, so user input doesn't trigger both
	static int					score				= 0;						// Score variable
	static int					hscore				= 0;						// High score variable
	static int					hvalue				= 2;						//highest tile on board, used to change title	

	public Canvas() {
		try {
			Font g = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("ClearSans-Bold.ttf")); // import font 
			f = g.deriveFont(80f);
		}
		catch (IOException | FontFormatException e) {
			System.out.println("Could not Find file");
		}

		board = new Tile[4][4]; //new board
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				board[i][j] = new Tile(i, j, d); // fill board with empty tiles, and having the coordinates set in the tile
			}
		}
		randomnewTile(); // Assign two randomly placed tiles
		randomnewTile();
		Timer timer = new Timer(150, new ActionListener() { // set the graphics to repaint every 150ms. I do this instead of the on click refresh, so the new tiles will flash and then go back to their normal color. 
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint(); // Repaint board
				if (ask != 0) { //if there is a question present, check to see if it's been answere'd 
					restart();
					quit();
				}

			}
		});
		//Keyboard input
		String[] commands = { "UP", "DOWN", "LEFT", "RIGHT", "R", "r", "w", "W", "S", "s", "a", "A", "d", "D", "Y", "y",
		        "N", "n", "Q", "q" }; //possible inputs
		for (int i = 0; i < commands.length; i++) //for each input ^^ 
			registerKeyboardAction(new ActionListener() { //register them as keystroke and listen for them
				@Override
				public void actionPerformed(ActionEvent ae) {
					String command = ae.getActionCommand(); //convert someaction into string
					boolean valid = false; // boolean to see if command is valid (eg can any pieces move on the board)
					if (command.equals("UP") || command.equals("W") || command.equals("w")) valid = move(3); // if someaction is up key or w, move up
					else if (command.equals("DOWN") || command.equals("S") || command.equals("s")) valid = move(1); //etc
					else if (command.equals("LEFT") || command.equals("a") || command.equals("A")) valid = move(2); // Note, the values passed through to move reflect the number of times you have to rotate the board so direction is left to right
					else if (command.equals("RIGHT") || command.equals("D") || command.equals("d")) valid = move(0);
					else if (command.equals("R") || command.equals("r")) {
						qorr = 1;//set question mode into restart mode
						restart(); // run restart method
					}
					else if (command.equals("Y") || command.equals("y")) confirm(); // confirm or deny which changes ask variable
					else if (command.equals("N") || command.equals("n")) deny(); // ^^
					else if (command.equals("Q") || command.equals("q")) { // set quit mode 
						qorr = 2;
						quit(); //run quit method
					}
					if (valid && freetiles > 0) randomnewTile(); //if the move is valid and there are open spaces generate a new tile
					else if (freetiles == 0) gameover(); //otherwise run the gameover method and make sure there are no more moves availiable
				}
			}, commands[i], KeyStroke.getKeyStroke(commands[i]), JComponent.WHEN_IN_FOCUSED_WINDOW); //set up listener to work when focused on 
		timer.start(); //start timer
	}

	protected void paintComponent(Graphics g) {
		// Draw everything
		Graphics2D g2 = (Graphics2D) g; //setup g2d so textantialiasing can be enabled
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //enable antialiasing
		g.setColor(new Color(250, 248, 239));//set background color
		g.fillRect(0, 0, getWidth(), getHeight());//fill background
		g.setColor(new Color(187, 173, 160));//set board background color
		g.fillRoundRect(d.width, d.height, 630, 630, 10, 10); //fill board background
		for (int i = 0; i < 4; i++) { //for every row in board 2d array
			for (int j = 0; j < 4; j++) { //for every collumn 
				if (board[i][j].getValue() > hvalue) hvalue = board[i][j].getValue();
				if (board[i][j].getValue() != 0) { //if the tile isn't empty
					g.setColor(board[i][j].getC()); //get color from the tile
					g.fillRoundRect(board[i][j].getxAb(), board[i][j].getyAb(), 137, 137, 10, 10); //get the coordinates from the tile, and paint there
					g.setColor(board[i][j].getCt()); //get the color of the text (stored in the tile)
					tileValueTextCenter(g, String.valueOf(board[i][j].getValue()), new Rectangle(board[i][j].getxAb(),
					        board[i][j].getyAb() - 1, 137, 137), f, board[i][j]); // Draw the value in the tile
				}
				else { //if tile is empty
					g2.setColor(new Color(205, 193, 180)); //set tile background color (when empty)
					g2.fillRoundRect(d.width + (int) (16.8 * (i + 1)) + (int) (137.2 * i), d.height + (int) (16.8 * (j
					        + 1)) + (int) (137.2 * j), 137, 137, 10, 10); //draw all tile backgrounds for emptytiles
				}
			}
		}
		g.drawImage(i3, 0, 0, null);//Draw the gameover/restart/quit screen
		g.setColor(new Color(119, 110, 101)); //color for 2048 text
		Font f2 = f.deriveFont(130f); //size for 2048 text
		g.setFont(f2);
		title = "2048";
		if (hvalue >= 2048) title = String.valueOf(hvalue * 2); //if a user gets to 2048, make the title change to represent a new goal
		g.drawString(title, 60, 150); //draw 2048 at top
		g.setColor(new Color(187, 173, 160)); //color for box behind scores
		g.fillRoundRect(432, 62, 122, 65, 8, 8); //draw box for score
		g.fillRoundRect(432 + 122 + 15, 62, 122, 65, 8, 8); //draw box for high score
		g.setColor(Color.white); //color of score
		if (score > 1000) { //if the socre gets too big, make the text smaller
			f2 = f.deriveFont(28f);
			g.setFont(f2);
		}
		else {
			f2 = f.deriveFont(35f); //if it's not big, have the text be bigger
			g.setFont(f2);
		}
		drawCenteredString(g, String.valueOf(score), new Rectangle(432, 90, 122, 30), f2); //draw the score
		if (hscore > 1000) { //same thing as above
			f2 = f.deriveFont(28f);
			g.setFont(f2);
		}
		else {
			f2 = f.deriveFont(35f);
			g.setFont(f2);
		}
		drawCenteredString(g, String.valueOf(hscore), new Rectangle(569, 90, 122, 30), f2); //draw high score
		f2 = f.deriveFont(18f); //size for score high score text
		g.setFont(f2);
		g.setColor(new Color(250, 248, 239));
		drawCenteredString(g, "SCORE", new Rectangle(432, 62, 122, 35), f2);//draw text high score and score
		drawCenteredString(g, "HIGH SCORE", new Rectangle(569, 62, 122, 35), f2);

	}

	public void drawCenteredString(Graphics g, String text, Rectangle d, Font font) {
		//simple method to draw a centered string within a bounding box (rectangle d) with a particular font
		FontMetrics m = g.getFontMetrics(font);
		int x = d.x + (d.width - m.stringWidth(text)) / 2; // origin of bounding box + 1/2 the width of the string when printed
		int y = d.y + ((d.height - m.getHeight()) / 2) + m.getAscent(); // origin of boundinb box +1/2 the hieght of the text
		g.setFont(font);
		g.drawString(text, x, y);
	}

	public void tileValueTextCenter(Graphics g, String text, Rectangle d, Font font, Tile b) {
		//modified version of above method which changes the font size based on the tile value
		if (b.getValue() > 9) {
			if (b.getValue() > 99) {
				if (b.getValue() > 999) {
					if (b.getValue() > 9999) {
						font = font.deriveFont(40f);
					}
					else font = font.deriveFont(50f);
				}
				else font = font.deriveFont(60f);
			}
			else font = font.deriveFont(70f);
		}
		FontMetrics m = g.getFontMetrics(font);
		int x = d.x + (d.width - m.stringWidth(text)) / 2;
		int y = d.y + ((d.height - m.getHeight()) / 2) + m.getAscent();
		g.setFont(font);
		g.drawString(text, x, y);
	}

	protected void gameover() {
		// Run when you think it's game over
		boolean valid = false; //is there still a valid move on the board?
		Tile[][] a;
		for (int k = 0; k < 4; k++) { //rotate all four directions
			a = board; //reset a to board
			a = rotate(k, a); //rotate a k times
			for (int i = 3; i >= 0; i--) {
				for (int j = 3; j >= 0; j--) {
					if (a[i][j].getValue() != 0) { //check every tile, if its 0 or adjacent to same valued tile, then there is a valid move
						a[i][j].setRecentcombo(false); //needed to make sure there are no adjacent same valued tiles (nexttile will not trigger if the recent combo flag is active)
						if (nextTile(a, i, j).width != 4) { // 4 is a value for (no adjacent tile of same value)
							valid = true;
						}
					}
					else valid = true;
				}
			}
		}
		if (!valid && qorr == 0) { //if there are no valid moves and there isn't already a question being shown
			message("      Game Over      ", "Press R to restart", 7); //tell the user the game is over and ask the user to restart
		}
	}

	public static boolean move(int direction) {
		// The brains of the whole game. Takes the board array, rotates it so the direction the user inputted is from left to right, and runs an algorithm to combine according to the rules
		boolean valid = false; // is the move a valid move?
		Tile[][] a = board; // temp copy of board
		a = rotate(direction, a); // rotate so user inputted direction is left to right
		for (int i = 3; i >= 0; i--) {
			for (int j = 3; j >= 0; j--) {
				//for every tile on the board
				if (a[i][j].getValue() != 0) { //if the tile is not empty
					if (nextTile(a, i, j).width == 4) continue; //if it cannot be moved or combined with something, go to next tile
					else {
						valid = true; //if it can be moved or combined, the user input is valid
						Dimension d = nextTile(a, i, j); // next tile returns the index of the tile to combine with
						if (a[d.width][d.height].getValue() != 0) { //if the one you are combining with/moving to isn't 0
							a[d.width][d.height].setRecentcombo(true); //then don't let it autocombine with another value (this prevents an entire row of 2's combining into a single 8)
							freetiles += 1; //if it's combining(Not just moving to a new space) then you will have one more free space on the board
							score += a[i][j].getValue() + a[d.width][d.height].getValue(); // add sum of combination to board
							if (score > hscore) hscore = score; //high score always reflects the highest possible score
						}
						a[d.width][d.height].setValue(a[i][j].getValue() + a[d.width][d.height].getValue()); // combine the two tiles. if the tile returned is blank, it will add the value plus 0, effectively moving the original tile
						a[i][j].setValue(0);// make the original tile empty
						if (i != 0) { //if original tile not all the way  on the left
							int l = d.width - i;
							for (int k = i + l - 1; k > l - 1; k--) {
								a[k][j].setValue(a[k - l][j].getValue());//move all tiles the number of tiles that the first one moved
								// EG:
								// if our row is   2 4 * 4 and  the fours combine so its 2 * * 8, move the two over two spaces (index 3-1 = 2 which is l value)
							}
							a[0][j].setValue(0); // if everything moves over, first tile has to be 0
							if (i < 3) i += l; // continue for the same tile again (in case a different one slid to it's position in the above process)
						}
					}
				}
			}
		}
		board = rotate(4 - direction, a); // unrotate board
		return valid; //if it's a valid move ^^ if something happened ^^ then return valid so the program knows to add a new tile
	}

	protected static Dimension nextTile(Tile[][] a, int x, int y) {
		// Method that checks the row (starting the original tile and moving backwards) for a same valued tile, or the end of the row, or a different valued tile
		// It will either return the coordinates of the next same valued tile, the last blank tile before a different valued tile, or 4,4, which means it can't move the tile at all
		if (x != 3) { //if it's not the last value in the row (because you can't move it anymore)
			for (int i = x + 1; i <= 3; i++) { // for every value to the right of the tile you are checking
				if (a[i][y].getValue() == 0) { //if it's a blank space
					if (i == 3) return new Dimension(3, y); //if the blank space is the one on the far right, return that tile (slide the tile all the way to the right)
					else continue; // if its blank, check the next tile
				}
				else if (a[i][y].getValue() == a[x][y].getValue()) { // if the tile is the same value, return the coordinates so they can combine
					if (a[i][y].getRecentcombo()) { //first check though to make sure the tile you are trying to combine with isn't a result of a different combination from the same turn (this prevents rows like 2 2 2 2 from combining to * * * 8)
						if (i - 1 != x && a[i - 1][y].getValue() == 0) return new Dimension(i - 1, y); // if the tile you are trying to combine with was recently made in this term, and there is white space between the two, put the tile right next to the value
						else break; // Returns (4,4), which tells the calling function to not move the tile
					}
					else return new Dimension(i, y); //combine two similar pieces
				}
				else if (a[i][y].getValue() != a[x][y].getValue()) { //if there is a non like tile, don't combine, move piece to place before non like tile
					if (i - 1 != x && a[i - 1][y].getValue() == 0) return new Dimension(i - 1, y);
					else break;
				}
			}
		}
		return new Dimension(4, 4);
	}

	public static Tile[][] rotate(int num, Tile[][] a) {
		// rotates array a num times
		Tile[][] b = new Tile[a[0].length][a.length];
		if (num % 4 != 0) { //if it is a factor of four, doesn't need to be rotated (efficiency line)
			for (int k = 0; k < num; k++) { //run num times
				b = new Tile[a[0].length][a.length]; //temp array to store rotated value
				for (int i = 0; i < a.length; i++) {
					for (int j = 0; j < a[i].length; j++) {
						b[j][i] = a[a.length - 1 - i][j]; //swap all the coordinates
					}
				}
				a = b;
			}
		}
		b = a;
		return b;
	}

	protected void randomnewTile() {
		Tile t1;
		do {
			t1 = board[(int) (Math.random() * 4)][(int) (Math.random() * 4)]; // random location
		}
		while (t1.getValue() != 0); // until the space is empty
		if ((int) Math.ceil(Math.random() * 10) > 8) t1.setValue(4); //.8 chance of 2, .2 chance of 4
		else t1.setValue(2);
		t1.setC(Color.WHITE); //make tile flash white
		t1.setCt(Color.white);
		freetiles -= 1;

	}

	private void message(String message, String message2, int ts) {
		//function is used to print a two line message on the screen. Used for game over, reset,  and quit messages
		i3 = new BufferedImage(760, 900, BufferedImage.TYPE_INT_ARGB); // size of window, with alpha channel (transparency)
		Graphics2D g3 = i3.createGraphics(); // setup graphics so we can draw to image
		g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //make text smooth
		g3.setColor(new Color(250, 248, 239, 180)); // white background, with alpha at about half
		g3.fillRect(0, 0, 760, 900); // paint the background
		g3.setColor(new Color(119, 110, 101));
		Font f2 = f.deriveFont(10f * ts); //ts referrs to the relative size of text. 
		g3.setFont(f2);
		FontMetrics fm = g3.getFontMetrics(); //used to get width of string for particular font
		int x = (size.width - fm.stringWidth(message)) / 2; //center horizontally
		int y = (fm.getAscent() + (size.height - (fm.getAscent() + fm.getDescent())) / 2 - 40); //center vertically (-40 to account for java error with title bar)
		g3.drawString(message, x, y);
		f2 = f.deriveFont(8f * ts); //set second line of text to be smaller
		g3.setFont(f2);
		g3.drawString(message2, x + 50, y + 60); //paint the text below. For all the messages I put, the manual +50 and +60 makes it centered
	}

	protected void confirm() {
		// run on user inputting y only changes ask variable if the message screen (i3) isn't up. this prevents accidental presses of y an n from messing up future attempts to quit or reset
		if (i3 != null) ask = 1;
	}

	protected void deny() {
		//same for no
		if (i3 != null) ask = 2;
	}

	protected void restart() {
		if (qorr == 1) { //makes sure we are on the restart screen. 
			message("Are you sure you want to restart?", "Press Y to restart, N to continue", 4);
			if (ask == 1) { //if the user says yes, repopulate the board with empty tiles, reset the score, and add two new tiles
				board = new Tile[4][4];
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						board[i][j] = new Tile(i, j, d);
					}
				}
				score = 0;
				freetiles = 16;
				randomnewTile();
				randomnewTile();
				i3 = null;
				ask = 0;
				qorr = 0; //reset state of all key variables
			}
			else if (ask == 2) { //if user says no, remove question screen
				i3 = null;
				ask = 0;
				qorr = 0;
			}
		}
	}

	protected void quit() {
		//same structure as restart method above, but for quit
		if (qorr == 2) {
			message("Are you sure you want to quit?", "Press Y to quit, N to continue", 4);
			if (ask == 1) {
				System.exit(0);//quit program immediately 
			}
			else if (ask == 2) {
				i3 = null;
				ask = 0;
				qorr = 0;
			}
		}
	}

}
