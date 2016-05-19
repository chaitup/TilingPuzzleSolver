import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
/**
 * This class processes all the orientations of a tile piece
 *
 */
public class EachTile extends TileToken implements Serializable{
	private NewTile newTile;
	public int size;

	int[][] x_coordinate,y_coordinate,tileDimension;
	char[][] value;
	int count,currentOrient,orient,maxX,item,x,y;
	SquarePiece[] squareCoordinates,referenceCoordinates;
	boolean duplicate;
	/**
	 * Constructor
	 * @param id
	 * @param newTile
	 */
	public EachTile(int id, NewTile newTile) {
		this.id = id;
		this.newTile = newTile;
		this.size = newTile.size();
		check(new int[] { TileToken.TURN0  });
	}
	/**
	 * This method is called from processPuzzle method with all types of orientations and the above method
	 * @param position
	 */
	public void check(int[] position) {
		int tileLeft = newTile.getTileLeft();
		int tileUp = newTile.getTileUp();
		//It gets the right coordinates of the tile with respect to origin
		this.right = newTile.getTileRight() - tileLeft;
		this.down = newTile.getTileDown() - tileUp;
		ArrayList<SquarePiece[]> FirstCoordinates = new ArrayList<SquarePiece[]>();
		ArrayList<int[]> InitialDimensions = new ArrayList<int[]>();
		count = 0;
		for (int i = 0; i < position.length; i++) {
			orient = position[i];
			maxX = Integer.MAX_VALUE;
			for (SquarePiece square : newTile.coordinates) {
				// making all points in the new tile from (0,0). In new tile, the values are taken w.r.t file position
				x = square.x_ord - tileLeft; 
				y = square.y_ord - tileUp;
				if (getYCoordinate(x, y, orient) == 0) {
					if (getXCoordinate(x, y, orient) <= maxX) 
						//for 0 orientation the value of maxX would be the right most value of x
						maxX = getXCoordinate(x, y, orient);
				}
			}
			item = 0;
			squareCoordinates = new SquarePiece[this.size];
			for (SquarePiece square : newTile.coordinates) {
				x = square.x_ord - tileLeft;
				y = square.y_ord - tileUp;
				// this is the new position of points of tile for a particular rotation
				//for the above statement for 0 orientation the values of x starts from -maxX to 0 and same y values
				squareCoordinates[item++] = new SquarePiece(getXCoordinate(x, y, orient) - maxX,
						getYCoordinate(x, y, orient), square.value);
			}
			//sort(T[] a, Comparator<? super T> c):Sorts the specified array of objects according to the order induced by the specified comparator
			Arrays.sort(squareCoordinates, new Comparator<SquarePiece>() {

				public int compare(SquarePiece rotate1, SquarePiece rotate2) {
					return (rotate1.y_ord - rotate2.y_ord) != 0 ? rotate1.y_ord - rotate2.y_ord : rotate1.x_ord - rotate2.x_ord;
				}
			});

			//If new, add to FirstCoordinates
			//This is used to prevent repetitions, suppose if both flip and rotation has same coordinates 
			//then, it is not inserted into the FirstCoordinates, hence it sufficient to check only 2 orientations of the tile
			duplicate = false;
			for (currentOrient = count; currentOrient-- > 0 && !duplicate;)
			{
				//referenceCoordinates holds the square array that contains points for one particular orientation
				referenceCoordinates = FirstCoordinates.get(currentOrient);
				for (item = size; item-- > 0;) {
					if (!referenceCoordinates[item].equals(squareCoordinates[item])) {
						break;
					}
				}
				//This will be true only of all the elements in the referenceCoordinates and points match at least for one orientation
				duplicate = item == -1;
			}

			if (!duplicate) {
				FirstCoordinates.add(squareCoordinates);
				if ((orient & TURN90_270) == 0)
					InitialDimensions.add(new int[] { down + 1, right + 1 });
				else
					InitialDimensions.add(new int[] { right + 1, down + 1 });
				count++;
			}
		}
		//orientation order will be according to how the orientation parameters are passed
		x_coordinate = new int[count][size];
		y_coordinate = new int[count][size];
		value = new char[count][size];
		tileDimension = new int[count][];
		//after getting coordinates for all orientations, add different coordinates for orientations are stored in X,Y
		for (currentOrient = 0; currentOrient < FirstCoordinates.size(); currentOrient++) {
			referenceCoordinates = FirstCoordinates.get(currentOrient);
			for (item = 0; item < size; item++) {
				x_coordinate[currentOrient][item] = referenceCoordinates[item].x_ord;
				y_coordinate[currentOrient][item] = referenceCoordinates[item].y_ord;
				value[currentOrient][item] = referenceCoordinates[item].value;
			}
			tileDimension[currentOrient] = InitialDimensions.get(currentOrient);
		}
		currentOrient = 0;
	}
	/**
	 * This method gets the square coordinates with the current orientation
	 */
	public SquareCoordinates[][] getData() {
		int adjX = Integer.MAX_VALUE;
		for (int i = x_coordinate[currentOrient].length; i-- > 0;) {
			if (x_coordinate[currentOrient][i] < adjX)
				adjX = x_coordinate[currentOrient][i];
		}
		SquareCoordinates[][] data = new SquareCoordinates[tileDimension[currentOrient][0]][tileDimension[currentOrient][1]];
		for (int j = data.length; j-- > 0;)
			for (int i = data[j].length; i-- > 0;)
				data[j][i] = new SquareCoordinates(id, ' ');

		for (int index = x_coordinate[currentOrient].length; index-- > 0;) {
			data[y_coordinate[currentOrient][index]][x_coordinate[currentOrient][index] - adjX]
					.setB(value[currentOrient][index]);
		}
		return data;
	}
}
