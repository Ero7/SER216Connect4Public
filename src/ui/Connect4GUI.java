/**Connect4GUI Description: This is the class that makes and runs a game of Connect4 with a GUI (Using Javafx)
 * @author Everett Olson
 * @version 2.0
 * 
 */
package ui;
import core.Connect4;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Connect4GUI extends Application 
{
	private static final int TILE_SIZE = 80;
	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	public boolean redTurn = true;
	private boolean PVP;
	private int moves = 0;
	
	private Scene start;
	private Scene end;
	private Stage window;
	
	private Connect4 c4;
	private Disc [][] grid = new Disc[COLUMNS][ROWS];
	private Pane discRoot = new Pane();
	private Label t = new Label("Red's Turn");
	

	/** This is the initial window that prompts the user if they want to play PvP or PvE
	 * @param st this is where all the content will be displayed
	 */
	public void start(Stage st) throws Exception
	{
		window = st;
		Label l1 = new Label("Welcome to Connect4");
		Button pvp = new Button("Player Vs Player");
		Button pvc = new Button("Player Vs Computer");
		
		pvp.setOnAction(e-> st.setScene(new Scene(makeGraphics(true))));
		pvc.setOnAction(e-> st.setScene(new Scene(makeGraphics(false))));
		
		VBox layout = new VBox(20);
		layout.getChildren().addAll(l1, pvp, pvc);
		start = new Scene(layout,450,300);
		//stage.setScene(new Scene(createContent()));
		st.show();
		window.setScene(start);
		window.show();
	}
	
	public void startClient()
	{
		Stage stage = new Stage();
		stage.setScene(new Scene(makeGraphics(true)));
		stage.show();
	}
	
	/**
	 * This places a piece into the board
	 * @param disc the shape that will be placed
	 * @param col where the user selected the piece to be placed
	 */
	private void placePiece(Disc disc, int col)
	{
		int r = ROWS-1;
		int cpuTurn = -1;
		
				if(moves == 41)
				{
					gameEnd();
				}
				
		//CPU turn
		if(!redTurn && !PVP)
		{
			cpuTurn = c4.turnGUI(redTurn, col, ROWS- r -1);
			col = cpuTurn;
		}
		else
		{
			c4.turnGUI(redTurn, col, ROWS-r);
		}
	
		do
		{
			if(!getPiece(col,r).isPresent())
			{
				break;
			}
			
			r--;
		}while(r >= 0);
		
		if(r < 0)
		{
			return;
		}
		
		boolean win = c4.checkWin(redTurn ? 1 : 0);
		
		if(cpuTurn != -1)
		{
			col = cpuTurn;
		}
		
		grid[col][r] = disc;
		discRoot.getChildren().add(disc);
		disc.setTranslateX(col * (TILE_SIZE + 5) + TILE_SIZE / 4);
		disc.setTranslateY(r*(TILE_SIZE + 5) + TILE_SIZE / 4);
		
		if(win)
		{
			gameEnd();
		}
		
		redTurn = !redTurn;
		
		//for PvP game
		if(PVP)
		{
			if(!redTurn)
			{
				t.setText("Yellow's Turn");
			}
			else
			{
				t.setText("Red's Turn");
			}
		}
		else
		{
			if(!redTurn) {
				t.setText("Computer's turn, click to continue");
			} else {
				t.setText("Your turn");
			}
		}
		moves++;
	}
	
	/**
	 * This is how the game calls to end when someone has won
	 */
	private void gameEnd()
	{
		Label eL = new Label();
		
		if(moves == 41)
		{
			eL.setText("Tie game");
		}
		else if(PVP)
		{
			eL.setText("Winner: " + (redTurn ? "Red" : "Yellow"));
		}
		else
		{
			eL.setText("Winner: " + (redTurn ? "You" : "Computer"));
		}
		Button xB = new Button("Exit");
		xB.setOnAction(e -> window.close());
		
		VBox layout2 = new VBox(50);
		layout2.setMinSize(50, 50);
		layout2.getChildren().addAll(eL,xB);
		end = new Scene(layout2, 450, 300);
		window.setScene(end);
		
	}

	
	/**
	 * Creates the things that will be put into the Scene
	 * @param isP This checks to see if the game is PvP or PvC
	 * @return borderPane is returned and this shows the full board and a bottom pane that has prompts in it for the user
	 */
	private Parent makeGraphics(boolean isP)
	{
		
		c4 = isP ? new Connect4(0) : new Connect4(1);
		PVP = isP;
		
		Pane root = new Pane();
		root.getChildren().add(discRoot);
		Shape gridShape = makeArray();
		root.getChildren().add(gridShape);
		root.getChildren().addAll(createColumns());
		
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(root);
		borderPane.setBottom(t);
		
		return borderPane;
	}
	
	/**
	 * Makes rectangle that align with the columns to show what the user can select
	 * @return This makes it show the user can see which column they are selecting and returns the list of them
	 */
	private List<Rectangle> createColumns()
	{
		List<Rectangle> list = new ArrayList<>();
		
		for(int i = 0; i < COLUMNS; i++)
		{
			Rectangle rect = new Rectangle(TILE_SIZE, (ROWS + 1) * TILE_SIZE);
			rect.setTranslateX(i*(TILE_SIZE +5) + TILE_SIZE /4);
			rect.setFill(Color.TRANSPARENT);
			
			rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(200,200,50,0.3)));
			rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));
			
			final int column = i;
			rect.setOnMouseClicked(e -> placePiece(new Disc(redTurn), column));
			
			list.add(rect);
		}
		return list;
	}
	
	/**
	 * Makes a Rectangle with a bunch of circles cut of from it
	 * @return This returns the actual gameboard itself so the players have a visual representation of the game to play with
	 */
	private Shape makeArray()
	{
		Shape shape = new Rectangle((COLUMNS +1)*TILE_SIZE, (ROWS +1) * TILE_SIZE);
		
		for(int i = 0; i < ROWS; i++)
		{
			for(int j = 0; j< COLUMNS; j++)
			{
				Circle circle = new Circle(TILE_SIZE/2);
				circle.setCenterX(TILE_SIZE/2);
				circle.setCenterY(TILE_SIZE/2);
				circle.setTranslateX(j*(TILE_SIZE+5) + TILE_SIZE /4);
				circle.setTranslateY(i*(TILE_SIZE+5) + TILE_SIZE /4);
				
				shape = Shape.subtract(shape, circle);
				
			}
		}
		Light.Distant light = new Light.Distant();
		light.setAzimuth(45.0);
		light.setElevation(30);
		
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(5.0);
		
		shape.setFill(Color.AQUA);
		shape.setEffect(lighting);
		return shape;
	}
	

		
	/**
	 * Checks to see if there are pieces in the game board
	 * @param col the column selected
	 * @param r the row selected
	 * @return a placed piece
	 */
	private Optional<Disc> getPiece(int col, int r)
	{
		if(col < 0 || col >= COLUMNS || r < 0 || r>= ROWS)
		{
			return Optional.empty();
		}
		return Optional.ofNullable(grid[col][r]);
	}
	
	/**
	 * This is the class for a piece used in the game
	 * @author Everett
	 * 
	 */
	private static class Disc extends Circle
	{
		private final boolean Red;
		public Disc(boolean red)
		{
			super(TILE_SIZE/2, red ? Color.RED : Color.YELLOW);
			Red = red;
			
			setCenterX(TILE_SIZE /2);
			setCenterY(TILE_SIZE /2);
		}
	}
	
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
