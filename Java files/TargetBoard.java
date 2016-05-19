import java.io.Serializable;

/**
 * This class is used to place and remove tiles from the target board
 *
 */

public class TargetBoard extends TileToken implements Serializable {

	char[][] square;
	int[][] squareId;
	public int size,unfilledSquare = 0,height,width;
	/**
	 * This constructor is used to initialize all the square pieces of the target board to some value
	 * @param id
	 * @param raw
	 */
	public TargetBoard(int id, NewTile newTile) {
		this.id = id;
		this.size = newTile.size();
		this.unfilledSquare = size;
		int tileLeft = newTile.getTileLeft();
		int tileUp = newTile.getTileUp();
		this.right = newTile.getTileRight() - tileLeft;
		this.down = newTile.getTileDown() - tileUp;

		height = down+1;
		width = right+1;
		square = new char[height][width];
		squareId = new int[height][width];
		//initializing the target board with empty values and each square piece has been assigned a value of -1
		for (int j = square.length; j-- > 0;) {
			for (int i = square[j].length; i-- > 0;) {
				square[j][i] = ' ';
				squareId[j][i] = -1;
			}
		}
		for (SquarePiece sq : newTile.coordinates) {
			//put the values of the board making the board corrdinated from 0
			square[sq.y_ord - tileUp][sq.x_ord - tileLeft] = sq.value;
			//so except for holes all the squares will be having some value
			squareId[sq.y_ord - tileUp][sq.x_ord - tileLeft] = id;
		}
	}
	/**
	 * This method places a tile on the target board by checking whether the tile fits the current configuration or not 
	 * @param tile
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean positionTilePiece(EachTile tile, int row, int col) {
		int height = down+1, width = right+1,x, y;;
		int[] X = tile.x_coordinate[tile.currentOrient];
		int[] Y = tile.y_coordinate[tile.currentOrient];
		char[] value = tile.value[tile.currentOrient];
		for (int i = tile.size-1; i > 0;i--) {
			x = col + X[i];
			y = row + Y[i];
			if (y < 0 || y >= height || x < 0 || x >= width	|| square[y][x] != value[i] || squareId[y][x] != id) 
				return false;
		}
		for (int index = tile.size; index-- > 0;) {
			x = col + X[index];
			y = row + Y[index];
			squareId[y][x] = tile.id;
		}
		unfilledSquare -= tile.size;
		return true;
	}
	/**
	 * This method removes the tile to recursively backtrack
	 * @param tile
	 * @param row
	 * @param col
	 */
	public void removeTile(EachTile tile, int row, int col) {
		int[] pX = tile.x_coordinate[tile.currentOrient];
		int[] pY = tile.y_coordinate[tile.currentOrient];
		int x, y;
		for (int index = tile.size; index-- > 0;) {
			x = col + pX[index];
			y = row + pY[index];

			squareId[y][x] = id;
		}
		unfilledSquare += tile.size;
	}
/**
 * This method just takes the values present in each square and creates a new board
 * @return
 */
	//
	public ExploreTile getNewBoard() {

		char[][] data = new char[height][width];
		for (int j = height; j-- > 0;)
			for (int i = width; i-- > 0;)
				data[j][i] = square[j][i];

		for (int j = height; j-- > 0;)
			for (int i = width; i-- > 0;)
				if (squareId[j][i] != id)
					data[j][i] = ' ';

		return new ExploreTile(data);
	}
/**
 * Calls isomorphicCheck method to check for symmetry
 * @return
 */
	public IsomorphicSolutionCheck getSolution() {
		return new IsomorphicSolutionCheck(this, getData());
	}

	/**
	 * Returns the size of the tile
	 * @return
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks whether the board is full
	 * @return
	 */
	public boolean isFull() {
		return unfilledSquare == 0;
	}

	/**
	 * This method returns the coordinates of each individual square piece
	 */
	public SquareCoordinates[][] getData() {
		SquareCoordinates[][] data = new SquareCoordinates[height][width];
		for (int j = square.length; j-- > 0;)
			for (int i = square[j].length; i-- > 0;)
				data[j][i] = new SquareCoordinates(squareId[j][i], square[j][i]);
		return data;
	}
}
