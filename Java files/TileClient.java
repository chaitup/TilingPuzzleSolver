
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *This class will take the input from the server and places the index tile in the first position on the board 
 *and calls the placeTilesOnBoard to check for the possiblities with other tiles from the 2nd position onwards,
 *Returns all generated solutions in a list. Once done closes the connection and initiates it again for the new task
 *
 */public class TileClient {

	  public static Socket client = null;
	  public static OutputStream os = null;
	  public static InputStream is = null;
	  public static List<IsomorphicSolutionCheck> solution = new ArrayList<IsomorphicSolutionCheck>();
	  public static boolean closed = false;
	  public static TargetBoardFill boardFilling = null;
	  
	  public static void main(String[] args) {

	    int portNumber = 9090; //port
	    String host = "10.1.1.254";//This is the default host ip address i.e memviz.memphis.edu 
	    ObjectInputStream ois =null;
	    ObjectOutput oos = null;
	    int row = 0;
		int col = 0;
		int[] coordinates = new int[2];
	    
	    try {
	      client = new Socket(host, portNumber);
	      os = client.getOutputStream();
	      is = client.getInputStream();

	      int presentIndex;
	      int currentOrientation;
	      while(true){
		System.out.println("request sent to server for connection");

	    	//Reading objects sent from server
	    	ois = new ObjectInputStream(is);
	        boardFilling = (TargetBoardFill)ois.readObject();
	      	ois = new ObjectInputStream(is);
	      	presentIndex = (Integer)ois.readObject();
	      	ois=new ObjectInputStream(is);
	      	currentOrientation = (Integer)ois.readObject();
	      	//System.out.println("boardfilling: "+boardFilling);
			//int boardWidth = boardFilling.board.width;
			int totalNumOfTiles = boardFilling.tileState.length;
	      	//Assigning the tile to be placed in the first position on the board
    	    EachTile tile = boardFilling.memberTiles[boardFilling.tileState[presentIndex]];
    	    tile.currentOrient = currentOrientation;
	    	  
    	    //If able to place the tile, mark the tile status as placed, and check if we can proceed forward, if yes then placeTilesOnBoard is called to check other possibilities
    	    if (boardFilling.board.positionTilePiece(tile, boardFilling.startingRow, boardFilling.startingColumn))
    	    {
    	      boardFilling.tileState[presentIndex] -= totalNumOfTiles;
	      	      
		      if(!boardFilling.notPromising()) {//If we can proceed forward, search for the next empty square to place the tiles. Traversing will be done from left to right and top to down

		    	  coordinates = getNextCoordinates(row, col);
		    	  placeTilesOnBoard(coordinates[0], coordinates[1]);// calling this function to check for other possibilities
		    	  
				}
    	    }
	      
		      oos = new ObjectOutputStream(os);
		      oos.writeObject(solution);//sending the solution object to server
		      //closing the object and opening a new connection with server
		      os.close();
		      is.close();
		      client.close();
		      //System.out.println("opening new connection again");
		      client = new Socket(host, portNumber);
		      os = client.getOutputStream();
		      is = client.getInputStream();
	      
	    }    
	      
	    } catch (UnknownHostException e) {
	      System.err.println("Don't know about host " + host);
	    } catch (IOException e) {
	      System.err.println("Couldn't get I/O for the connection to the host "
	          + host);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	  }
	  
/**
* This method will use recursive backtracking algorithm to place tiles on the board, check all possibilities to place the tiles and find the solutions
* 
*/
	  public static void placeTilesOnBoard(int row, int column){
		
		  int totalNumOfTiles = boardFilling.tileState.length;
		  int tileIndex; 
		  int[] coordinates = new int[2];
		  
		  for (int index = 0; index < boardFilling.memberTiles.length; index++) {
				
			  tileIndex = boardFilling.tileState[index];
			  if (tileIndex < 0)
					continue; // tile already used
				 			
				EachTile tile = boardFilling.memberTiles[boardFilling.tileState[index]];
				int totalOrientations = tile.count;
				for (int i = 0; i < totalOrientations; i++) {
					
					tile.currentOrient = i;										
					if (boardFilling.board.positionTilePiece(tile, row, column))
					{
						boardFilling.tileState[index] -= totalNumOfTiles;
						if (boardFilling.board.isFull()) {
							solution.add(boardFilling.board.getSolution());
						} else if (!boardFilling.notPromising()) {	
						     coordinates = getNextCoordinates(row, column);
						     placeTilesOnBoard(coordinates[0], coordinates[1]);
						}							
						boardFilling.board.removeTile(tile, row, column);
						boardFilling.tileState[index] += totalNumOfTiles;					
					}else{
						continue;
					}
				}
			}
		  }
/**
 * This method will take the coordinates and generate the next free coordinates, where next peice can be placed
 * 
 */

	  public static int[] getNextCoordinates(int row, int column){
		  
		int[] coordinates = new int[2];
		int boardWidth = boardFilling.board.width;
		
		while (boardFilling.board.squareId[coordinates[0]][coordinates[1]] != boardFilling.board.id) {
			coordinates[1] = coordinates[1] + 1;
			if (boardWidth == coordinates[1]) {
				coordinates[1] = 0;
				coordinates[0] = coordinates[0]+1;
			}		  
	  }
		return coordinates;
	}
}
