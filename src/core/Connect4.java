/**Description of Connect4: Has the major code for all the connect4 modes. This is essentially the hub for all the other programs
 * 
 * @author Everett Olson
 * @version 1.0
 */
package core;

import java.util.Scanner;
import core.Connect4ComputerPlayer;

/**Description of Connect4 class
*
* @author Everett Olson
* @version 3.0 3/28/19
*
*/

public class Connect4
{
   final  int COLUMNS = 7;
   final  int ROWS = 6;
    char board[][] = new char [COLUMNS + 1][ROWS +1];
    int turnCounter = 0;
   int playerTurn = 0;
   private  Connect4ComputerPlayer cpu = new Connect4ComputerPlayer();
   private  int PVP; //for pvp = 0for pvc = 1
   private  Scanner scan = new Scanner(System.in);

   /**
    * This fills the array with spaces to make the gameboard look nice
    * @param type indicates if the game is PvP or PvCs
    */
   public Connect4(int type)
   {
	   PVP = type;
	   
	   for(int i = 0; i < ROWS; i++) 
	   {
           for(int j = 0; j < COLUMNS; j++) 
           {
               board[i][j] = ' ';
           }
       }
   }

   /**
    * This returns who's turn it is
    * @return who's turn it is
    */
   /*public int getPlayerTurn()
   {
       return playerTurn;
   }*/
   /**
    * This returns the number of turns
    * @return the number of turns
    */
  /* public int getTurnCounter()
   {
       return turnCounter;
   }*/

   /**Checks to see if there are not more open spots on the board*/
   /*public boolean checkTurn()
   {
       //Checks to see if the game resulted in a tie
       if(turnCounter > 41 && checkWin() == false)
       {
           return true; // Tie game
       }
       return false;
   }*/

   /**
    * Checks to see if a given move is valid
    * @param x is the inputted position that the player wants
    * @return true if move is valid and false if the move can't be made
    */
 /*  public boolean validMove(int x)
   {
       int row = x;
       if (row >= 0 && row < COLUMNS)
       {
           if (board[ROWS-1][row] != ' ')
           {
               System.out.println("Column is full");
               return false;
           }
           for (int i = 0; i <= ROWS; i++)
           {
               if (board[i][row] == ' ')
               {
                   if (playerTurn == 0)
                   {
                       board[i][row] = 'X';
                       turnCounter++;
                       playerTurn = turnCounter%2;
                       return true;
                   }
                   else
                   {
                       board[i][row] = 'O';
                       turnCounter++;
                       playerTurn = turnCounter%2;
                       return true;
                   }
               }
               else if(board[0][row] == ' ')
               {
                   if (playerTurn == 0)
                   {
                       board[i][row] = 'X';
                       turnCounter++;
                       playerTurn = turnCounter%2;
                       return true;
                   }
                   else
                   {
                       board[i][row] = 'O';
                       turnCounter++;
                       playerTurn = turnCounter%2;
                       return true;
                   }
               }
           }
       }
       return false;
   }*/

