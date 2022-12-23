package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 * UsersOnline panel
 */
public class UsersOnline extends JPanel {
	
	private static final long serialVersionUID = 1L;
	/**
	 * ScrollPane to show users online
	 */
	private JScrollPane scrollPane;
	/**
	 * List for listing users online
	 */
	private DefaultListModel<String> model = new DefaultListModel<>();
	/**
	 * list with users online
	 */
	private JList<String> list_users;
	/**
	 * Button play for sending a request to start a match with another user
	 */
	private JButton btn_play;
	/**
	 * Panel width
	 */
	private final int preferred_width = 500;
	/**
	 * panel height
	 */
	private final int preferred_height = 700;
	
	/**
	 * Set up UsersOnline panel
	 */
	public UsersOnline() 
	{
		//JPanel
		setPreferredSize(new Dimension(preferred_width,preferred_height));
		setBackground(Color.decode("#004a60"));
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		//icon
		JLabel l_icon = new JLabel();
		l_icon.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\res\\img\\ship.png"));
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(l_icon, gbc);
		
		// label: users connected
		JLabel l = new JLabel("Users connected");
		l.setFont(new Font("Roboto", Font.BOLD, 30));
		l.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(l, gbc);
		
		// users
		scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#004a60")));
		scrollPane.setBackground(Color.decode("#004a60"));
		scrollPane.setPreferredSize(new Dimension(300,200));
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(scrollPane, gbc);
		
		// button play
		btn_play = new JButton("PLAY");
		setButton(btn_play);
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(btn_play, gbc);

	}
	
	/**
	 * It lists all users online except me
	 * 
	 * @param clients Array with nicknames of all online users 
	 * @param nickname nickname of the player (removed from the list)
	 */
	public void updateUsers(String[] clients, String nickname)
	{
		list_users = new JList<>();
		list_users.setFont(new Font("Roboto", Font.BOLD, 20));
		model.removeAllElements();

		for(int i = 0; i < clients.length; i++)
		{
			if(clients[i] != null && !clients[i].equals(nickname))
				model.addElement(clients[i]);
		}
		list_users.setModel(model);
		
		scrollPane.setViewportView(list_users);

		// icon
		new JLabel(new ImageIcon(System.getProperty("user.dir")+"\\res\\img\\avversario.png"));

		this.revalidate();
	}

	/**
	 * Add style to button 
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
	 * It returns play button
	 * @return btn_play reference
	 */
	public JButton getBtnPlay()
	{
		return btn_play;
	}
	/**
	 * online users list
	 * @return list_users JList
	 */
	public JList<String> getList_users() {
		return list_users;
	}
}
