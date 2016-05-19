import java.io.Serializable;
import java.util.HashSet;
/**
 * This class stores the properties of each individual square piece
 *
 */
public class NewTile implements Comparable<NewTile> , Serializable{

	protected int id = -1;
	protected HashSet<SquarePiece> coordinates = new HashSet<SquarePiece>();

	protected int left = 0,top = 0, right = 0, bottom = 0;

	public int getTileLeft() {
		return left;
	}

	public int getTileUp() {
		return top;
	}

	public int getTileRight() {
		return right;
	}

	public int getTileDown() {
		return bottom;
	}
	public int size() {
		return coordinates.size();
	}
	/**
	 * This method adds the tile coordinates to the hashset
	 * @param x
	 * @param y
	 * @param ch
	 */
	public void addSquare(int x, int y, char ch) {
		if (coordinates.isEmpty()) {
			left = right = x;
			top = bottom = y;
		} else {
			if (x < left)
				left = x;
			else if (x > right)
				right = x;
			if (y < top)
				top = y;
			else if (y > bottom)
				bottom = y;
		}
		coordinates.add(new SquarePiece(x, y, ch));
	}
	public int compareTo(NewTile o) {
		if (o == null)
			return 1;
		return this.coordinates.size() - o.coordinates.size();
	}
}
