package model;

import java.util.Random;

/**
 * It manages user's ships
 * 
 * @author Giovanni Quacchia
 *
 */
public class Manage 
{
	/**
	 * Array with all player's ships
	 */
	private Ship[] player_ships;
	
	/**
	 * Random object for getRandomCor and getRandomDir methods
	 */
	private  Random random = new Random();
	
	/**
	 * it sets the initial position of the ships
	 */
	public Manage() 
	{
		setRandomShipsPosition();
	}
	
	/**
	 * It checks if a coordinte is associated with a ship
	 * 
	 * @param coordinate coordinate to check
	 * @return true if the coordinate is associated with a ship
	 */
	public boolean isShip(String coordinate)
	{
		for(int i=0; i<player_ships.length; i++)
		{
			if(player_ships[i] != null && player_ships[i].isShip(coordinate))
					return true;
		}
		
		return false;
	}
	
	/**
	 * It finds a ship associated with a coordinate
	 * 
	 * @param coordinate coordinate to check
	 * @return the ship associated with a coordinated or null if the ship is not found
	 */
	// Data una cordinata di una nave, trova la nave
	public Ship getShip(String coordinate)
	{
		for(int i=0; i<player_ships.length; i++)
		{
			if(player_ships[i].isShip(coordinate))
				return player_ships[i];
		}
		return null;
	}
	
	/**
	 * It checks if a ship has ships nearby itself
	 * 
	 * @param oldShip Ship to not check during the control (newShip can be placed close to itself (oldShip) when it is moved)
	 * @param newShip Ship to check
	 * @return true if newShip has ships nearby, false otherwise
	 */
	public boolean isShipNear(Ship oldShip, Ship newShip)
	{	
		// Slide the ship for checking it
		Cell iter = newShip.getHead(); 
		while (iter != null) 
		{
			// cell coordinate
			String[] c = iter.getCor().split(";"); 
			
			// if the ship has size 1, check all the cells around
			if(newShip.getSize()==1)
			{
				// from the previous column to the next one of the ship (ex.: ship in E5, check D,E,F)
				for(int i = Integer.valueOf(c[0].charAt(0))-1; i < Integer.valueOf(c[0].charAt(0)) + 2; i++)
				{
					// from the previous row to the next one of the ship (ex.: ship in E5, check 4,5,6)
					for(int j = Integer.valueOf(c[1])-1; j < Integer.valueOf(c[1]) + 2; j++)
					{
						String to_check = (char)i+";"+j;
						// check all the cells, except the one in which the ship will be placed and those in which the ship was placed before
						if(to_check.compareTo(newShip.getHead().getCor())!= 0 && !oldShip.isShip(to_check))
						{
							//System.out.println(to_check);
							if(isShip(to_check))
								return true;
						}
							
					}
				}
			}
			else
			{
				// if the ship is horizontal
				if(newShip.getDirection() == Ship.Direction.HORIZONTAL)
				{
					// head of the ship --> check cells on the left
					if(newShip.isHead(iter.getCor()))
					{
						// from the previous column to that one of the head (ex.: ship in E5, chek D,E)
						for(int i = Integer.valueOf(c[0].charAt(0))-1; i < Integer.valueOf(c[0].charAt(0)) + 1; i++)
						{
							// from the previous row to the next one of the ship (ex.: ship in E5, check 4,5,6)
							for(int j = Integer.valueOf(c[1])-1; j < Integer.valueOf(c[1]) + 2; j++)
							{
								String to_check = (char)i+";"+j;
								// check cells
								if(to_check.compareTo(newShip.getHead().getCor())!= 0 && !oldShip.isShip(to_check))
								{
									//System.out.println(to_check);
									if(isShip(to_check))
										return true;
								}
									
							}
						}
						
					}
					// tail of the ship --> check cells on the right
					else if(newShip.isLast(iter.getCor()))
					{
						// from cell's column to the next one (ex.: ship in E5, check E,F)
						for(int i = Integer.valueOf(c[0].charAt(0)); i < Integer.valueOf(c[0].charAt(0)) + 2; i++)
						{
							// from the previous row to the next one of the ship (ex.: ship in E5, check 4,5,6)
							for(int j = Integer.valueOf(c[1])-1; j < Integer.valueOf(c[1]) + 2; j++)
							{
								String to_check = (char)i+";"+j;
								// check cells
								if(to_check.compareTo(newShip.getHead().getCor())!= 0 && !oldShip.isShip(to_check))
								{
									//System.out.println(to_check);
									if(isShip(to_check))
										return true;
								}
									
							}
						}
					}
					// body of the ship
					else
					{
						// Otherwise check cells above and below
						for(int j = Integer.valueOf(c[1])-1; j < Integer.valueOf(c[1]) + 2; j++)
						{
							String to_check = c[0]+";"+j;
							// check cells
							if(to_check.compareTo(newShip.getHead().getCor())!= 0 && !oldShip.isShip(to_check))
							{
								//System.out.println(to_check);
								if(isShip(to_check))
									return true;
							}
								
						}
						
					}
						
				}
				
				// if the ship is vertical
				if(newShip.getDirection() == Ship.Direction.VERTICAL)
				{
					// head of the ship --> check cells at the top
					if(newShip.isHead(iter.getCor()))
					{
						// from the previous column to the next one of the ship (ex.: ship in E5, check D,E,F)
						for(int i = Integer.valueOf(c[0].charAt(0))-1; i < Integer.valueOf(c[0].charAt(0)) + 2; i++)
						{
							// from the previous row to that one of the ship (ex.: ship in E5, check 4,5)
							for(int j = Integer.valueOf(c[1])-1; j < Integer.valueOf(c[1]) + 1; j++)
							{
								String to_check = (char)i+";"+j;
								// check cells
								if(to_check.compareTo(newShip.getHead().getCor())!= 0 && !oldShip.isShip(to_check))
								{
									//System.out.println(to_check);
									if(isShip(to_check))
										return true;
								}
									
							}
						}
						
					}
					// tail of the ship --> check cells at the bottom
					else if(newShip.isLast(iter.getCor()))
					{
						// from the previous column to the next one of the ship (ex.: ship in E5, check D,E,F)
						for(int i = Integer.valueOf(c[0].charAt(0))-1; i < Integer.valueOf(c[0].charAt(0)) + 2; i++)
						{
							// from cell's row to the next one (ex.: ship in E5, check 5,6)
							for(int j = Integer.valueOf(c[1]); j < Integer.valueOf(c[1]) + 2; j++)
							{
								String to_check = (char)i+";"+j;
								// check cells
								if(to_check.compareTo(newShip.getHead().getCor())!= 0 && !oldShip.isShip(to_check))
								{
									//System.out.println(to_check);
									if(isShip(to_check))
										return true;
								}
									
							}
						}
					}
					// body of the ship
					else
					{
						// otherwise check cells on the left and on the right
						for(int i = Integer.valueOf(c[0].charAt(0))-1; i < Integer.valueOf(c[0].charAt(0)) + 2; i++)
						{
							String to_check = (char)i+";"+c[1];
							// check cells
							if(to_check.compareTo(newShip.getHead().getCor())!= 0 && !oldShip.isShip(to_check))
							{
								//System.out.println(to_check);
								if(isShip(to_check))
									return true;
							}
								
						}
						
					}
				}
			}
			
			iter = iter.next;
		} 
		return false;
	}
	
