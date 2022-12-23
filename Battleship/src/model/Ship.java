package model;

/**
 * Linked list of Cells
 * 
 * @author Giovanni Quacchia
 *
 */
public class Ship 
{
	/**
	 * head of the list
	 */
	private Cell head;
	/**
	 * size of the ship
	 */
	private int size;
	
	/**
	 * directions that a ship can take
	 * 
	 * @author Giovanni Quacchia
	 * 
	 */
	public enum Direction
	{
		/**
		 * ship is positioned horizontally
		 */
		HORIZONTAL,
		/**
		 * ship is positioned vertically
		 */
		VERTICAL
	}
	/**
	 * ship's direction
	 */
	private Direction dir;
	/**
	 * True if a ship has been turned to be moved
	 */
	private boolean isTurned = false;
	
	/**
	 * Constructor for creating a new ship
	 * 
	 * @param cor First coordinate of the list's head
	 * @param size Dimension of a ship. (number of nodes)
	 * @param dir Direction of a ship
	 * 
	 */
	public Ship(String cor, int size, Direction dir) 
	{
		this.size=size;
		this.dir=dir;
		
		// set head
		head = new Cell();
		head.setCor(cor);
		
		// set the rest of the ship
		setShip();
	}
	
	/**
	 * it linkes new nodes the the head
	 */
	public void setShip()
	{
		// aggiungo le altre celle
		for(int i = 1; i < size; i++)
		{
			add(i);
		}
	}
	
	/**
	 * it adds new cells to the ship and links them
	 * 
	 * @param i index of the node (0 for head, by increasing for the following nodes)
	 * 
	 */
	public void add(int i)
	{
		Cell newShip = new Cell();

		// scroll through the list until I find the last node to attach the new Cell
		Cell iter = head; 
		while (iter.next != null) {iter = iter.next;} 

		// coordinate is split for managing it
		String[] coordinate = head.getCor().split(";"); //coordinate[0]: letter, coordinate[1]: number
		String c = updateCor(coordinate, i);
		newShip.setCor(c);
		iter.next = newShip;
	}
	
	/**
	 * it states the coordinate for the new cell
	 * 
	 * @param coordinate Head coordinate split
	 * @param i index of the cell
	 * @return coordinate of the new cell
	 * 
	 */
	public String updateCor(String[] coordinate, int i)
	{
		// if the ship is set vertical, increase the number
		if(dir == Direction.VERTICAL)
		{
			coordinate[1] = String.valueOf(Integer.valueOf(coordinate[1])+i);
		}
		// if the ship is set horizontal, increase the letter
		else
		{
			coordinate[0] = String.valueOf((char)(Integer.valueOf(coordinate[0].charAt(0))+i));
		}
		return String.join(";",coordinate);
	}
	
	/**
	 * It prints all the coordinates of a ship (useful for debugging)
	 */
//	public void stampa()
//	{
//		Cell iter = head; 
//		while (iter != null) 
//		{
//			System.out.println(iter.getCor());
//			iter = iter.next;
//		} 
//	}
	
	/**
	 * it checks if a coordinate belongs to this ship
	 * 
	 * @param coordinate coordinate to check
	 * @return true if the coordinate belongs to this ship
	 */
	public boolean isShip(String coordinate)
	{
		Cell iter = head; 
		while(iter != null) 
		{
			if(iter.getCor().compareTo(coordinate) == 0)
				return true;
			iter = iter.next;
		} 
		return false;
	}
	
	/**
	 * It checks if a coordinate identifies this ship's head
	 * 
	 * @param coordinate coordinate to check
	 * @return true if the coordinate identifies the ship's head
	 * 
	 */
	public boolean isHead(String coordinate)
	{
		if(head.getCor().compareTo(coordinate) == 0)
			return true;
		
		return false;
	}
	

	/**
	 * It checks if a coordinate identifies this ship's tail
	 * 
	 * @param coordinate coordinate to check
	 * @return true if the coordinate identifies the ship's tail
	 * 
	 */
	public boolean isLast(String coordinate)
	{
		Cell iter = head;
		while(iter.next != null) 
			iter = iter.next;
		
		if(iter.getCor().compareTo(coordinate) == 0)
			return true;
		
		return false;
	}
	
	/**
	 * it checks if the ship has sunk
	 * 
	 * @return true if all the cells of the ship have been hit
	 * 
	 */
	public boolean isSunk()
	{
		Cell iter = head; 
		while(iter != null) 
		{
			if(!iter.isShot())
				return false;
			iter = iter.next;
		} 
		return true;
	}
	

	/**
	 * It finds a cell of this ship
	 * 
	 * @param coordinate coordinate associated with the cell to find
	 * @return the cell found, or null if the cell is not found
	 * 
	 */
	public Cell getCell(String coordinate)
	{
		Cell iter = head; 
		while(iter != null) 
		{
			if(iter.getCor().compareTo(coordinate) == 0)
				return iter;
			iter = iter.next;
		} 
		return null;
	}

	/**
	 * It returns Head Cell of this ship
	 * 
	 * @return Head cell
	 */
	public Cell getHead() {
		return head;
	}

	/**
	 * It returns the number of cells of this ship
	 * 
	 * @return size value
	 */
	public int getSize() {
		return size;
	}

	/**
	 * It returns direction of this ship
	 * 
	 * @return dir value
	 */
	public Direction getDirection() {
		return dir;
	}
	
	/**
	 * It changes the direction of the ship. If it is horizontal, it becomes vertical and vice versa
	 */
	public void changeDirection()
	{
		if(dir == Direction.HORIZONTAL)
			dir=Direction.VERTICAL;
		else
			dir=Direction.HORIZONTAL;
	}

	/**
	 * it returns true if the ship has been turned during the repositioning
	 * 
	 * @return isGirata value
	 */
	public boolean isTurned() {
		return isTurned;
	}

	/**
	 * It changes isGirata value. if it is true, it becomes false and vice versa.
	 */
	public void turn()
	{
		if(isTurned)
			isTurned=false;
		else
			isTurned=true;
	}
}
