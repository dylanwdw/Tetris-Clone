
public class GameBoard
{
	/* 
	 *  The gameboard is divided into 308 squares (22 by 14).
	 *  Each tetromino is composed of several squares. These are represented by numbers in the gameBoard array
	 *  Each tetromino has a unique number associated with it. for example, if a square has a 1 in it, then that square is part of an "I" tetromino.
	 *  A 0 represents no tetromino.
	 */
	private int[][] gameBoard; //Represents the currently falling tetromino
	private Tetro tetro; // the currently falling tetromino
	private int[][] permBoard; //represents the tetrominos that have already fallen and accumulated at the bottom.
	private KeyManager keyManager; //detects key presses
	private static int waitCount = 0;
	private static final int waitCountSet = 200; //determines the speed at which the tetrominos fall
	private int score;
	
	public GameBoard()
	{
		score = 0;
		gameBoard = new int[22][14];
		permBoard = new int [22][14];
		tetro = new Tetro();
		initGameBoard();
		initPermBoard();
		keyManager = new KeyManager();
	}
	
	//sets all values in the gameboard to 0.
	private void initGameBoard()
	{
		for(int i = 0; i <= 21; i++)
		{
			for(int j = 0; j <= 13; j++)
			{
				gameBoard[i][j] = 0;
			}
		}
	}
	
	//sets all values in the permboard to 0.
	private void initPermBoard() 
	{
		for(int i = 0; i <= 21; i++)
		{
			for(int j = 0; j <= 13; j++)
			{
				permBoard[i][j] = 0;
			}
		}
	}
	
	//starts the next tetromino falling sequence
	private void newTetro()
	{
		save();
		score += 10; 
		tetro.shiftKeys(); 
		tetro = new Tetro();
	}
	
	// moves the tetromino on the gameboard, only if necessary. Repeatedly called by thread.
	public void tick()
	{
		if(checkTop(tetro))
		{
			initPermBoard();
			score = 0;
		}
		this.addToGameBoard(tetro);
		
		if(checkBottom(tetro))
			tetro.gravity();
		else
		{
			if(waitCount == waitCountSet)
			{
				checkTop(tetro);
				newTetro();
				checkTetris();
				waitCount = 0;
			}
			else
				waitCount++;
		}
		
		if(keyManager.getDown() && checkBottom(tetro))
			tetro.moveDown();
		if(keyManager.getLeft() && checkLeft(tetro))
			tetro.moveLeft();
		if(keyManager.getRight() && checkRight(tetro))
			tetro.moveRight();
		if(keyManager.getUp() && canRotate())
			tetro.rotate();
	}
	
	//rotates the tetromino, if possible. Only can rotate clockwise.
	public void rotateTetro()
	{
		if(canRotate())
			tetro.rotate();
	}
	
	//Updates the gameboard so that the tetromino is represented at the right position.
	private void addToGameBoard(Tetro t)
	{
		this.initGameBoard();
		{
			for(int i = 0; i <= t.getNumRows() -1; i++)
			{
				for(int j = 0; j <= t.getNumColumns()-1; j++)
				{
					if(t.getAtIndex(i, j) != 0)
					{
						gameBoard[t.getY()+i][t.getX() + j] = t.getAtIndex(i, j);
					}
				}
			}
		}
	}
	
	//returns the number on the gameBoard at the specified index
	public int getGameBoardAtIndex(int r, int c)
	{
		return this.gameBoard[r][c];
	}
	
	//returns the number on the permBoard at the specified index
	public int getPermBoardAtIndex(int r, int c)
	{
		return this.permBoard[r][c];
	}
	
	//returns true if the tetromino has reached the top of the gameBoard.
	public boolean checkTop(Tetro t)
	{
		if(checkBottom(t) == false && t.getY() + t.getNumRows() <= 2)
			return true;
		return false;
	}
	
	//returns true if the tetromino has touched the left border of the gameBoard or is against another piece
	public boolean checkLeft(Tetro t)
	{
		int n = 500;
		for(int i = 0; i <= t.getNumRows()-1; i++)
		{
			for(int j = 0; j <= t.getNumColumns()-1; j++)
			{
				if(t.getAtIndex(i, j) != 0)
				{
					if(permBoard[t.getY()+i][t.getX()+j-1] != 0)
						return false;
					if(j < n)
						n = j;
					break;
				}
			}
		}
		if(t.getX()+n <= 2)
			return false;
		return true;
	}

	//returns true if the tetromino has touched the right side of the gameBoard or is against another piece
	public boolean checkRight(Tetro t)
	{
		int n = 0;
		for(int i = 0; i <= t.getNumRows()-1; i++)
		{
			for(int j = t.getNumColumns()-1; j >= 0; j--)
			{
				if(t.getAtIndex(i, j) != 0)
				{
					if(permBoard[t.getY()+i][t.getX()+j+1] != 0)
						return false;
					if(j > n)
						n=j;
					break;
				}
			}
		}
		if(t.getX()+n >= 11)
			return false;
		return true;
	}
	
