import java.io.Serializable;

/**
 * This class stores the coordinates and value of a square piece
 *
 */
public class SquarePiece implements Serializable{
	int x_ord,y_ord;
	char value;
	/**
	 * Constructor
	 * @param x_ord
	 * @param y_ord
	 * @param value
	 */
	public SquarePiece(int x_ord, int y_ord, char value) {
		this.x_ord = x_ord;
		this.y_ord = y_ord;
		this.value = value;
	}
	/**
	 * Returns X coordinate of square piece
	 * @return
	 */
	public int getXOrdinate() {
		return x_ord;
	}
	/**
	 * Returns Y coordinate of square piece
	 * @return
	 */
	public int getYOrdinate() {
		return y_ord;
	}
	/**
	 * Returns value of square piece
	 * @return
	 */
	public char getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SquarePiece))
			return false;
		SquarePiece o = (SquarePiece) obj;
		return this.x_ord == o.x_ord && this.y_ord == o.y_ord && this.value == o.value;
	}
	@Override
	public String toString() {
		return String.format("(%s %s:%s)", x_ord, y_ord, value);
	}
}