	/**
	 * It changes a ship
	 * 
	 * @param oldShip ship to remove
	 * @param newShip ship to add, instead of the old one
	 */
	public void changeShip(Ship oldShip, Ship newShip)
	{
		player_ships[findShip(oldShip)] = newShip;
	}
	
	/**
	 * It finds the position of a ship in player_ships array
	 * 
	 * @param s Ship to be found
	 * @return the index in player_ships array, where the ship to be found is
	 */
	public int findShip(Ship s)
	{
		for(int i=0; i < player_ships.length; i++)
		{
			if(player_ships[i]==s)
				return i;
		}
		return -1;
	}
	
	/**
	 * It checks if all the ships have been sunk
	 * 
	 * @return true if all the cells of all the ships have been hit
	 */
	public boolean AreShipsSunk()
	{
		for(int i=0; i<player_ships.length; i++)
		{
			if(!player_ships[i].isSunk())
				return false;
		}
		return true;
	}
	
	/**
	 * It sets a random position for all the ships
	 */
	public void setRandomShipsPosition()
	{
		int size = 0;
		player_ships = new Ship[10];
		for(int i = 0; i < player_ships.length; i++)
		{
			switch(i)
			{
				case 0:
					size = 4; // aircraft (portaerei)
					break;
				case 1,2:
					size = 3; // armored ships (corazzate)
					break;
				case 3,4,5:
					size = 2; // submarine (sottomarini)
					break;
				case 6,7,8,9: // simple ships (navi semplici)
					size = 1;
					break;
			}
			
			Ship newShip = new Ship(getRandomCor(), size, getRandomDir());
			
			while(!isShipAvailable(newShip))
				newShip = new Ship(getRandomCor(), size, getRandomDir());
			
			player_ships[i] = newShip;
		}
	}
	
	/**
	 * it checks if a ship can be placed
	 * 
	 * @param s ship to check
	 * @return true  if a ship is on the cells of another ship or has ships nearby
	 */
	public boolean isShipAvailable(Ship s)
	{
		Cell iter = s.getHead();
		while(iter != null)
		{
			if(isShipNear(s, s) || isShip(iter.getCor()))
				return false;
			iter = iter.next;
		}
		return true;
	}

	/**
	 * It returns all the ships of a player
	 * 
	 * @return player_ships array
	 */
	public Ship[] getplayer_ships() {
		return player_ships;
	}
	
	/**
	 * It returns a random coordinate
	 * 
	 * @return a random coordinate (letter;number) [letter in range('A','J') and number in range(1,10)]
	 */
	public String getRandomCor()
	{
		char lettera_random = (char)(random.nextInt('J'-'A'+1)+'A'); 
		int numero_random = random.nextInt(10)+1; 
		
		return lettera_random+";"+numero_random;
	}
	
	/**
	 * It returns a random direction
	 * 
	 * @return random direction [horizontal or vertical]
	 */
	public Ship.Direction getRandomDir()
	{
		if(random.nextBoolean())
			return Ship.Direction.HORIZONTAL;
		return Ship.Direction.VERTICAL;
	}
}
