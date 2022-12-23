package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Cell;
import model.Manage;
import model.Ship;

/**
 * Main Game Panel for managing players' grids
 */
public class Dashboard extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Manage reference
	 */
	private Manage g;
	/**
	 * Header panel (icon and title)
	 */
	private JPanel header;
	/**
	 * Main panel (grids and chat)
	 */
	private JPanel p_game;
	/**
	 * Footer panel (buttons)
	 */
	private JPanel footer;
	/**
	 * Player coordinates, saved in a JButton matrix 
	 */
	protected JButton[][] player_coords = new JButton[10][10];
	/**
	 * Opponent coordinates, saved in a JButton matrix 
	 */
	protected JButton[][] opponent_coords = new JButton[10][10];
	/**
	 * Player grid panel
	 */
	protected JPanel player_grid;
	/**
	 * Opponent grid panel
	 */
	protected JPanel opponent_grid;
	/**
	 * Chat panel
	 */
	protected Chat chat;
	/**
	 * Start button for starting a match
	 */
	private JButton btn_start;
	/**
	 * random arrangement to place the ships randomly
	 */
	private JButton btn_random_positioning;
	/**
	 * Exit button for quit the game
	 */
	private JButton btn_quit;
	/**
	 * Panel width
	 */
	private final int preferred_width = 1600;
	/**
	 * panel height
	 */
	private final int preferred_height = 700;
	
	/**
	 * It creates the dashboard
	 * 
	 * @param g Management reference
	 */
	public Dashboard(Manage g) 
	{
		this.g=g;

		setPreferredSize(new Dimension(preferred_width,preferred_height));
		setBackground(Color.decode("#004A60"));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(50, 50));
		
		// header
		header = new JPanel();
		setHeader();
		add(header, BorderLayout.NORTH);
		
		//game
		p_game = new JPanel(new GridLayout(0, 3, 100, 0));
		setGame();
		add(p_game, BorderLayout.CENTER);
		
		// footer
		footer = new JPanel(new GridLayout(0, 3, 100, 0));
		setFooter();
		add(footer, BorderLayout.SOUTH);
		setShips();
	}
	
	/**
	 * Set up header panel
	 */
	public void setHeader()
	{
		header.setBackground(Color.decode("#004A60"));
		header.setForeground(new Color(0, 0, 0));
		JLabel title = new JLabel("BATTLESHIP");
		title.setFont(new Font("Roboto", Font.BOLD, 50));
		title.setForeground(Color.WHITE);
		header.add(title);
	}
	/**
	 * Set up game panel
	 */
	public void setGame()
	{
		p_game.setBackground(Color.decode("#004A60"));
		
		// griglia player
		player_grid = new JPanel(new GridLayout(11, 13, 0, 0));
		setGrid(player_grid, player_coords);
		p_game.add(player_grid);
		
		// chat
		chat = new Chat();
		p_game.add(chat);
		
		// griglia avversario
		opponent_grid = new JPanel(new GridLayout(11, 13, 0, 0));
		setGrid(opponent_grid, opponent_coords);
		disableGrid(opponent_coords);
		p_game.add(opponent_grid);
	}
	/**
	 * Reset grids for starting a new match
	 */
	public void resetGame()
	{
		// remove X text from PlayerGrid hit cells
		// remove X text and ships from OpponentGrid hit cells
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				player_coords[i][j].setText("");
				opponent_coords[i][j].setText("");
				// Put the coordinate taken from the player grid
				opponent_coords[i][j].setActionCommand(player_coords[i][j].getActionCommand());
				opponent_coords[i][j].setBackground(Color.decode("#F6F6F6"));
				opponent_coords[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#E4E7EA")));
			}
		}
		
		// enable my grid, so that I can manage ships position
		disableGrid(opponent_coords);
		enableGrid(player_coords);
	}

	/**
	 * Set up players' grids
	 * 
	 * @param grid Grid to set up (player or opponent)
	 * @param coordinate coordinate associated with the grid (player or opponent)
	 */
	public void setGrid(JPanel grid, JButton[][] coordinate)
	{
		grid.setBackground(Color.decode("#004A60"));
		for(int i = 0; i < 10; i++)
		{
			// first row with letters
			if(i == 0)
			{
				// Top corner to left empty
				grid.add(new JLabel(""));
				for(int j = (int)'A'; j < (int)'K'; j++)
				{
					JLabel letter = new JLabel(""+(char)j);
					letter.setFont(new Font("Roboto", Font.BOLD, 15));
					letter.setForeground(Color.WHITE);
					letter.setHorizontalAlignment(SwingConstants.CENTER);
					grid.add(letter);
				}
				// empty label at end of line
				grid.add(new JLabel(""));
			}
			for(int j = 0; j < 10; j++)
			{
				// number at the beginning of the row
				if(j == 0)
				{

					JLabel number = new JLabel(Integer.toString(i+1));
					number.setFont(new Font("Roboto", Font.BOLD, 15));
					number.setForeground(Color.WHITE);
					number.setHorizontalAlignment(SwingConstants.CENTER);
					grid.add(number);
				}
				coordinate[i][j] = new JButton();
				coordinate[i][j].setFocusable(false);
				// give the coordinates to the button
				coordinate[i][j].setActionCommand((char)(j+(int)'A')+";"+Integer.toString(i+1));
				coordinate[i][j].setBackground(Color.white);
				coordinate[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#E4E7EA")));
				
				grid.add(coordinate[i][j]);
			}
			// empty label at the end of each row
			grid.add(new JLabel(""));
		}
	}
	
	/**
	 * Set up footer panel with buttons
	 */
	public void setFooter()
	{
		footer.setBackground(Color.decode("#004A60"));
		
		// buttons
		btn_start = new JButton("START");
		btn_random_positioning = new JButton("RANDOM POSITIONING");
		btn_quit = new JButton("QUIT");
		setButton(btn_start);
		setButton(btn_random_positioning);
		setButton(btn_quit);
		footer.add(btn_start);	
		footer.add(btn_random_positioning);
		footer.add(btn_quit);
	}
	
	/**
	 * Add style to button
	 * 
	 * @param btn button to customize
	 */
	public void setButton(JButton btn)
	{
		btn.setForeground(Color.BLACK);
		btn.setFont(new Font("Roboto", Font.BOLD, 20));
		btn.setBackground(Color.decode("#526F51"));
		btn.setBorder(BorderFactory.createLineBorder(Color.black,3));
		btn.setFocusable(false);
	}
	/**
	 * It disables buttons in a grid
	 * 
	 * @param coordinate buttons associated with the grid to disable
	 */
	public void disableGrid(JButton[][] coordinate)
	{
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				coordinate[i][j].setEnabled(false);
				coordinate[i][j].setBackground(Color.decode("#F6F6F6"));
				
				// player's ships are still visible
				if(g.isShip(player_coords[i][j].getActionCommand()))
					player_coords[i][j].setBackground(Color.decode("#5D8FC2"));
				// opponent's ships hit are still visible
				if(opponent_coords[i][j].getActionCommand().equals("ship"))
				{
					opponent_coords[i][j].setBackground(Color.decode("#FC3259"));
					opponent_coords[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#FC3259")));
				}
					
			}
		}
	}
	
	/**
	 * It enables buttons in a grid
	 * 
	 * @param coordinate buttons associated with the grid to enable
	 */
	public void enableGrid(JButton[][] coordinate)
	{
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				coordinate[i][j].setEnabled(true);
				coordinate[i][j].setBackground(Color.decode("#FFFFFF"));
				
				// player's ships are still visible
				if(g.isShip(player_coords[i][j].getActionCommand()))
					player_coords[i][j].setBackground(Color.decode("#5D8FC2"));
				// opponent's ships hit are still visible
				if(opponent_coords[i][j].getActionCommand().equals("ship"))
				{
					opponent_coords[i][j].setBackground(Color.decode("#FC3259"));
					opponent_coords[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#FC3259")));
				}
				// Opponent hit cell stay disabled
				if(opponent_coords[i][j].getActionCommand().equals("ship") || opponent_coords[i][j].getText().equals("X"))
				{
					opponent_coords[i][j].setEnabled(false);
				}
					
			}
		}
	}

	
	/**
	 * It highlights the cell on which I am with the mouse
	 * 
	 * @param coordinate buttons associated with the grid, on which is the cell to be highlighted
	 * @param i first index of the matrix where grid's buttons are saved
	 * @param j	second index of the matrix where grid's buttons are saved
	 * @param toMove reference of the ship that needs to be moved (selection changed when I want to move a ship on the grid)
	 */
	public void hover_cell(JButton[][] coordinate,int i, int j, Ship toMove)
	{
		// if I’m on a ship select it all
		if(toMove == null)
		{
			if(g.isShip(coordinate[i][j].getActionCommand()) && player_coords[i][j].isEnabled())
			{
				Ship toSelect = g.getShip(coordinate[i][j].getActionCommand());
				selectShip(toSelect,Color.blue);	
			}
			else
				coordinate[i][j].setBorder(BorderFactory.createLineBorder(Color.blue,2));	
		}
		// if I am moving a ship, select the cells that are not ship
		else if(toMove != g.getShip(coordinate[i][j].getActionCommand()))
		{
			// if I’m on a ship select it all
			if(g.isShip(coordinate[i][j].getActionCommand()))
			{
				Ship toSelect = g.getShip(coordinate[i][j].getActionCommand());
				selectShip(toSelect,Color.red);	
			}
			// show the user where the new ship might be put
			else
			{
				Ship toSelect = new Ship(coordinate[i][j].getActionCommand(), toMove.getSize(), toMove.getDirection());
				selectShip(toSelect,Color.blue);	
			}
				
		}
			
	}
	
	/**
	 * It deselect (remove highlighting of) the cell when I move to another cell
	 * 
	 * @param coordinate buttons associated with the grid
	 * @param i first index of the matrix where grid's buttons are saved
	 * @param j	second index of the matrix where grid's buttons are saved
	 * @param toMove reference of the ship that needs to be moved (deselection changed when I want to move a ship on the grid)
	 */
	public void not_Hover_cell(JButton[][] coordinate,int i, int j, Ship toMove)
	{
		// If I'm not moving a ship, deselect each cell
		if(toMove == null)
		{
			// if it is a ship, keep the blue border
			if(g.isShip(coordinate[i][j].getActionCommand()) && player_coords[i][j].isEnabled())
				player_coords[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#5D8FC2")));
			// if it is an opponent ship, keep the red border
			else if(coordinate[i][j].getActionCommand().equals("ship"))
				opponent_coords[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#FC3259")));
			else
				coordinate[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#E4E7EA")));
		}
		// If I'm moving a ship
		else if(toMove != g.getShip(coordinate[i][j].getActionCommand()))
		{
			// deselect ships cells
			if(g.isShip(coordinate[i][j].getActionCommand()) && player_coords[i][j].isEnabled())
				player_coords[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#5D8FC2")));
			else
			{
				Ship toDeselect = new Ship(coordinate[i][j].getActionCommand(), toMove.getSize(), toMove.getDirection());
				deselectShip(toDeselect,Color.decode("#E4E7EA"), toMove);
			}
				
		}
			
	}
	
	/**
	 * It places the ships on the grid of the player, based on the ships created in the management class
	 */
	public void setShips()
	{
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				// given a coordinate, check if there’s a ship
				if(g.isShip((char)(j+(int)'A')+";"+Integer.toString(i+1)))
				{
					player_coords[i][j].setBackground(Color.decode("#5D8FC2"));
					player_coords[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#5D8FC2")));
				}
				// otherwise the cell stays empty
				else
				{
					player_coords[i][j].setBackground(Color.WHITE);
					player_coords[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#E4E7EA")));
				}
					
			}
		}
	}
	
	/**
	 * Highlight a ship
	 * @param s ship to select 
	 * @param color color to use for selecting the ship
	 */
	public void selectShip(Ship s, Color color)
	{
		
		// scroll through the grid
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				// Check if the coordinate belongs to the ship s
				if(s.isShip((char)(j+(int)'A')+";"+Integer.toString(i+1)))
				{
					// if the ship has size one, select the entire cell
					if(s.getSize() == 1)
					player_coords[i][j].setBorder(BorderFactory.createLineBorder(color,2));
					else
					{
						// head
						if(s.isHead(player_coords[i][j].getActionCommand()))
						{
							// if the ship is horizontal or vertical, borders change
							if((s.getDirection() == Ship.Direction.VERTICAL && s.isTurned()==false) || (s.getDirection() == Ship.Direction.HORIZONTAL && s.isTurned()==true))
								player_coords[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 0, 2, color));
							else
								player_coords[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 0, color));
						}
						// tail
						else if(s.isLast(player_coords[i][j].getActionCommand()))
						{
							if((s.getDirection() == Ship.Direction.VERTICAL && s.isTurned()==false) || (s.getDirection() == Ship.Direction.HORIZONTAL && s.isTurned()==true))
								player_coords[i][j].setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, color));
							else
								player_coords[i][j].setBorder(BorderFactory.createMatteBorder(2, 0, 2, 2, color));
						}
						// rest of the ship
						else
						{
							if((s.getDirection() == Ship.Direction.VERTICAL && s.isTurned()==false) || (s.getDirection() == Ship.Direction.HORIZONTAL && s.isTurned()==true))
								player_coords[i][j].setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2, color));
							else
								player_coords[i][j].setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, color));
						}
					}
						
				}
			}
		}
	}
	
	/**
	 * Deselect a ship
	 * 
	 * @param s ship to deselect
	 * @param color color to use 
	 * @param toMove possible ship to move
	 */
	public void deselectShip(Ship s, Color color, Ship toMove)
	{
		// scroll through the grid
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				// Check if the coordinate belongs to the ship s
				if(s.isShip((char)(j+(int)'A')+";"+Integer.toString(i+1)))
				{
					// Deselect the cell, if I'm not moving a ship
					if(toMove == null)
					{
						player_coords[i][j].setBorder(BorderFactory.createLineBorder(color));
					}
					// If I'm not moving a ship
					else
					{
						// deselect cells that are not ship
						if(!g.isShip(player_coords[i][j].getActionCommand()))
							player_coords[i][j].setBorder(BorderFactory.createLineBorder(color));
						else
						{
							// if I don’t pass near the ship to move, restore default border
							if(!toMove.isShip(player_coords[i][j].getActionCommand()))
								player_coords[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#5d8fc2")));
							// If I pass near the ship to move, restore 'selected_ship' border
							else
							{
								selectShip(toMove, Color.green);
							}
								
						}
							
					}
						
				}
			}
		}
	}
	
	/**
	 * It checks if cells of a ship are outside the grid
	 * 
	 * @param s Ship to check
	 * @return true if the position is valid, false otherwise
	 */
	public boolean validateShip(Ship s)
	{
		Cell iter = s.getHead();
		// scroll through the ship for checking all cell's coordinates
		while(iter != null)
		{
			String[] coord = iter.getCor().split(";");
			
			// check the letter ('A' < LETTER < 'J')
			if(Integer.valueOf(coord[0].charAt(0)) < (int)'A' || Integer.valueOf(coord[0].charAt(0)) > (int)'J')
				return false;
			// check the number (1 < NUMBER < 10)
			if(Integer.valueOf(coord[1]) < 1 || Integer.valueOf(coord[1]) > 10)
				return false;
			
			iter = iter.next;
		}
		return true;
	}
	
	/**
	 * It adds an 'X' to the cells of the grid that the opponent hit.
	 * 
	 * @param coordinate coordinate of the cell to update
	 * @param type type of attack, for setting a new bg_color for ships' cells
	 */
	public void setHitCell(String coordinate, String type)
	{
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				// find hit cell
				if(opponent_coords[i][j].getActionCommand().equals(coordinate))
				{
					// if it is a ship
					if(type.equals("ship") || type.equals("sunk") || type.equals("allSunk"))
					{
						opponent_coords[i][j].setActionCommand("ship");
						opponent_coords[i][j].setBackground(Color.decode("#FC3259"));
						opponent_coords[i][j].setBorder(BorderFactory.createLineBorder(Color.decode("#FC3259")));
						// The hit ship, can't be clicked more than once 
						opponent_coords[i][j].setEnabled(false);
					}
					else
					{
						opponent_coords[i][j].setText("X");
					}
				}
			}
		}
	}
	
	/**
	 * Enable opponent grid for starting the match and chat for sending messages
	 */
	public void readyToStart()
	{
		disableGrid(player_coords);
		chat.getTextField().setEnabled(true);
	}
	/**
	 * It returns player_coords cells
	 * @return player_coords
	 */
	public JButton[][] getPlayerCoor() {
		return player_coords;
	}
	/**
	 * It returns opponent_coords cells
	 * @return opponent_coords
	 */
	public JButton[][] getOpponentCoor() {
		return opponent_coords;
	}
	/**
	 * It returns btn_random_positioning
	 * @return btn_random_positioning reference
	 */
	public JButton getBtn_random_positioning() {
		return btn_random_positioning;
	}
	/**
	 * It returns btn_start
	 * @return btn_start reference
	 */
	public JButton getBtn_start() {
		return btn_start;
	}
	/**
	 * It returns btn_quit
	 * @return btn_quit reference
	 */
	public JButton getBtn_quit() {
		return btn_quit;
	}
	/**
	 * It returns chat
	 * @return chat reference
	 */
	public Chat getChat()
	{
		return chat;
	}

	// for debug
	// public static void main(String[] args) throws Exception {
	// 	JFrame f = new JFrame();
	// 	f.add(new Dashboard(new Manage()));
	// 	f.pack();
	// 	f.setVisible(true);
	// }
}
