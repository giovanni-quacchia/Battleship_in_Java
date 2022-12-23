package model;

/**
 * Node of the linked list Ship.
 * 
 * @author Giovanni Quacchia
 * 
 */
public class Cell 
{
	/**
	 * isShot = true if the cell was hit by the opponent
	 */
	private boolean isShot=false;
	/**
	 * Coordinate of the cell (letter;number)
	 */
	private String cor;
	
	/**
	 * pointer to the next node
	 */
	public Cell next;
	
	/**
	 * Constructor for creating a node.
	 */
	public Cell()
	{
		isShot = false;
		next = null;
	}
	
	/**
	 * it returns isShot value
	 * @return isShot value
	 */
	public boolean isShot() {
		return isShot;
	}

	/**
	 * it sets isShot value
	 * @param isShot isShot new value
	 */
	public void setShot(boolean isShot) {
		this.isShot = isShot;
	}

	/**
	 * it sets cor value
	 * @param cor coordinate new value
	 */
	public void setCor(String cor)
	{
		this.cor=cor;
	}
	/**
	 * it returns cor value
	 * @return coordinate value
	 */
	public String getCor()
	{
		return cor;
	}

   
}
