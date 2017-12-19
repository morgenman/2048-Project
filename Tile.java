import java.awt.Color;
import java.awt.Dimension;

public class Tile {
	private Color		c			= Color.WHITE, ct = Color.WHITE;	//default color is white
	private int			value		= 0;								//default value is empty (0)
	private Dimension	d1;												//stores absolute coordinates in jframe 
	private int			x, y;											//stores index in 2darray
	private Boolean		custom		= false;							// used to store color change of tile for one iteration. 
	private Boolean		recentcombo	= false;							//used to store information within the same turn reguarding if a tile is the product of two other tiles

	public Tile(int x, int y, Dimension d) {
		// Initialize new tiles. Dimension d referrs to the absolute coordinate of the board origin (top left corner of board)
		d1 = new Dimension(d.width + (int) (12 * 1.4 * (x + 1)) + (int) (98 * 1.4 * x), d.height + (int) (12 * 1.4 * (y
		        + 1)) + (int) (98 * 1.4 * y)); //d1 is the absolute coordinate of the tile 
		this.x = x; //index
		this.y = y;
	}

	public void update() {
		//this changes the colors based on the value
		ct = Color.white; //default text color is white
		recentcombo = false; // after one turn, each tile should have this reset to 0
		switch (value) {
		case 0:
			c = new Color(205, 193, 180);
			break;
		case 2:
			if (!custom) c = new Color(238, 228, 218);
			else custom = false;
			ct = new Color(119, 110, 101);
			break;
		case 4:
			if (!custom) c = new Color(237, 224, 200);
			else custom = false;
			ct = new Color(119, 110, 101);
			break;
		case 8:
			c = new Color(242, 177, 121);
			ct = Color.white;
			break;
		case 16:
			c = new Color(245, 149, 99);
			break;
		case 32:
			c = new Color(246, 124, 95);
			break;
		case 64:
			c = new Color(246, 94, 59);
			break;
		case 128:
			c = new Color(237, 207, 114);
			break;
		case 256:
			c = new Color(236, 203, 97);
			break;
		case 512:
			c = new Color(236, 199, 80);
			break;
		case 1024:
			c = new Color(236, 196, 64);
			break;
		case 2048:
			c = new Color(236, 193, 46);
			break;
		case 4096:
			c = new Color(255, 61, 62);
			break;
		default:
			c = new Color(255, 30, 31); //all tiles beyond 4096 in the real game are the same color
		}
	}

	// Getters and setters

	public int getxAb() {
		return d1.width;
	}

	public int getyAb() {
		return d1.height;
	}

	public void setCt(Color ct) {
		this.ct = ct;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setC(Color c) {
		this.c = c;
		this.custom = true;
	}

	public Color getC() {
		update();
		return c;
	}

	public Color getCt() {
		update();
		return ct;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Boolean getRecentcombo() {
		return recentcombo;
	}

	public void setRecentcombo(Boolean recentcombo) {
		this.recentcombo = recentcombo;
	}
}
