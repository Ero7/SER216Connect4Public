/**
 * @author Everett Olson
 * @version 1.0
 * 
 */
package core;
import java.util.Random;


public class Connect4ComputerPlayer 
{
	private final int HEIGHT = 6;
	private final int WIDTH = 7;
	//private final char EMPTY = ' ';
	int lastPlaced; //last placed computer piece
	int playerLP; //player last placed piece
	Random rand = new Random();
	//Connect4 cBoard;
	
	/** Constructor for Connect4ComputerPlayer 
	 * @param board is the c4 game board
	 * */
	/*public Connect4ComputerPlayer(Connect4 board)
	{
		cBoard = board;
	}*/
	
	/**
	 * Method for placing a random piece
	 * @param board the game board
	 * @return a random number
	 */
	public int placePiece(char[][] board)
	{
		int place = rand.nextInt(7);
		/** @return a random number*/
		return place;
		
	}
	
	/**
	 * Method for placing a piece
	 * @param board the game board
	 * @param piece which piece may win
	 * @return either a random number or a column
	 */
	public int winSoon(char[][] board, char piece)
	{
		int place = rand.nextInt(7);
	       //what player might have won
	       char pWin = piece;
	       /**Vertical check*/
	       for (int j = 0; j < HEIGHT - 3; j++)
	       {
	           //count = 0;
	           for (int i = 0; i+3 < WIDTH; i++)
	           {

	        	   if(board[j][i] == pWin && board[j+1][i] == pWin && board[j+2][i] == pWin)
	        	   {
	        		   if(i+3 >=board.length-1)
	        		   {
	        			   return place;
	        		   }
	        		   return j;
	        	   }
	               
	           }
	       }

	       /** horizontal Check*/
	       for (int i = 0; i < WIDTH; i++)
	       {
	           //count = 0;
	           for (int j = 0; j+3 < HEIGHT; j++)
	           {
	        	   if(board[j][i] == pWin && board[j+1][i] == pWin && board[j+2][i] == pWin)
	        	   {
	        		   if(j+3 >= board[0].length)
	        		   {
	        			   return j-1;
	        		   }
	        		   return j+3;
	        	   }
	               
	           }
	       }
	       /** ascendingDiagonalCheck*/
	       for (int i = 0; i < WIDTH - 3; i++)
	       {
	           for (int j = 3; j < HEIGHT; j++)
	           {
	               if (board[j][i] == pWin && board[j - 1][i + 1] == pWin && board[j - 2][i + 2] == pWin )
	                   return j+3;
	           }
	       }
	       for (int i = 0; i < board[0].length-3; i++)
	       {
	           for (int j = 0; j < board.length-3; j++) // COLUMNS -> COLUMNS -3
	           {
	               if (board[j][i] == pWin && board[j + 1][i + 1] == pWin && board[j + 2][i + 2] == pWin )
	                   return j+3;
	           }
	       }
		return place;
	   }
}
