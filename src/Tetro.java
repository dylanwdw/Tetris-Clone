import java.util.Random;

public class Tetro 
{
	private int[][] shape; //defines the shape of the tetromino. The shape is made up of blocks in a 2D array. 0 = not part of the shape.
	private int rotateNum; //keeps track of which phase of rotation the tetromino is on
	private int x;
	private int y;
	private static Random random = new Random(); //creates a random number between 1 and 7
	private static int key = random.nextInt(7) + 1; 
	private static int key1 = random.nextInt(7) + 1;
	private static int key2 = random.nextInt(7) + 1;
	private static int key3 = random.nextInt(7) + 1;
	private int gravityTimer = 0; //keeps track of how long it's been since the tetromino moved downwards due to gravity.
	private int coolDownTimer = 0; //keeps track of how long it's been since the tetromino moved downwards due to arrow key press.
	private int coolLeftTimer = 0; //keeps track of how long it's been since the tetromino moved to the left.
	private int coolRightTimer = 0;
	private int rotateCoolDown = 0;

	//Creates a new tetromino with a specified shape, position, and orientation. 
	public Tetro(int theKey, int x, int y, int rotateNum)
	{
		this.x = x;
		this.gravityTimer = 0;
		this.rotateNum = rotateNum;
		this.create(theKey);
	}
	
	//creates a tetromino with a specified shape.
	public Tetro(int theKey)
	{
		this.rotateNum = 3;
		this.create(theKey);
	}
	
	//completely new tetromino.
	public Tetro()
	{
		this.rotateNum = 1;
		this.x = 5;
		this.y = 0;
		this.create(key);
	}
	
	//updates the three upcoming tetrominoes by deleting the first key and adding another one.
	public void shiftKeys()
	{
		key = key1;
		key1 = key2;
		key2 = key3;
		key3 = random.nextInt(7) + 1;
	}

	//defines the shape of the tetromino based on a specified key.
	private void create(int key)
	{
		if(key == 1)
		{
			if(rotateNum == 1)
				this.shape = new int[][] {
					{0, 0, 0, 0},
					{1, 1, 1, 1}};
			else if(rotateNum == 2)
				this.shape = new int[][] {
					{0, 0, 1, 0},
					{0, 0, 1, 0},
					{0, 0, 1, 0},
					{0, 0, 1, 0}};	
			else if(rotateNum == 3)
				this.shape = new int[][] {
					{0, 0, 0, 0},
					{0, 0, 0, 0},
					{1, 1, 1, 1}};
			else
				this.shape = new int[][] {
					{0, 1, 0, 0},
					{0, 1, 0, 0},
					{0, 1, 0, 0},
					{0, 1, 0, 0}};
		}
		else if(key == 2)
			this.shape = new int[][] {
				{0, 2, 2},
				{0, 2, 2}};
				
		else if(key == 3)
		{
			if(rotateNum == 1)
				this.shape = new int[][] {
					{0, 3, 0},
					{3, 3, 3}};
			else if(rotateNum == 2)
				this.shape = new int[][] {
					{0, 3, 0},
					{0, 3, 3},
					{0, 3, 0}};
			else if(rotateNum == 3)
				this.shape = new int[][] {
					{0, 0, 0},
					{3, 3, 3},
					{0, 3, 0}};
			else
				this.shape = new int[][] {
					{0, 3, 0},
					{3, 3, 0},
					{0, 3, 0}};
		}
		else if(key == 4)
		{
			if(rotateNum == 1)
				this.shape = new int[][] {
					{0, 4, 4},
					{4, 4, 0}};
			else if(rotateNum == 2)
				this.shape = new int[][] {
					{0, 4, 0},
					{0, 4, 4},
					{0, 0, 4}};
			else if(rotateNum == 3)
				this.shape = new int[][] {
					{0, 0, 0},
					{0, 4, 4},
					{4, 4, 0}};
			else
				this.shape = new int[][] {
					{4, 0, 0},
					{4, 4, 0},
					{0, 4, 0}};
		}
		else if(key == 5)
		{
			if(rotateNum == 1)
				this.shape = new int[][] {
					{5, 5, 0},
					{0, 5, 5}};
			else if(rotateNum == 2)
				this.shape = new int[][] {
					{0, 0, 5},
					{0, 5, 5},
					{0, 5, 0}};
			else if(rotateNum == 3)
				this.shape = new int[][] {
					{0, 0, 0},
					{5, 5, 0},
					{0, 5, 5}};
			else
				this.shape = new int[][] {
					{0, 5, 0},
					{5, 5, 0},
					{5, 0, 0}};
		}
		else if(key == 6)
		{
			if(rotateNum == 1)
				this.shape = new int[][] {
					{6, 0, 0},
					{6, 6, 6}};
			else if(rotateNum == 2)
				this.shape = new int[][] {
					{0, 6, 6},
					{0, 6, 0},
					{0, 6, 0}};
			else if(rotateNum == 3)
				this.shape = new int[][] {
					{0, 0, 0},
					{6, 6, 6},
					{0, 0, 6}};
			else
				this.shape = new int[][] {
					{0, 6, 0},
					{0, 6, 0},
					{6, 6, 0}};
		}
		else
		{
			if(rotateNum == 1)
				this.shape = new int[][] {
					{0, 0, 7},
					{7, 7, 7}};
			else if(rotateNum == 2)
				this.shape = new int[][] {
					{0, 7, 0},
					{0, 7, 0},
					{0, 7, 7}};
			else if(rotateNum == 3)
				this.shape = new int[][] {
					{0, 0, 0},
					{7, 7, 7},
					{7, 0, 0}};
			else
				this.shape = new int[][] {
					{7, 7, 0},
					{0, 7, 0},
					{0, 7, 0}};
		}
	}
	
