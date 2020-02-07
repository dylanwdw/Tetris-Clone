import java.awt.*;
import javax.swing.JFrame;

public class Display
{
	private JFrame frame; //window for the graphics
	private int width; //with of the display in pixels
	private int height; //height of the display in pixels
	
	public Display(int width, int height)
	{
		this.width = width;
		this.height = height;
		frame = new JFrame();
		frame.setSize(this.width+250, this.height);
		frame.setPreferredSize(new Dimension(this.width+250, this.height));
		frame.setLocationRelativeTo(null); //window will center itself on the screen
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //program will terminate when window is closed
		frame.setResizable(false); //window can not be stretched
		frame.setUndecorated(false); 
		frame.getContentPane().setLayout(null); //no special layout is needed
		frame.setBackground(Color.DARK_GRAY); //background color for the window. 
		frame.pack();
		frame.setVisible(true); //makes the window visible
	}
	
	public JFrame getFrame()
	{
		return this.frame;
	}
	
	//returns width of the window
	public int getWidth()
	{
		return this.width;
	}
	
	//returns height of the window
	public int getHeight()
	{
		return this.height;
	}
	
}
	