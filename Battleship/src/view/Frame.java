package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Manage;

import java.awt.Color;

/**
 * Main frame for managing all other panels
 */
public class Frame extends JFrame{
	private static final long serialVersionUID = 1L;
	/**
	 * ContentPane
	 */
	private JPanel contentPane;
	/**
	 * Menu panel
	 */
	private Menu menu;
	/**
	 * Dashboard panel
	 */
	private Dashboard dashboard;
	/**
	 * HowPlay panel
	 */
	private HowPlay howPlay;
	/**
	 * Login panel
	 */
	private Login_panel loging_panel;
	/**
	 * UsersOnline panel
	 */
	private UsersOnline usersOnline;
	
	/**
	 * It sets up the frame
	 * 
	 * @param g Manage reference
	 */
	public Frame(Manage g)
	{
		// JFrame
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("Battleship");
		// contentPane
		contentPane = new JPanel(new BorderLayout(50, 50));
		contentPane.setBackground(Color.decode("#004A60"));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		// JPanels
		menu = new Menu();
		dashboard = new Dashboard(g);
		howPlay = new HowPlay();
		loging_panel = new Login_panel();
		usersOnline = new UsersOnline();
		// Menu is default panel
		add(menu);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
//----------getter e setter---------------------------
	/**
	 * It returns dashboard panel
	 * @return dashboard
	 */
	public Dashboard getDashboard(){return dashboard;}
	/**
	 * It returns menu panel
	 * @return menu
	 */
	public Menu getMenu(){return menu;}
	/**
	 * It returns login_panel panel
	 * @return login_panel
	 */
	public Login_panel getLoginPanel() {return loging_panel;}
	/**
	 * It returns howPlay panel
	 * @return howPlay
	 */
	public HowPlay getHowPlay(){return howPlay;}
	/**
	 * It returns usersOnline panel
	 * @return usersOnline
	 */
	public UsersOnline getUsersOnline(){return usersOnline;}
//----------------------------------------------------	
	/**
	 * It changes panel in the frame
	 * 
	 * @param toAdd panel to add
	 * @param toRemove panel to remove
	 */
	public void changePanel(JPanel toAdd, JPanel toRemove)
	{
		remove(toRemove);
		add(toAdd);
		pack();
		repaint();
	}
	/**
	 * It resets dashboard for starting a new match
	 */
	public void resetDashboard()
	{
		// clean the grids
		dashboard.resetGame();
		
		dashboard.disableGrid(dashboard.opponent_coords);
		dashboard.enableGrid(dashboard.player_coords);
		dashboard.chat.cleanChat();
	}

}