	//rotates the tetromino, if it has been long enough since last rotation.
	public void rotate() // clockwise
	{
		if(rotateCoolDown == 30)
		{
			if(rotateNum >= 4)
				rotateNum = 1;
			else
				rotateNum++;
			create(key);
			rotateCoolDown = 0;
		}
		else
			rotateCoolDown++;
	}
	
	//returns the number of the tetromino's shape at a specified index.
	public int getAtIndex(int r, int c)
	{
		return this.shape[r][c];
	}
	
	//moves the tetromino downwards due to gravity, if it has been long enough.
	public void gravity()
	{
		gravityTimer++;
		if(gravityTimer == 200)
		{
			this.y++;
			gravityTimer = 0;
		}
	}
	
	//moves the tetromino downwards due to the down arrow key being pressed, if it has been long enough.
	public void moveDown()
	{
		if(coolDownTimer == 15)
		{
			this.y++;
			coolDownTimer = 0;
		}
		else
			coolDownTimer++;
	}
	
	//moves the teromino to the left, if it has been long enough.
	public void moveLeft()
	{
		if(coolLeftTimer == 20)
		{
			this.x--;
			coolLeftTimer = 0;
		}
		else
			coolLeftTimer++;
	}
	
	//moves the tetromino to the right, if it has been long enough
	public void moveRight()
	{
		if(coolRightTimer == 20)
		{
			this.x++;
			coolRightTimer = 0;
		}
		else
			coolRightTimer++;
	}
	
	//sets the orientation of the teromino
	public void setRotateNum(int n)
	{
		rotateNum = n;
	}
	
	//returns the orientation of the tetromino
	public int getRotateNum()
	{
		return rotateNum;
	}
	
	//returns the width of the shape
	public int getNumColumns()
	{
		return this.shape[0].length;
	}
	
	//returns the height of the shape
	public int getNumRows()
	{
		return this.shape.length;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	//returns the key of the tetromino, which identifies it's shape.
	public static int currentKey()
	{
		return key;
	}
	
	//returns the keys of the future tetrominoes.
	public static int getKeyN(int n)
	{
		if(n == 1)
			return key1;
		if(n == 2)
			return key2;
		if(n == 3)
			return key3;
		return -1;
	}
	
	//changes the x position of the tetromino
	public void setX(int newX)
	{
		this.x = newX;
	}
	
	//changes the y position of the tetromino. 
	public void setY(int newY)
	{
		this.y = newY;
	}

}