	//returns true if the tetromino has hit the bottom of the gameBoard or landed on another piece
	public boolean checkBottom(Tetro t)
	{
		if(t.getY() + t.getNumRows() <= 21)
		{
			for(int j = 0; j < t.getNumColumns(); j++ )
			{
				for(int i = t.getNumRows()-1; i >= 0; i--)
				{
					if(t.getAtIndex(i, j) != 0 && permBoard[t.getY()+i+1][t.getX()+j] != 0)
						return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public void save() //adds the current tetromino on the gameBoard to the perm board.
	{
		for(int i = 0; i <= tetro.getNumRows() -1; i++)
		{
			for(int j = 0; j <= tetro.getNumColumns()-1; j++)
			{
				if(tetro.getAtIndex(i, j) != 0)
					permBoard[tetro.getY()+i][tetro.getX() + j] = tetro.getAtIndex(i, j);
			}
		}
	}
	
	//returns true if the tetromino can rotate. 
	public boolean canRotate()
	{
		if(Tetro.currentKey() == 2)
			return true;
		Tetro tetro2 = new Tetro(Tetro.currentKey(), tetro.getX(), tetro.getY(), tetro.getRotateNum()+1);
		if(checkBottom(tetro) == false && tetro2.getNumRows() > tetro.getNumRows())
			return false;
		for(int i = 0; i <= tetro2.getNumRows()-1; i++)
		{
			for(int j = 0; j <= tetro2.getNumColumns()-1; j++)
			{
				if((permBoard[tetro.getY()+i][tetro.getX()+j] != 0 && tetro2.getAtIndex(i, j) != 0) || checkBottom(tetro2) == false)
					return false;
			}
		}
		if(tetro2.getX() < 2)
		{
			if(Tetro.currentKey() == 1 && tetro2.getRotateNum() == 2)
			{
				tetro.setX(tetro.getX()+1);
				return true;
			}
			int n = getLeftN(tetro2);
			if((tetro2.getX()+n) == 1)
			{
				tetro.setX(tetro.getX()+1);
				tetro2.setX(tetro2.getX()+1);
			}
			else if((tetro2.getX()+n) == 0)
			{
				tetro.setX(tetro.getX()+2);
				tetro2.setX(tetro2.getX()+2);
			}		
		}
		else if(tetro2.getX()+tetro2.getNumColumns() > 11)
		{
			int n = getRightN(tetro2);
			if((tetro2.getX()+n) == 12)
			{
				tetro.setX(tetro.getX()-1);
				tetro2.setX(tetro2.getX()-1);
			}
			else if((tetro2.getX()+n) == 13)
			{
				tetro.setX(tetro.getX()-2);
				tetro2.setX(tetro2.getX()-2);
			}	
		}
		return true;
	}
	
	//returns the left-most position of the tetromino.
	private int getLeftN(Tetro t)
	{
		int n = 5;
		for(int j = 0; j <= t.getNumColumns()-1; j++)
		{
			for(int i = t.getNumRows()-1; i >= 0; i--)
			{
				if(t.getAtIndex(i, j) != 0)
				{
					n = j;
					break;
				}
			}
			if(n != 5)
				break;
		}
		return n;
	}
	
	//returns the right-most position of the tetromino
	private int getRightN(Tetro t)
	{
		int n = 5;
		for(int j = t.getNumColumns()-1; j >= 0; j--)
		{
			for(int i = t.getNumRows()-1; i >= 0; i--)
			{
				if(t.getAtIndex(i, j) != 0)
				{
					n = t.getNumColumns()-j;
					break;
				}
			}
			if(n != 5)
				break;
		}
		return n;
	}
	
	/*
	 * Checks to see if a tetris has occurred, which is when an entire row is filled up. 
	 * If more than one row is filled at the sane time, bonus points are awarded.
	 * If there was a tetris, the row(s) are then cleared and the accumulated tetrominoes above the rows move down.
	 */
	private void checkTetris()
	{
		int[] rows = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int count = 0;
		for(int i = permBoard.length-1; i >= 2; i--)
		{
			boolean row = true;
			for(int j = 2; j < permBoard[0].length-2; j++)
			{
				if(permBoard[i][j] == 0)
				{
					row = false;
					break;
				}
			}
			if(row)
			{
				count++;
				rows[i] = 1;
			}
		}
		if(count > 0)
		{
			for(int i = 2; i <= rows.length-1; i++)
			{
				if(rows[i] == 1)
				{
					for(int j = 0; j <= permBoard[0].length-1; j++)
					{
						permBoard[i][j] = 0;
					}
					shiftDown(i);
				}
			}
		}
		if(count == 1)
			score += 100;
		else if(count == 2)
			score += 200;
		else if(count == 3)
			score += 300;
		else if(count == 4)
			score += 500;	
	}
	
	//helper function for the checkTetris() function, responsible for shifting the board down after rows are cleared.
	private void shiftDown(int n)
	{
		for(int i = n; i > 1; i--)
		{
			for(int j = 2; j < permBoard[0].length-2; j++)
			{
				permBoard[i][j] = permBoard[i-1][j];
			}
		}
	}
	
	public KeyManager getKeyManager()
	{
		return keyManager;
	}
	
	public int getScore()
	{
		return score;
	}
	
	//returns the tetrominoes that are going to fall in the future.
	public Tetro getTetroN(int n)
	{
		Tetro t;
		if(n == 1)
			t = new Tetro(Tetro.getKeyN(1));
		else if(n == 2)
			t = new Tetro(Tetro.getKeyN(2));
		else
			t = new Tetro(Tetro.getKeyN(3));
		return t;
	}
}