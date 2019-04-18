package core;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
/**
 * Connect4Server is the Sever for the connect4 game
 * @author Everett Olson
 *
 */
public class Connect4Server extends Application
{
	public static int PLAYER1 = 1; // Indicate player 1
	  public static int PLAYER2 = 2; // Indicate player 2
	  public static int PLAYER1_WON = 1; // Indicate player 1 won
	  public static int PLAYER2_WON = 2; // Indicate player 2 won
	  public static int DRAW = 3; // Indicate a draw
	  public static int CONTINUE = 4; // Indicate to continue
    private int sessionNo = 1; // Number a session

    @Override // Override the start method in the Application class
    
    /**
     * Runs the server
     * @param primaryStage is what the server will look like
     */
    public void start(Stage primaryStage)
    {
        TextArea tLog = new TextArea();

        // Creates a scene and places it on the stage
        Scene scene = new Scene(new ScrollPane(tLog), 600, 230);
        // Set the title of the stage
        primaryStage.setTitle("Connect 4"); 
        // Places the scene on the stage
        primaryStage.setScene(scene); 
        // Displays the stage
        primaryStage.show(); 

        new Thread( () ->
        {
            try {
                // Creates a server socket
                ServerSocket servSock = new ServerSocket(8000);
                Platform.runLater(() -> tLog.appendText(new Date() +
                        ": Server started at socket 8000\n"));

                // The server is now ready to create a session for every two players
                while (true)
                {
                    Platform.runLater(() -> tLog.appendText(new Date() +
                            ": Wait for players to join session " + sessionNo + '\n'));

                    // Connects to player 1
                    Socket p1 = servSock.accept();

                    Platform.runLater(() -> {
                        tLog.appendText(new Date() + ": Player 1 joined session "
                                + sessionNo + '\n');
                        tLog.appendText("Player 1's IP address" +
                                p1.getInetAddress().getHostAddress() + '\n');
                    });

                    // Notifies that they are Player 1
                    new DataOutputStream(p1.getOutputStream()).writeInt(PLAYER1);

                    // Connects to player 2
                    Socket p2 = servSock.accept();

                    Platform.runLater(() -> {
                        tLog.appendText(new Date() +": Player 2 joined session " + sessionNo + '\n');
                        tLog.appendText("Player 2's IP address" + p2.getInetAddress().getHostAddress() + '\n');
                    });

                    // Notifies that they are Player 2
                    new DataOutputStream( p2.getOutputStream()).writeInt(PLAYER2);

                    // Displays this session and increment session number
                    Platform.runLater(() ->
                            tLog.appendText(new Date() +
                                    ": Start a thread for session " + sessionNo++ + '\n'));

                    // Launch a new thread for this session of two players
                    new Thread(new HandleASession(p1, p2)).start();
                }
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * Runs a session of the game of Connect4
     * @author Everett Olson
     *
     */
    class HandleASession implements Runnable
    {
        private Socket p1;
        private Socket p2;

        // Create game board
        private char[][] board =  new char[6][7];

        private DataInputStream fromPlayer1;
        private DataOutputStream toPlayer1;
        private DataInputStream fromPlayer2;
        private DataOutputStream toPlayer2;

        // Boolean to keep playing the game
        private boolean keepPlaying = true;

        /**
         * Creates a thread for the game
         * @param player1
         * @param player2
         */
        public HandleASession(Socket player1, Socket player2)
        {
            p1 = player1;
            p2 = player2;

            // Initialize cells
            for (int i = 0; i < 6; i++)
                for (int j = 0; j < 7; j++)
                    board[i][j] = ' ';
        }

        /**
         * This makes the run method for the thread
         */
        public void run()
        {
            try {
                // Create data input and output streams
                DataInputStream fromPlayer1 = new DataInputStream(p1.getInputStream());
                DataOutputStream toPlayer1 = new DataOutputStream(p1.getOutputStream());
                DataInputStream fromPlayer2 = new DataInputStream(p2.getInputStream());
                DataOutputStream toPlayer2 = new DataOutputStream(p2.getOutputStream());

                // Write something to notify player 1 to start
                toPlayer1.writeInt(1);

                // Continuously serve the players and determine and report
                // the game status to the players
                while (true) {
                    // Get a move from player 1
                    int row = fromPlayer1.readInt();
                    int column = fromPlayer1.readInt();
                    for(int i = 0; i < 6; i++)
                    {
                        if(board[i][column] == ' ')
                        {
                            row = i;
                        }
                    }
                    board[row][column] = 'X';

                    // Checks if Player 1  has won
                    if (checkWin('X')) 
                    {
                        toPlayer1.writeInt(PLAYER1_WON);
                        toPlayer2.writeInt(PLAYER1_WON);
                        sendMove(toPlayer2, row, column);
                        break; // Break the loop
                    }
                    // Check if the game is tied
                    else if (isFull()) 
                    { 
                        toPlayer1.writeInt(DRAW);
                        toPlayer2.writeInt(DRAW);
                        sendMove(toPlayer2, row, column);
                        break;
                    }
                    else 
                    {
                        // Tells player 2 that it is their turn
                        toPlayer2.writeInt(CONTINUE);

                        // Sends player 1's move to player 2
                        sendMove(toPlayer2, row, column);
                    }

                    // Gets a move from Player 2
                    row = fromPlayer2.readInt();
                    column = fromPlayer2.readInt();
                    for(int i = 0; i < 6; i++)
                    {
                        if(board[i][column] == ' ')
                        {
                            row = i;
                        }
                    }
                    board[row][column] = 'O';

                    // Check if Player 2 has won
                    if (checkWin('O'))
                    {
                        toPlayer1.writeInt(PLAYER2_WON);
                        toPlayer2.writeInt(PLAYER2_WON);
                        sendMove(toPlayer1, row, column);
                        break;
                    }
                    else
                    {
                    // Tells player 1 that it is their turn
                    toPlayer1.writeInt(CONTINUE);

                    // Send player 2's move to player 1
                    sendMove(toPlayer1, row, column);
                    }
                }
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }

        /**
         * Sends a players move to the other player
         * @param out how it will send the data to the other player
         * @param row row that the piece is in
         * @param column column that the piece is in
         * @throws IOException
         */
        private void sendMove(DataOutputStream out, int row, int column) throws IOException
        {
            out.writeInt(row); // Send row index
            out.writeInt(column); // Send column index
        }

        /**
         * 
         * @return returns true if board is full
         */
        private boolean isFull()
        {
            for (int i = 0; i < 6; i++)
            {
                for (int j = 0; j < 7; j++)
                {
                    if (board[i][j] == ' ')
                    {
                    	// Board still has room
                        return false;
                    }
                }
            }

            // The board is full
            return true;
        }

        /**
         * Checks to see if someone has won
         * @param token which player might have won
         * @return true if someone has won and false if no one has won
         */
        private boolean checkWin(char token)
        {
            //Horizontal check
            for (int j = 0; j < 7 - 3; j++)
            {
                for (int i = 0; i < 6; i++)
                {
                    if (board[i][j] == token && board[i][j + 1] == token && board[i][j + 2] == token && board[i][j + 3] == token)
                    {
                        return true;
                    }
                }
            }

            //Vertical check
            for (int i = 0; i < 6 - 3; i++)
            {
                for (int j = 0; j < 7; j++)
                {
                    if (board[i][j] == token && board[i + 1][j] == token && board[i + 2][j] == token && board[i + 3][j] == token)
                    {
                        return true;
                    }
                }
            }

            //ascending diagonal check
            for (int i = 3; i < 6; i++)
            {
                for (int j = 0; j < 7 - 3; j++)
                {
                    if (board[i][j] == token &&
                            board[i - 1][j + 1] == token &&
                            board[i - 2][j + 2] == token &&
                            board[i - 3][j + 3] == token)
                    {
                        return true;
                    }
                }
            }

            //Descending diagonal check
            for (int i = 3; i < 6; i++)
            {
                for (int j = 3; j < 7; j++)
                {
                    if (board[i][j] == token &&
                            board[i - 1][j - 1] == token &&
                            board[i - 2][j - 2] == token &&
                            board[i - 3][j - 3] == token)
                    {
                        return true;
                    }
                }
            }
            //No one has won
            return false;
        }
    }

    /**
     * Basic main method
     * @param args basic parameter
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}