import java.awt.*;
import java.awt.image.BufferStrategy;
/*
 * This class is responsible for all of the graphics.
 * Contains a thread that repeatedly updates the graphics
 */
public class Game implements Runnable
{
	private static Display display; //the container in which the graphics are drawn
	private Thread thread;
	private boolean isRunning = false; //keeps track of the state of the thread
	
	private BufferStrategy bs;
	private Graphics g; //graphics object used
	
	private GameBoard gameBoard;
	private Color[] colors = new Color[] {Color.CYAN, Color.YELLOW, Color.MAGENTA, Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE}; //all the colors for the tetrominoes
	private Font font = new Font("Dialog", Font.PLAIN, 19); //some fonts I like
	private Font font2 = new Font("Dialog", Font.PLAIN, 25);
	
	//All of these  values can be changed, but they must be divisible by 10.
	public static int fps = 300; //can be changed if needed
	public static int width = 450;
	public static int height = width*2; 
	public static final int blockWidth = width/10; //the size of each block

	public void init()
	{
		display = new Display(width, height);
		gameBoard = new GameBoard(); 
		display.getFrame().addKeyListener(gameBoard.getKeyManager()); //allows the display to detect key presses
	}
	
	//updates the games graphics, repeatedly called by thread
	public void render()
	{
		bs = display.getFrame().getBufferStrategy();
		if(bs == null)
		{
			display.getFrame().createBufferStrategy(3); //creates a buffer strategy if it didn't already have one
			return;
		}
		
		g = bs.getDrawGraphics();
		g.clearRect(0, 0, width+250, height); //clears the graphics so they can be redrawn 
		g.setColor(Color.BLACK); //background color - black adds the most contrast
		g.fillRect(0, 0, width, height); //this is the background of the game
		
		for(int i = 2; i <= 21; i++)
		{
			for(int j = 2; j <= 11; j++)
			{
				if(gameBoard.getGameBoardAtIndex(i, j) != 0) //draws falling tetromino
				{
					int n = gameBoard.getGameBoardAtIndex(i, j); //Each tetromino is represented by a different number, so the program knows which color to use.
					g.setColor(colors[n-1]);
					g.fillRect(((j-2) *blockWidth) +1, ((i-2) *blockWidth) +1, blockWidth-2, blockWidth-2);
				}	
				if(gameBoard.getPermBoardAtIndex(i, j) != 0) //draws tetrominoes that have accumulated on the bottom.
				{
					int n = gameBoard.getPermBoardAtIndex(i, j);
					g.setColor(colors[n-1]);
					g.fillRect(((j-2) *blockWidth) +1, ((i-2) *blockWidth) +1, blockWidth-2, blockWidth-2);
				}
			}
		}
		for(int k = 1; k <= 3; k++) //This loop draws the "Next" three tetrominoes. 
		{
			int n = Tetro.getKeyN(k);
			g.setColor(colors[n-1]);
			for(int i = 0;  i < gameBoard.getTetroN(k).getNumRows(); i++)
			{
				for(int j = 0; j < gameBoard.getTetroN(k).getNumColumns(); j++)
				{
					if(gameBoard.getTetroN(k).getAtIndex(i, j) != 0)
					{
						if(Tetro.getKeyN(k) == 2)
							g.fillRect(485+(j*blockWidth), (200+(200*k))-(i*blockWidth), blockWidth-1, blockWidth-1);
						else if(Tetro.getKeyN(k) == 1)
							g.fillRect(485+(j*blockWidth), (250+(200*k))-(i*blockWidth), blockWidth-1, blockWidth-1);
						else
							g.fillRect(507+(j*blockWidth), (250+(200*k))-(i*blockWidth), blockWidth-1, blockWidth-1);
					}
				}
			}
		}
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("CURRENT SCORE: " + gameBoard.getScore(), 457, 65); //displays the current score on the top right
		g.setFont(font2);
		g.drawString("NEXT:", 535, 290); //displays "Next" above the future pieces
		bs.show(); //makes the graphics visible
		g.dispose();
	}

	@Override
	public void run() 
	{
		double timePerTick = 1000000000/fps; //how frequently this method calls other methods
		double delta = 0;
		long now;
		long lastTime = System.nanoTime(); //stores the current time
		init();
		while(isRunning)
		{
			now = System.nanoTime(); //stores the current time
			delta += (now - lastTime)/timePerTick;
			lastTime = now;
			if(delta >= 1)
			{
				render();
				gameBoard.tick();
				delta=0; 
			}
		}
		stop();	
	}
	
	public synchronized void start() //Starts the thread
	{
		if(isRunning)
		{
			return;
		}
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() //Ends the thread
	{
		if(!isRunning)
		{
			return;
		}
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Display display()
	{
		return display;
	}
}