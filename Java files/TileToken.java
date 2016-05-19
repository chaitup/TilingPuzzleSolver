import java.io.Serializable;

/**
 * This class stores the x and y coordinates of a tile orientation
 *
 */
public abstract class TileToken implements Serializable{
	public static final int TURN0 = 1,TURN90 = 2,TURN180 = 3,TURN270 = 4,FLIPTURN0 = 5,FLIPTURN90 = 6,FLIPTURN180 = 7,FLIPTURN270 = 8;
	public static final int TURN90_270 = TURN90 | TURN270 | FLIPTURN90| FLIPTURN270;
	public int id = -1,right = 0,down = 0;

	public abstract SquareCoordinates[][] getData();
	/**
	 * getXCoordinate() method returns the x coordinate after making specified rotation
	 * @param x
	 * @param y
	 * @param position
	 * @return
	 */
	protected int getXCoordinate(int x, int y, int position) {
		int r=right-x;
		int d= down-y;
		switch (position) {
		case 2: return y;
		case 3: return r;
		case 4: return d;
		case 5: return r;
		case 6: return y;
		case 7: return x;
		case 8: return d;
		default: return x;
		}
	}
	/**
	 * getYCoordinate() method returns the y coordinate after making specified rotation
	 * @param x
	 * @param y
	 * @param position
	 * @return
	 */
	protected int getYCoordinate(int x, int y, int position) {
		int r=right-x;
		int d= down-y;
		switch (position) {
		case 2: return r;
		case 3: return d;
		case 4: return x;
		case 5: return y;
		case 6: return x;
		case 7: return d;
		case 8: return r;
		default: return y;
		}
	}
	/**
	 * This method displays all the tiles
	 */
	public void display() {
		SquareCoordinates[][] coord = getData();
		int i=0,j=0;
		while(i < coord.length)
		{		
			while(j < coord[i].length)
			{
				System.out.print(coord[i][j]);
				j++;
			}
			i++;
		}
		System.out.println();
	}
}
