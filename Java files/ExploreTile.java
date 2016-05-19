import java.io.Serializable;
import java.util.ArrayList;
/**
 * This class parses each input tile in all possible directions and finds the minimum and maximum tile piece
 *
 */
public class ExploreTile implements Serializable {

	private char[][] inputSquares;
	private ArrayList<NewTile> newTiles = new ArrayList<NewTile>();
	private NewTile maxTile = null, minTile = null;
/**
 * Constructor
 * @param inputSquares
 */
	public ExploreTile(char[][] inputSquares) {
		this.inputSquares = inputSquares;
	}
/**
 * Constructor
 * @param input
 */
	public ExploreTile(ArrayList<char[]> input) {
		inputSquares = new char[input.size()][];
		for (int i = input.size(); i-- > 0;)
			inputSquares[i] = input.get(i);
	}

	public ArrayList<NewTile> getNewTiles() {
		return newTiles;
	}
/**
 * This method retrieves the tile piece of maximum size
 * @return
 */
	public NewTile getMaxTile() {
		return maxTile;
	}
	/**
	 * This method retrieves the tile piece of minimum size
	 * @return
	 */
	public NewTile getMinTile() {
		return minTile;
	}
	/**
	 * This method identifies and scans each individual raw piece and finds the maximum and minimum tile piece
	 */
	public void parse() {
		for (int i = 0; i < inputSquares.length; i++) {
			for (int j = 0; j < inputSquares[i].length; j++) {
				if (inputSquares[i][j] != ' ') {
					NewTile newtile = new NewTile();
					scan(inputSquares, j, i, newtile);
					newTiles.add(newtile);
					if (maxTile == null) {
						maxTile = newtile;
						minTile = newtile;
					} else if (newtile.compareTo(maxTile) > 0) 
						maxTile = newtile;
					else if (newtile.compareTo(minTile) < 0) 
						minTile = newtile;
				}
			}
		}
	}
	/**
	 * limit method checks whether the square piece belongs to the tile considered
	 * @param in
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean limit(char[][] inputSquares, int x, int y) {
		return y >= 0 && y < inputSquares.length && x >= 0 && x < inputSquares[y].length;
	}
	/**
	 * scan method scans each tile raw piece and stores it 
	 * @param in
	 * @param x
	 * @param y
	 * @param piece
	 */
	private void scan(char[][] inputSquares, int x, int y, NewTile newTile) {
		newTile.addSquare(x, y, inputSquares[y][x]);
		inputSquares[y][x] = ' ';
		if (limit(inputSquares, x - 1, y) && inputSquares[y][x - 1] != ' ') 
			scan(inputSquares, x - 1, y, newTile);
		if (limit(inputSquares, x, y - 1) && inputSquares[y - 1][x] != ' ') 
			scan(inputSquares, x, y - 1, newTile);
		if (limit(inputSquares, x, y + 1) && inputSquares[y + 1][x] != ' ') 
			scan(inputSquares, x, y + 1, newTile);
		if (limit(inputSquares, x + 1, y) && inputSquares[y][x + 1] != ' ') 
			scan(inputSquares, x + 1, y, newTile);
	}	
}