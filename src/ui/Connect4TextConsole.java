package ui;
/**Description of Connect4TextConsole class : This has the code for a text console based game of Connect 4. Makes calls to Connect4 for the code to run. 
 * This is the starting point for the overall program as the program gives the user the choice of a text based game or a gui based game. 
 *
 * @author Everett Olson
 * @version 3.0 3/28/19
 *
 */
import java.util.Scanner;

import core.Connect4;
//import core.Connect4ComputerPlayer;
import ui.Connect4GUI;


public class Connect4TextConsole
{
    public static void main(String[] args)
    {
    	Scanner scan = new Scanner(System.in);
    	System.out.println("Welcome to Connect4");
    	String input;
    	int PVP;
    	
    	//Asks the user if they want a text based console or want a GUI
    	System.out.println("Enter T for a text console or G for a GUI");
    	input = scan.nextLine();
    	
    	while(!input.equalsIgnoreCase("T") && !input.equalsIgnoreCase("G"))
    	{
    		System.out.println("Please enter a valid input");
    		input = scan.nextLine();
    	}
    	if(input.equalsIgnoreCase("G"))
    	{
    		Connect4GUI.main(args);
    		return;
    	}
    	
    	//Asks if the user wants to play against a CPU or another player
    	System.out.println("Enter'P' if you want to play against another player or 'C' to play against a computer.");
    	input = scan.nextLine();
    	
    	while(!input.equalsIgnoreCase("C") && !input.equalsIgnoreCase("P"))
    	{
    		System.out.println("Please enter a valid input");
    		input = scan.nextLine();
    	}
    	
    	if(input.equalsIgnoreCase("P"))
    	{
    		System.out.println("PvP selected\n");
    		PVP = 0;
    	}
    	else
    	{
    		System.out.println("PvC selected");
    		PVP = 1;
    	}
    	
    	//makes a new Text based game
    	Connect4 c4 = new Connect4(PVP);
    	System.out.print(c4.print());
    	boolean win = false;
    	int turn = 0;
    	
    	// keeps the game going if no one has won and the board still has spaces
    	while(!win && turn < 42)
    	{
    		win = c4.turn(turn);
    		System.out.print(c4.print());
    		if(turn %2 == 0)
            {
         	   System.out.println("X's turn");
            }
            else
            {
         	   System.out.println("O's Turn");
            }
    		turn++;
    	}
    	
    	// if board is full and not one has won
    	if(turn == 42 && !win)
    	{
    		System.out.println("Tie Game. Game board is full.");
    	}
    	
    	//if 
    	else if(turn % 2 == 0 && PVP == 0)
    	{
            System.out.println("Player X won the game");
    	}
        else if(turn % 2 == 0 && PVP == 1)
        {
            System.out.println("The Computer won the game");
        }
        else if(turn % 2 == 1 && PVP == 0)
        {
            System.out.println("Player O won the game");
        }
        else if(turn % 2 == 1 && PVP == 1)
        {
            System.out.println("You won the game");
        }
    }
}
