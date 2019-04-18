package core;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.*;
import java.net.*;

public class Connect4Client extends JApplet
    implements Runnable 
    {
	  public static int PLAYER1 = 1; // Indicate player 1
	  public static int PLAYER2 = 2; // Indicate player 2
	  public static int PLAYER1_WON = 1; // Indicate player 1 won
	  public static int PLAYER2_WON = 2; // Indicate player 2 won
	  public static int DRAW = 3; // Indicate a draw
	  public static int CONTINUE = 4; // Indicate to continue
  // Indicate whether the player has the turn
  private boolean myTurn = false;

  // Indicate the token for the player
  private char p1Token = ' ';

  // Indicate the token for the other player
  private char p2Token = ' ';

  // Create and initialize cells
  private Cell[][] board =  new Cell[6][7];

  // Create and initialize a title label
  private JLabel jlblTitle = new JLabel();

  // Create and initialize a status label
  private JLabel jlblStatus = new JLabel();

  // Indicate selected row and column by the current move
  private int rowSel;
  private int columnSel;

  // Input and output streams from/to server
  private DataInputStream fromTheSer;
  private DataOutputStream toTheSer;

  // Keep playing?
  private boolean keepPlaying = true;

  // Wait for the player to mark a cell
  private boolean waiting = true;

  // Indicate if it runs as application
  private boolean isStandAlone = false;

  // Host name or ip
  private String host = "localhost";

  /**
   * starts the program for the client
   */
  public void init() 
  {
    // Panel fringpan holds the board
    JPanel fryingpan = new JPanel();
    fryingpan.setBackground(Color.DARK_GRAY);
    fryingpan.setLayout(new GridLayout(6, 7, 0, 0));
    for (int i = 0; i < 6; i++)
      for (int j = 0; j < 7; j++)
        fryingpan.add(board[i][j] = new Cell(i, j));

    // Set properties for labels and borders for labels and panel
    fryingpan.setBorder(new LineBorder(Color.BLACK, 1));
    jlblTitle.setHorizontalAlignment(JLabel.CENTER);
    jlblTitle.setFont(new Font("Times New Roman", Font.BOLD, 16));
    jlblTitle.setBorder(new LineBorder(Color.BLACK, 1));
    jlblStatus.setBorder(new LineBorder(Color.BLACK, 1));

    // Place the panel and the labels to the applet
    add(jlblTitle, BorderLayout.NORTH);
    add(fryingpan, BorderLayout.CENTER);
    add(jlblStatus, BorderLayout.SOUTH);

    // Connect to the server
    connectToServer();
  }

  /**
   * Connects the client to the server
   */
  private void connectToServer() 
  {
    try 
    {
      // Create a socket to connect to the server
      Socket sock;
      if (isStandAlone)
        sock = new Socket(host, 8000);
      else
        sock = new Socket(getCodeBase().getHost(), 8000);

      // Create an input stream to receive data from the server
      fromTheSer = new DataInputStream(sock.getInputStream());

      // Create an output stream to send data to the server
      toTheSer = new DataOutputStream(sock.getOutputStream());
    }
    catch (Exception ex) 
    {
      System.err.println(ex);
    }

    // Control the game on a separate thread
    Thread thread = new Thread(this);
    thread.start();
  }

  /**
   * Runs the game for the client
   */
  public void run() 
  {
    try 
    {
      // Get notification from the server
      int p = fromTheSer.readInt();

      // Am I player 1 or 2?
      if (p == PLAYER1) 
      {
        p1Token = 'X';
        p2Token = 'O';
        jlblTitle.setText("Player 1 with red pieces");
        jlblStatus.setText("Waiting for player 2 to join");

        // Receive startup notification from the server
        fromTheSer.readInt(); // Whatever read is ignored

        // The other player has joined
        jlblStatus.setText("Player 2 has joined. You start first");

        // It is my turn
        myTurn = true;
      }
      else if (p == PLAYER2) 
      {
        p1Token = 'O';
        p2Token = 'X';
        jlblTitle.setText("Player 2 with yellow pieces");
        jlblStatus.setText("Waiting for player 1 to move");
      }

      // Continue to play
      while (keepPlaying) 
      {
        if (p == PLAYER1) 
        {
          waitForMove(); // Wait for player 1 to move
          sendMove(); // Send the move to the server
          receiveMoveFromServer(); // Receive info from the server
        }
        else if (p == PLAYER2) 
        {
          receiveMoveFromServer(); // Receive info from the server
          waitForMove(); // Wait for player 2 to move
          sendMove(); // Send player 2's move to the server
        }
      }
    }
    catch (Exception ex) 
    {
    }
  }

  /**
   * Waits for the player to place a piece
   * @throws InterruptedException
   */
  private void waitForMove() throws InterruptedException 
  {
    while (waiting) {
      Thread.sleep(100);
    }

    waiting = true;
  }

  /**
   * Sends the player's move to the server
   * @throws IOException
   */
  private void sendMove() throws IOException 
  {
	// Sends the selected row
	toTheSer.writeInt(rowSel); 
	// Sends the selected column
	toTheSer.writeInt(columnSel); 
	for(int i =0; i< 6; i++)
    {
  	  if(board[i][columnSel].getToken() == ' ')
  	  {
  		  rowSel = i;
  	  }
    }
    
    
  }

  /**
   * Receive the other players move from the server
   * @throws IOException
   */
  private void receiveMoveFromServer() throws IOException 
  {
    // Receive game status
    int status = fromTheSer.readInt();

    if (status == PLAYER1_WON) 
    {
      // Player 1 has won, end game
      keepPlaying = false;
      if (p1Token == 'X') 
      {
        jlblStatus.setText("You won! (Red)");
      }
      else if (p1Token == 'O') 
      {
        jlblStatus.setText("Player 1 (Red) has won!");
        receiveMove();
      }
    }
    else if (status == PLAYER2_WON) 
    {
      // Player 2 has won, end game
      keepPlaying = false;
      if (p1Token == 'O') 
      {
        jlblStatus.setText("You won! (Yellow)");
      }
      else if (p1Token == 'X') 
      {
        jlblStatus.setText("Player 2 (Yellow) has won!");
        receiveMove();
      }
    }
    else if (status == DRAW) 
    {
      // tie game, game is over
      keepPlaying = false;
      jlblStatus.setText("Game is over, Tie game");

      if (p1Token == 'O') 
      {
        receiveMove();
      }
    }
    else 
    {
      receiveMove();
      jlblStatus.setText("Your turn");
      myTurn = true; // It is the players turn
    }
  }

  /**
   * Sets the other players piece
   * @throws IOException
   */
  private void receiveMove() throws IOException 
  {
    // Get the other player's move
    int row = fromTheSer.readInt();
    int column = fromTheSer.readInt();
    board[row][column].setToken(p2Token);
  }

  // An inner class for a cell
  public class Cell extends JPanel 
  {
    // Indicate the row and column of this cell in the board
    private int row;
    private int column;

    // Token used for this cell
    private char token = ' ';

    public Cell(int row, int column) 
    {
      this.row = row;
      this.column = column;
      setBorder(new LineBorder(Color.blue, 1)); // Set cell's border
      addMouseListener(new ClickListener());  // Register listener
    }

    /**
     * returns the token being used
     * @return token
     */
    public char getToken() 
    {
      return token;
    }

    /**
     * Sets a new token 
     * @param c takes a char to set as the token
     */
    public void setToken(char c) 
    {
      token = c;
      repaint();
    }

    @Override /** Paints the cell */
    /**
     * This paints the cells to look the way they do
     */
    protected void paintComponent(Graphics g) 
    {
      super.paintComponent(g);

      if (token == 'X') {
        //g.drawLine(10, 10, getWidth() - 10, getHeight() - 10);
        //g.drawLine(getWidth() - 10, 10, 10, getHeight() - 10);
    	  g.setColor(Color.RED);
        g.fillOval(0, 0, getWidth()-1, getHeight()-1);
        
      }
      else if (token == 'O') {
        //g.drawOval(0,0, getWidth(), getHeight());
        g.setColor(Color.YELLOW);
        g.fillOval(0, 0, getWidth()-1, getHeight()-1);
      }
    }

    /** Handle mouse click on a cell */
    private class ClickListener extends MouseAdapter 
    {
      public void mouseClicked(MouseEvent e) 
      {
        // If cell is not occupied and the player has the turn
        if ((token == ' ') && myTurn) 
        {
            // Set the player's token in the cell
          myTurn = false;
          columnSel = column;
          for(int i =0; i< 6; i++)
          {
        	  if(board[i][columnSel].getToken() == ' ')
        	  {
        		  row = i;//sets value to the lowest spot
        	  }
          }
          rowSel = row; 
          board[row][column].setToken(p1Token);//sets it in the lowest spot on the player's screen
          jlblStatus.setText("Waiting for the other player to move");
          waiting = false; // Just completed a successful move
        }
      }
    }
  }

  /**
   * This main method enables the applet to run as an application
   * @param args basic parameter
   */
  public static void main(String[] args) 
  {
    // Creates a frame
    JFrame fr = new JFrame("Connect4 Client");

    // Creates an instance of the applet
    Connect4Client applet = new Connect4Client();
    applet.isStandAlone = true;

    // Gets the host
    if (args.length == 1) applet.host = args[0];

    // Adds the applet instance to the frame
    fr.getContentPane().add(applet, BorderLayout.CENTER);

    // Invokes init() and start()
    applet.init();
    applet.start();

    // Displays the frame
    fr.setSize(320, 300);
    fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    fr.setVisible(true);
  }
}