   /**
    * This checks if a player has won in any possible direction
    * @param turn used to calculate whose turn it is
    * @return if someone has won the game
    */
   public boolean checkWin(int turn)
   {
       //what player might have won
       char pWin;
       int count;
       if (turn % 2 == 0)
       {
           pWin = 'O';
       }
       else
       {
           pWin = 'X';
       }

       //horizontal check
       for (int j = 0; j < ROWS; j++)
       {
           count = 0;
           for (int i = 0; i < COLUMNS; i++)
           {

               if (board[j][i] == pWin)
               {
                   count++;
               }
               else
               {
                   count = 0;
               }
               if (count >= 4)
               {
                   return true;
               }
           }
       }

       // verticalCheck
       for (int i = 0; i < COLUMNS; i++)
       {
           count = 0;
           for (int j = 0; j < ROWS; j++)
           {

               if(board[j][i] == pWin)
               {
                   count++;
               }
               else
               {
                   count = 0;
               }
               if(count >=4)
               {
                   return true;
               }
           }
       }
       // ascendingDiagonalCheck
       for (int i = 0; i < COLUMNS - 3; i++)
       {
           for (int j = 3; j < ROWS; j++)
           {
               if (board[j][i] == pWin && board[j - 1][i + 1] == pWin && board[j - 2][i + 2] == pWin && board[j - 3][i + 3] == pWin)
                   return true;
           }
       }
       // descendingDiagonalCheck
       for (int i = 0; i < COLUMNS-3; i++)
       {
           for (int j = 0; j < ROWS-3; j++) // COLUMNS -> COLUMNS -3
           {
               if (board[j][i] == pWin && board[j + 1][i + 1] == pWin && board[j + 2][i + 2] == pWin && board[j + 3][i + 3] == pWin)
                   return true;
           }
       }
       return false;
   }
   /**
    * This fills prints out the gameboard with numbers underneath it
    * @return prints out the visual representation of the gameboard
    */
   public String print()
   {
       String rBoard = "";
       for(int i = ROWS; i > 0; i--)
       {
           rBoard += "|";
           for (int j = 0; j < COLUMNS; j++)
           {
               rBoard += board[i-1][j] + "|";
           }
           rBoard += "\n";
       }
       
       return rBoard + " 1 2 3 4 5 6 7\n";
   }
   
   /**
    * Places a piece in the game board
    * @param turn used to calculate whose turn it is
    * @return if someone has won the game (true) or not (false) or returns another turn
    */
   public boolean turn(int turn)
   {
       int row;

       // Takes in an input from the user
       if(PVP == 1 && turn % 2 == 0 || PVP == 0) {
           System.out.println("Please enter a number between 1 and 7");
           
           row = 0;
           try 
           {
               row = scan.nextInt() - 1;
           } 
           catch (Exception e) 
           {
               System.out.println("\nPlease enter an integer!\n");
               scan.nextLine();
               return turn(turn);
           }
       }
       // Takes an input from the computer
       else 
       {
    	   //waits 1 second before displaying CPU's move
    	   try 
    	   {
			Thread.sleep(1000);
    	   } 
    	   catch (InterruptedException e) 
    	   {
			// TODO Auto-generated catch block
			Thread.currentThread().interrupt();
		}
           row = cpu.placePiece(board);
       }

       // placing a piece
       if(row < COLUMNS && row >= 0) 
       {
           if (board[ROWS - 1][row] != ' ') 
           {
               System.out.println("\nThat column is full. Please select another column");
               return turn(turn);
           }

           for (int i = 0; i < ROWS; i++) 
           {
               if (board[i][row] == ' ') 
               {
                   if (turn % 2 == 0) 
                   {
                       board[i][row] = 'O';
                       break;
                   } else {
                       board[i][row] = 'X';
                       break;
                   }
               }
           }
       }

       else {
           System.out.println("\nInvalid column. Please select a valid column");
           return turn(turn);
       }

       return checkWin(turn);

	   
   }
   
  /**
   * The code to place a piece in the GUI
   * @param isR Indcates whose turn it is isR meaning Red and !isR meaning yellow
   * @param col column selected
   * @param row row selected
   * @return a piece is placed
   */
   public int turnGUI(boolean isR, int col, int row)
   {
	   //Code for the computer to place a piece
	   if(!isR && PVP == 1) {
   		int cpuTurn = cpu.placePiece(board);
   		
   		for(int i = 0; i < ROWS; i++) {
   			if(board[i][cpuTurn] == ' ') {
   	    		board[i][cpuTurn] = 'O';
   	    		return cpuTurn;
   			}
   		}
   	}
   		
   	//Code for player to place a piece
   	for(int i = 0; i < ROWS; i++) {
           if (board[i][col] == ' ') {
		    	if(isR) {
		    		board[i][col] = 'X';
		    		return 0;
		    	} else { 
		    		board[i][col] = 'O';
		    		return 0;
		    	}
           }
   	}
   	return -1;
   }
}
