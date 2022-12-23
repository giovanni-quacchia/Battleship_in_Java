package main;

import controller.Control;
import model.Manage;
import view.Frame;

/**
 * Main Class for creating 
 * - Manage (managing ships positions)
 * - Control (managing events with ActionListener for JButtons, KeyListener for keyboard, MouseListener for right click)
 * - Frame (Graphic User Interface)
 */
public class Main 
{
	/**
	 * Constructor that executes the program and provides a gui to the user
	 * 
	 * @param args args
	 */
	public static void main(String[] args) 
	{
		try
		{
			Manage g = new Manage();
			new Control(new Frame(g),g);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
