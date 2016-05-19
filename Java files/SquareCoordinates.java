import java.io.Serializable;

/**
 * This class stores the identifier and value of each individual square piece
 * 
 */
public class SquareCoordinates implements Serializable{
	int a;
	char b;

	/**
	 * Constructor
	 * @param a
	 * @param b
	 */
	public SquareCoordinates(int a, char b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * returns Identifier
	 * @return
	 */
	public int getA() {
		return a;
	}
	/**
	 * sets Identifier
	 * @param a
	 */
	public void setA(int a) {
		this.a = a;
	}

	/**
	 * Returns Value
	 * @return
	 */
	public char getB() {
		return b;
	}
	/**
	 * sets value
	 * @param b
	 */
	public void setB(char b) {
		this.b = b;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SquareCoordinates))
			return false;
		SquareCoordinates o = (SquareCoordinates) obj;
		return this.a == o.getA() && this.b == o.getB();
	}

	@Override
	public String toString() {
		return String.format("(%s-%s)", a, b);
	}
}
