import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener
{	
	private boolean down, left, right, up; //keeps track of which arrow keys are currently being pressed.
	
	public KeyManager()
	{
		down = false; //false = now pressed, true = being pressed.
		left = false;
		right = false;
		up = false;
	}

	@Override
	
	//called whenever a key is pressed.
	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode(); //the key code tells us which key was pressed.
		
		//if the key was an arrow key, then that key's boolean value is set to true.
		if(key == KeyEvent.VK_LEFT)
		{
			left = true;
		}
		else if(key == KeyEvent.VK_RIGHT)
		{
			right = true;
		}
		else if(key == KeyEvent.VK_DOWN)
		{
			down = true;
		}	
		else if(key == KeyEvent.VK_UP)
		{
			up = true;
		}
	}

	@Override
	//called when a key is released
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode(); //tells us which key was released
		
		//if it was an arrow key, then that keys boolean value is set to false.
		if(key == KeyEvent.VK_LEFT)
		{
			left = false;
		}
		else if(key == KeyEvent.VK_RIGHT)
		{
			right = false;
		}
		else if(key == KeyEvent.VK_DOWN)
		{
			down = false;
		}
		else if(key == KeyEvent.VK_UP)
		{
			up = false;
		}
	}
	
	//returns true if the left arrow key is being pressed.
	public boolean getLeft()
	{
		return left;
	}
	
	//returns true if the right arrow key is being pressed.
	public boolean getRight()
	{
		return right;
	}
	
	//returns true if the down arrow key is being pressed.
	public boolean getDown()
	{
		return down;
	}
	
	//returns true if the up arrow key is being pressed.
	public boolean getUp()
	{
		return up;
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
		//not needed for our program, required by interface.
	}
}
