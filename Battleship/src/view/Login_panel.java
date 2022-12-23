package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 * Login panel for managing login and sign up
 */
public class Login_panel extends JPanel {

	private static final long serialVersionUID = 1L;
	/**
	 * Textfield to write nickname
	 */
	private JTextField t_nickname;
	/**
	 * Textfield to write password
	 */
	private JTextField t_password;
	/**
	 * Textfield to write server ip
	 */
	private JTextField t_server_ip;
	/**
	 * button to register
	 */
	private JButton btn_reg;
	/**
	 * button to login
	 */
	private JButton btn_login;
	/**
	 * button to exit and return to menu
	 */
	private JButton btn_exit;
	/**
	 * Panel width
	 */
	private final int preferred_width = 800;
	/**
	 * panel height
	 */
	private final int preferred_height = 600;
	
	/**
	 * Set up login panel
	 */
	public Login_panel() 
	{
		//JPanel
		setPreferredSize(new Dimension(preferred_width,preferred_height));
		setBackground(Color.decode("#004a60"));
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		// header
		JLabel l_icon = new JLabel();
		l_icon.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\res\\img\\ship.png"));
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(l_icon, gbc);
		
		// form

		// input Server ip
		JPanel input_server_ip = new JPanel(new FlowLayout());
		input_server_ip.setBackground(Color.decode("#004a60"));
		
		JLabel l = new JLabel("Server IP: ");
		l.setForeground(Color.white);
		l.setFont(new Font("Roboto", Font.BOLD, 20));
		input_server_ip.add(l);
		
		t_server_ip = new JTextField();
		t_server_ip.setHorizontalAlignment(JTextField.CENTER);
		t_server_ip.setPreferredSize(new Dimension(200,20));
		t_server_ip.setBorder(BorderFactory.createLineBorder(Color.white));
		input_server_ip.add(t_server_ip);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(input_server_ip, gbc);
		
		// input_nickname
		JPanel input_nickname = new JPanel(new FlowLayout());
		input_nickname.setBackground(Color.decode("#004a60"));
		
		l = new JLabel("Nickname: ");
		l.setForeground(Color.white);
		l.setFont(new Font("Roboto", Font.BOLD, 20));
		input_nickname.add(l);
		
		t_nickname = new JTextField();
		t_nickname.setHorizontalAlignment(JTextField.CENTER);
		t_nickname.setPreferredSize(new Dimension(200,20));
		t_nickname.setBorder(BorderFactory.createLineBorder(Color.white));
		input_nickname.add(t_nickname);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(input_nickname, gbc);
		
		// input_password
		JPanel input_password = new JPanel(new FlowLayout());
		input_password.setBackground(Color.decode("#004a60"));
		
		l = new JLabel("Password: ");
		l.setForeground(Color.white);
		l.setFont(new Font("Roboto", Font.BOLD, 20));
		input_password.add(l);
		
		t_password = new JTextField();
		t_password.setHorizontalAlignment(JTextField.CENTER);
		t_password.setPreferredSize(new Dimension(200,20));
		t_password.setBorder(BorderFactory.createLineBorder(Color.white));
		input_password.add(t_password);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(input_password, gbc);
		
		//buttons
		JPanel buttons = new JPanel(new FlowLayout());
		buttons.setBackground(Color.decode("#004A60"));
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(buttons,gbc);
		
		btn_login = new JButton("LOGIN");
		btn_login.setFocusable(false);
		btn_login.setPreferredSize(new Dimension(200,30));
		setButton(btn_login);
		buttons.add(btn_login,gbc);
		
		btn_reg = new JButton("SIGN UP");
		btn_reg.setFocusable(false);
		btn_reg.setPreferredSize(new Dimension(200,30));
		setButton(btn_reg);
		buttons.add(btn_reg);

		// exit button
		btn_exit = new JButton("EXIT");
		btn_exit.setFocusable(false);
		btn_exit.setPreferredSize(new Dimension(200,30));
		setButton(btn_exit);
		buttons.add(btn_exit);
	}
	
	/**
	 * Add style to button
	 * @param btn button to customize
	 */
	public void setButton(JButton btn)
	{
		btn.setForeground(Color.BLACK);
		btn.setFont(new Font("Roboto", Font.BOLD, 15));
		btn.setBackground(Color.decode("#526F51"));
		btn.setBorder(BorderFactory.createLineBorder(Color.black,3));
		btn.setFocusable(false);
	}
	/**
	 * It returns btn_login
	 * @return btn_login_reference
	 */
	public JButton getBtn_login(){
		return btn_login;
	}
	/**
	 * It returns btn_reg
	 * @return btn_reg reference
	 */
	public JButton getBtn_reg()
	{
		return btn_reg;
	}
	/**
	 * It returns t_nickname
	 * @return t_nickname reference
	 */
	public JTextField getT_nickname() {
		return t_nickname;
	}
	/**
	 * It returns t_password
	 * @return t_password reference
	 */
	public JTextField getT_password() {
		return t_password;
	}
	/**
	 * It returns t_server_ip
	 * @return t_server_ip reference
	 */
	public JTextField getT_ip()
	{
		return t_server_ip;
	}
	/**
	 * It returns btn_exit
	 * @return btn_exit reference
	 */
	public JButton getBtn_exit() {
		return btn_exit;
	}

	// // for debug
	// public static void main(String[] args) {
	// 	JFrame f = new JFrame();
	// 	f.add(new Login_panel());
	// 	f.pack();
	// 	f.setVisible(true);
	// }
}
