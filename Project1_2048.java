import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Project1_2048 {
	private static Canvas main = new Canvas(); // New Canvas

	public static void main(String[] args) {
		JFrame frame = new JFrame("2048"); // Game Window
		frame.setPreferredSize(new Dimension(760, 930)); // size of the window
		frame.getContentPane().add(main, BorderLayout.CENTER); // Add canvas to Jframe
		frame.pack(); // shrink size to contents as needed
		frame.setResizable(false); // prevent user resizing
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // close on close (duh)
		frame.setVisible(true); // make window visible
		try {
			frame.setIconImage(ImageIO.read(Project1_2048.class.getResource("icon.png"))); // set 2048 icon (asthetics)
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
