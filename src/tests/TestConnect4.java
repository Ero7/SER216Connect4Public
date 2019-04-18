package tests;
import core.Connect4;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;


class TestConnect4 
{
	Connect4 c4pvp = new Connect4(0);
	Connect4 c4pvc = new Connect4(1);
	char[][] testBoard = new char[7][8];
	String rBoard;
	@BeforeEach
	void test() 
	{ 
		   for(int i = 0; i < 6; i++) 
		   {
	           for(int j = 0; j < 7; j++) 
	           {
	               testBoard[i][j] = ' ';
	           }
	       }
		
		 rBoard = "";
	       for(int i = 6; i > 0; i--)
	       {
	           rBoard += "|";
	           for (int j = 0; j < 7; j++)
	           {
	               rBoard += testBoard[i-1][j] + "|";
	           }
	           rBoard += "\n";
	       }
	       
	   rBoard += " 1 2 3 4 5 6 7\n";
	}
	@Test
	 void testConnect4()
	   {
		assertEquals(rBoard, c4pvp.print());
	   }
	@Test
	   void testValidMove()
	   {
		//assertEquals();
	   }
	@Test
	   void testCheckWin()
	   {
		for(int i = 0; i < 42; i++)
		{
			assertEquals(c4pvp.turn(i),c4pvp.turn(i));
			assertEquals(false,c4pvp.turn(i));
			assertEquals(true,c4pvp.turn(i));
			
			
			assertEquals(false, c4pvp.turn(1));
			assertEquals(true, c4pvp.turn(1));
		}
		   
	   }
	@Test
	   void testPrint()
	   {
	       assertEquals(rBoard, c4pvp.print());
	   }
	@Test
	   void testTurn()
	   {
		for(int i = 0; i < 42; i++)
		{
			assertEquals(c4pvp.turn(i),c4pvp.turn(i));
			assertEquals(false,c4pvp.turn(i));
			assertEquals(true,c4pvp.turn(i));
		}
	   }
	@Test
	   void testTurnGUI()
	   {
		boolean red = false;
		fail("Not yet implemented");
	   }

}
