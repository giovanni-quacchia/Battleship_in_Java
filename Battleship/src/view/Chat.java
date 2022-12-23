package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.GridLayout;

/**
 * Chat class for managing a chat panel to use during a match for showing to the users: new messages and events
 * It allow clients to send messages between them
 */
public class Chat extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Panel input (textField and send button)
	 */
	private JPanel input;
	/**
	 * TextField to write messages
	 */
	private JTextField textField;
	/**
	 * Button to send messages
	 */
	private JButton btnSendMsg;
	/**
	 * ScrollPane for showing all messages to the users
	 */
	private JScrollPane scrollPane;
	/**
	 * Panel used to show messages
	 */
	private JPanel chatArea;
	/**
	 * Enum that defines message type to add to the chat
	 */
	public enum Chat_message_type
	{
		/**
		 * Player chat message
		 */
		PLAYER,
		/**
		 * Opponent chat message
		 */
		OPPONENT,
		/**
		 * Event chat message
		 */
		EVENT
	}

	/**
	 * It sets up Chat panel
	 */
	public Chat() 
	{
		setLayout(new BorderLayout(0, 0));
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.black,2));
		// input panel (textfield and button)
		input = new JPanel();
		input.setBackground(Color.white);
		add(input, BorderLayout.SOUTH);
		GridBagLayout gbl_input = new GridBagLayout();
		gbl_input.columnWidths = new int[]{0, 0};
		gbl_input.rowHeights = new int[]{0, 0};
		gbl_input.columnWeights = new double[]{1.0, 0.0};
		gbl_input.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		input.setLayout(gbl_input);
		// textfield
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 0;
		input.add(textField, gbc_textField);
		textField.setColumns(10);
		textField.setEnabled(false);
		//button
		btnSendMsg = new JButton();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 1;
		gbc.gridy = 0;
		input.add(btnSendMsg, gbc);

		// ChatArea with scrollPane
		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.white);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.white));
		add(scrollPane, BorderLayout.CENTER);
		// ChatArea
		chatArea = new JPanel();
		chatArea.setBackground(Color.white);
		scrollPane.setViewportView(chatArea);
		chatArea.setLayout(new GridLayout(0, 1, 0, 0));
		setButton(btnSendMsg);
	}

	/**
	 * It adds style to buttons
	 * 
	 * @param btn button to customize
	 */
	public void setButton(JButton btn)
	{
		btn.setBackground(Color.white);
		btn.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\res\\img\\send.png"));
		btn.setFocusable(false);
	}

	/**
	 * It adds a new message to the chat
	 * 
	 * @param chat_message message to add
	 * @param type Type of the message to add (opponent or event)
	 */
	public void addMsg(String chat_message, Chat_message_type type)
	{
		JPanel msg = new JPanel();
		msg.setBackground(Color.white);

		// msg
		JLabel l = new JLabel("<html>"+chat_message+"</html>");
		l.setFont(new Font("Roboto", Font.BOLD, 15));

		switch(type)
		{
			case PLAYER:
				msg.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
				// icon
				msg.add(new JLabel(new ImageIcon(System.getProperty("user.dir")+"\\res\\img\\player.png")));
				break;
			case OPPONENT:
				msg.setLayout(new FlowLayout(FlowLayout.RIGHT,10,10));
				// icon
				msg.add(new JLabel(new ImageIcon(System.getProperty("user.dir")+"\\res\\img\\avversario.png")));
				break;
			case EVENT:
				msg.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
				l.setForeground(Color.green);
				break;
		}

		msg.add(l);
		chatArea.add(msg);
		this.revalidate();
	}
	
	/**
	 * Remove all messages in the chat
	 */
	public void cleanChat()
	{
		chatArea = new JPanel();
		scrollPane.setViewportView(chatArea);
		chatArea.setLayout(new GridLayout(0, 1, 0, 0));
	}
	/**
	 * It returns button for sending messages
	 * @return btnSendMsg reference
	 */
	public JButton getBtnSendMsg()
	{
		return btnSendMsg;
	}
	/**
	 * It returns textfield where messages are sent
	 * @return textField reference
	 */
	public JTextField getTextField()
	{
		return textField;
	}

	// debug
	// public static void main(String[] args) {
	// 	JFrame f = new JFrame();
	// 	Chat c = new Chat();
	// 	f.add(c);
	// 	f.pack();
	// 	f.setVisible(true);
	// 	c.addMsg("this is a test", Chat_message_type.AVVERSARIO);
	// }
}
