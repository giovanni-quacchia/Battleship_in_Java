package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.Font;
import java.awt.BorderLayout;

/**
 * Panel that explains how to play battleship
 */
public class HowPlay extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Panel width
	 */
	private final int preferred_width = 800;
	/**
	 * panel height
	 */
	private final int preferred_height = 500;

	/**
	 * Set up panel
	 */
	public HowPlay() 
	{
		setPreferredSize(new Dimension(preferred_width,preferred_height));
		setBackground(Color.decode("#004A60"));
		setLayout(new BorderLayout(0, 0));
		
		JLabel title = new JLabel("Rules");
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.BOLD, 30));
		add(title,BorderLayout.NORTH);
		
		JLabel instructions = new JLabel();
		instructions.setHorizontalAlignment(SwingConstants.CENTER);
		instructions.setFont(new Font("Roboto", Font.PLAIN, 15));
		instructions.setForeground(Color.WHITE);
		instructions.setText("<html>"+instructions()+"</html>");
		add(instructions, BorderLayout.CENTER);
		
		JLabel l_quit = new JLabel("ESC to exit");
		l_quit.setHorizontalAlignment(SwingConstants.LEFT);
		l_quit.setForeground(Color.WHITE);
		l_quit.setFont(new Font("Dialog", Font.BOLD, 20));
		add(l_quit, BorderLayout.SOUTH);
	}
	
	/**
	 * It returns instruction on how to play battleship written in howPlay.txt
	 * 
	 * @return String with instructions on how play battleship
	 */
	public String instructions()
	{
		File f = new File(System.getProperty("user.dir")+"\\res\\txt\\howPlay.txt");
		String instructions="";
		Scanner s;
		try {
			s = new Scanner(f);
			while(s.hasNextLine())
				instructions +=  s.nextLine() + "<br><br>";
			s.close();
		} catch (FileNotFoundException e) {
			new Alert(null, "File not found: " + System.getProperty("user.dir")+"\\res\\txt\\howPlay.txt", "error.png").showAlert();
		}
		
		return instructions;
	}

}
