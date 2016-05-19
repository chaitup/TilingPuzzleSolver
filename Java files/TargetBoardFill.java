
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.Serializable;
/**
 * This class is used to parse the input file, places all the tiles on to the target board
 * finds all possible solutions by checking promising ones and finds isomorphic combinations
 *
 */
public class TargetBoardFill implements Serializable{
	private static final long serialVersionUID = 1L;
	public  TargetBoard board;
	public  EachTile[] tiles,memberTiles;
	public  boolean sameTiles;
	public  int firstTile,allTiles=0;
	public int[] tileState,position =  new int[] { TileToken.TURN0, TileToken.TURN90,
			TileToken.TURN180, TileToken.TURN270, TileToken.FLIPTURN0,
			TileToken.FLIPTURN90, TileToken.FLIPTURN180,
			TileToken.FLIPTURN270 };

	public  ArrayList<IsomorphicSolutionCheck> solutions = new ArrayList<IsomorphicSolutionCheck>();
	static long timeForAllSolutions = 0, timeForFirstSolution =0, startingTime = 0;
/**
 * Main method
 * @param args
 */
	public static void main(String[] args){
		try{
			TargetBoardFill boardFilling=new TargetBoardFill();
			boardFilling.fileParsing(args[0]);
			boardFilling.processPuzzle();
			boardFilling.displayConfiguration();
			System.out.println("num of solutions: "+boardFilling.solutions.size());
                       System.out.println("Time for first solution="+timeForFirstSolution);
                       System.out.println("Time for all solutions="+timeForAllSolutions);
		}catch(IOException e){
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to parse the input file and finds the target board
	 * @param fileName
	 * @throws IOException
	 */
	public  void fileParsing(String fileName) throws IOException {
		ArrayList<char[]> inputList = new ArrayList<char[]>();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line = null;
		try
		{
			while ((line = reader.readLine()) != null) 
				inputList.add(line.toCharArray());
		}finally
		{
			reader.close();
		}
		ExploreTile explore = new ExploreTile(inputList);
		explore.parse();
		ArrayList<NewTile> newTiles = explore.getNewTiles(); // total number of coordinates in the input file
		newTiles.remove(explore.getMaxTile());
		board = new TargetBoard(0, explore.getMaxTile());
		tiles = new EachTile[newTiles.size()];
		firstTile = newTiles.get(0).size();
		sameTiles = true;
		int i = 0;
		for (NewTile newTile : newTiles) {
			tiles[i] = new EachTile(i + 1, newTile);
			tiles[i].display();
			allTiles = allTiles + tiles[i].size;
			if (firstTile != tiles[i].size)
				sameTiles = false;
			i++;
		}	
	}
	/**
	 * This method is used to check for orientations of the tiles by calling the corresponding methods and computes the start time and time to find all solutions
	 */
	int startingRow = 0, startingColumn = 0;
	public void processPuzzle() {

		for (int i = 0; i < tiles.length; i++)
			//checks for all the orientations of all the tiles
			tiles[i].check(position);
		if (board.size() == allTiles) {
			memberTiles = new EachTile[tiles.length];
			for (int j = 0; j < tiles.length; j++) 
				memberTiles[j] = tiles[j];
			tileState = new int[memberTiles.length];//it keeps track of which tile is placed on the board
			for (int i = 0; i < memberTiles.length; i++)
				tileState[i] = i;
			startingTime = System.currentTimeMillis();
			TileServer tilingSer = new TileServer();
			tilingSer.main(this);
			timeForAllSolutions = System.currentTimeMillis() - startingTime;

		}  else 
			timeForAllSolutions = 0;
	}
	/**
	 * This method checks if we can proceed further with the current position of the tiles placed
	 * @return
	 */
	public  boolean notPromising() {
		ExploreTile explore = board.getNewBoard();
		explore.parse();
		int size, leastHoleCount = 0, maximumHoleCount = 0,leastSizedTile = Integer.MAX_VALUE,maximumSizedTile = Integer.MIN_VALUE ;
		int leastSizedHole = explore.getMinTile().size();
		int maximumSizedHole = explore.getMaxTile().size();
		for (NewTile newTile : explore.getNewTiles()) {
			size = newTile.size();
			if (size == leastSizedHole)
				leastHoleCount++;
			if (size == maximumSizedHole)
				maximumHoleCount++;
		}
		for (int i = 0; i < memberTiles.length; i++) {
			if (tileState[i] < 0)
				continue;
			if (memberTiles[tileState[i]].size < leastSizedTile)
				leastSizedTile = memberTiles[tileState[i]].size;
			if (memberTiles[tileState[i]].size == leastSizedHole)
				leastHoleCount--;
			if (memberTiles[tileState[i]].size > maximumSizedTile)
				maximumSizedTile = memberTiles[tileState[i]].size;
			if (memberTiles[tileState[i]].size == maximumSizedHole)
				maximumHoleCount--;
		}
		return leastSizedTile > leastSizedHole
				|| (leastSizedTile == leastSizedHole && leastHoleCount > 0)
				|| maximumSizedTile > maximumSizedHole
				|| (maximumSizedTile == maximumSizedHole && maximumHoleCount < 0);
	}
	/**
	 * This method displays all the possible solutions 
	 */
	public  void displayConfiguration() {
		System.out.println("Number of Solutions: " + solutions.size());
		int count = 1;
		for (IsomorphicSolutionCheck solution : solutions) {
			System.out.println("Solution: " + count++);
			solution.display();
			System.out.println();
		}
	}
	/**
	 * This method checks if the present solution matches with any of the previous solution, if yes return true
	 * @param s
	 * @return
	 */
	public  boolean isomorphic(IsomorphicSolutionCheck soln) {
		for (int i = 0; i < solutions.size();) {
			if (soln.identical(solutions.get(i))) {
				return true;
			} else
				i++;
		}
		return false;
	}
}
