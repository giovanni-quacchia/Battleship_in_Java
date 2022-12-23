package view;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;

/**
 * Menu panel
 */
public class Menu extends JPanel{
	private static final long serialVersionUID = 1L;
	/**
	 * title panel
	 */
	private JPanel title;
	/**
	 * options panel
	 */
	private JPanel options;
	/**
	 * play button
	 */
	private JButton play;
	/**
	 * how play button
	 */
	private JButton howPlay;
	/**
	 * quit button
	 */
	private JButton quit;
	/**
	 * Panel width
	 */
	private final int preferred_width = 500;
	/**
	 * panel height
	 */
	private final int preferred_height = 700;

	/**
	 * Menu panel
	 */
	public Menu() 
	{
		setPreferredSize(new Dimension(preferred_width,preferred_height));
		setBackground(Color.decode("#004A60"));
		setLayout(new BorderLayout(0, 0));
		
		title = new JPanel();
		title.setBackground(Color.decode("#004A60"));
		add(title, BorderLayout.NORTH);
		title.setLayout(new BorderLayout(0, 0));
		
		JLabel icon = new JLabel("");
		icon.setHorizontalAlignment(SwingConstants.CENTER);
		icon.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\res\\img\\ship.png"));
		title.add(icon);
		
		JLabel l_title = new JLabel("BATTLESHIP");
		l_title.setVerticalAlignment(SwingConstants.BOTTOM);
		l_title.setFont(new Font("Roboto", Font.BOLD, 70));
		l_title.setForeground(Color.decode("#526F51"));
		l_title.setHorizontalAlignment(SwingConstants.CENTER);
		title.add(l_title, BorderLayout.NORTH);
		
		options = new JPanel();
		options.setBackground(Color.decode("#004A60"));
		add(options, BorderLayout.CENTER);
		options.setLayout(new GridLayout(3, 1, 20, 50));
		
		play = new JButton("PLAY");
		setButton(play);
		options.add(play);
		
		howPlay = new JButton("HOW PLAY");
		setButton(howPlay);
		options.add(howPlay);
		
		quit = new JButton("QUIT");
		setButton(quit);
		options.add(quit);
		
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

//-----getter e setter---------------
	/**
	 * It returns play button
	 * @return play button
	 */
	public JButton getPlay() {
		return play;
	}
	/**
	 * It returns howPlay button
	 * @return howPlay button
	 */
	public JButton getHowPlay() {
		return howPlay;
	}
	/**
	 * It returns quit button
	 * @return quit button
	 */
	public JButton getQuit() {
		return quit;
	}
//-----------------------------------
	

}
