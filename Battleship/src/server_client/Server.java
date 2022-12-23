package server_client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;

import server_client.Message.Msg_type;

/**
 * Server class for managing clients connections, sending and receiving messages between them.
 */
public class Server extends Thread
{
	/**
	 * Server socket for accepting connections
	 */
	private ServerSocket server = null;
	/**
	 * Arraylist with all clients' Sockets
	 */
	private ArrayList<ServerThread>clients;
	/**
	 * Scanner for reading input from CLI
	 */
	private Scanner keyboard = new Scanner(System.in);
	/**
	 * Scanner for reading a file
	 */
	private Scanner file;
	/**
	 * It allows users to connect to the server with ServerSocket.accept()
	 */
	private Thread listener;
	/**
	 * It checks if a client quits the server and 
	 * if there are clients who want to communicate with other clients
	 */
	private Thread users_manager;
	/**
	 * timer set when a client is waiting for a YES_NO_ANSWER, when he sent an ASKING_FOR_PLAYING request to another client
	 */
	protected static int connection_timer = 30000; // 30 seconds

	/**
	 * Constructor (It sets an array of Client's Socket and start up the server).
	 */
	public Server() 
	{
		printFile("server.txt");
		clients = new ArrayList<ServerThread>();
		menu();
	}
	
	/**
	 * It prints the content of a file, if it is found
	 * 
	 * @param file_name name of the file to print
	 */
	public void printFile(String file_name)
	{
		File f = new File(System.getProperty("user.dir")+"\\res\\txt\\" + file_name);
		try 
		{
			file = new Scanner(f);
			while(file.hasNextLine())
				System.out.println(file.nextLine());
			file.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println("File not found: " + System.getProperty("user.dir")+"\\res\\txt\\" + file_name);
		}
		
	}
	
	/**
	 * It prints menu and allows the user to interact with the server (connect, disconnect, show users connected...)
	 */
	public void menu()
	{
		keyboard.nextLine();

		while(true)
		{

			// print menu
			printFile("server_menu.txt");
						
			System.out.print("    choice: ");

			switch(keyboard.nextLine())
			{
				case "0":
					// exit
					disconnect();
					keyboard.close();
					System.exit(1);
					break;
				case "1":
					// connect
					connect();
					break;
				case "2":
					// print server status
					if(server == null || server.isClosed())
						System.out.println("\n    Server offline");
					else
						System.out.println("\n    Server online - " + server.getInetAddress());
					break;
				case "3":
					// print clients connected
					showClients();
					break;
				case "4":
					// print log file
					System.out.println("\n\n   ---------------------------------USERS OPERATIONS---------------------------------\n\n");
					printFile("logs.txt");
					System.out.println("\n\n   -----------------------------------------------------------------------------------\n\n");
					break;
				case "5":
					// close server
					disconnect();
					break;
				case "6":
					// clean log file
					cleanLogFile();
					break;
				default:
					// error: not valid input
					System.err.println("\n    Enter a valid option");
					break;
			}	
		}
	}

	/**
	 * It allows several clients to connect to the server.
	 */
	public void run() 
	{
		// check clients communication and disconnection every 100 millis
		users_manager = new Thread(this){
			public void run()
			{
				while(true)
				{
					try {
						checkConnections();
						managingCommunications();
						Thread.sleep(100);
					} 
					catch (InterruptedException e) 
					{	
						// when server is offline --> stop checking if users are online
						closeConnections();
						break;
					}
				}
					
			}
		};
		users_manager.start();
		
		// accept connection requests
		try 
		{
			while(true)
			{
				// when a client tries to connect, the server create a thread for managing it
				ServerThread user = new ServerThread(server.accept());

				// check username and password
				String result = client_connect(user.connection_request);
				if(result.equals("ok"))
				{
					clients.add(user);
					// send welcome message to the just connected client
					user.output.writeObject(new Message("Welcome to Battleship!"));
					// send all nicknames to all clients, for updating the UsersOnline JPanel
					Message m = new Message("Server","all",getNicknameUsers(),Msg_type.JPANEL_USERS_UPDATE);
					broadcast(m);
					// save operation (user connected)
					user.saveOperation(new Message(user.getNickname(),"","user connect",null));
				}
				else
				{
					// if credentials are incorrect
					Message m = new Message(Msg_type.CONNECTION_ERROR);
					m.setObject(result);
					user.sendMsg(m);
				}
				
			}
		} 
		catch (IOException exc) 
		{
			// communicate to all clients --> connection closed
			closeConnections();
		}

	}
	
	/**
	 * It sets up the server and allows users to connect
	 */
	public void connect()
	{
		// if I am already connected
		if(server != null && !server.isClosed())
		{
			System.out.println("\n    Server already online");
			return;
		}
		
		try 
		{
			// connection
			server = new ServerSocket(8081,10,InetAddress.getLocalHost());
			System.out.println("\n    Server online");
			// allow clients to connect
			listener = new Thread(this);
			listener.start();
		} 
		catch (Exception e) 
		{
			// error in connection (server already connected in another process or port isn't available)
			System.err.println("\n    Error in connection - " + e.getMessage());
		}
	}
	/**
	 * It prints clients connected to the server
	 */
	public void showClients()
	{
		System.out.println("\n    Users connected: ");
		for(String client: getNicknameUsers())
			System.out.println("    - " + client);
	}
	
	/**
	 * It disconnects all clients and close server connection
	 */
	public void disconnect()
	{
		// if I am already disconnected
		if(server == null || server.isClosed())
		{
			System.out.println("\n    Server already offline");
			return;
		}
		
		try 
		{
			// disconnect clients
			closeConnections();
			clients.clear();
			server.close();
			System.out.println("\n    Server offline");
		} 
		catch (Exception e) 
		{
			// error in connection
			System.err.println("\n    Error in disconnection - " + e.getMessage());
		}
	}
	
	/**
	 * disconnect clients 
	 */
	public void closeConnections()
	{
		for(ServerThread st : clients)
		{
			try 
			{
				// if the connection has not already closed by client
				if(!st.connection.isClosed())
				{
					Message m = new Message();
					m.msg_type = Msg_type.CLOSE;
					st.output.writeObject(m);
				}
					
			} 
			catch (Exception e) 
			{
				// client already closed connection
				System.err.println(st.getNickname() + " already disconnected");
			}
		}
	}

	/**
	 * check if there are connections closed and remove them from clients array
	 */
	public void checkConnections()
	{
		int size_before = clients.size();

		clients.removeIf(user -> (user.connection.isClosed()));
		
		// if some clients have been removed --> update UsersOnline panel
		if(size_before > clients.size())
		{
			Message b = new Message("Server","all",getNicknameUsers(),Msg_type.JPANEL_USERS_UPDATE);
			broadcast(b);
		}
			
	}

	/**
	 * It sends a message to all clients
	 * 
	 * @param msg message to send
	 */
	public void broadcast(Message msg) // object
	{
		for(ServerThread user : clients)
		{
			try 
			{
				user.output.writeObject(msg);
			} 
			catch (IOException e) 
			{
				// error in sending a message to a user
				System.err.println();
			}
			
		}
	}
	
	/**
	 * It returns all clients' nicknames
	 * 
	 * @return a String array with all clients' nicknames
	 */
	public String[] getNicknameUsers()
	{
		String[] nicknames = new String[clients.size()];
		
		for(int i = 0; i < nicknames.length; i++)
		{
			nicknames[i] = clients.get(i).getNickname();
		}
		
		return nicknames;
	}
	
	/**
	 * It manages users' requests to login or signup
	 * 
	 * @param request Message that contains credentials and request of the user
	 * @return "ok" if login or sign up was done correctly, "error" otherwise
	 */
	public String client_connect(Message request)
	{
		if(request.msg_type == Msg_type.LOGIN)
		{
			return checkLogin((String)request.object);
		}
		else if(request.msg_type == Msg_type.SIGN_UP)
		{
			return checkRegistration((String)request.object);
		}
		else
			return "error";
	}
	
	/**
	 * It redirects messages
	 */
	public void managingCommunications()
	{
		
		for(ServerThread st: clients)
		{
			// check if a client wants to communicate
			if(st.toSend != null && !st.toSend.getReceiver().isEmpty())
			{
				try 
				{
					// send the message from the Message.receiver to the Message.sender
					getSocket(st.toSend.getReceiver()).output.writeObject(st.toSend);
					st.toSend = null;
				} 
				catch(NullPointerException | IOException e)
				{
					// error raised when the receiver is offline --> communicate it to the sender and end the game
					Message tmp = new Message("Server","",st.toSend.getReceiver(),Msg_type.OPPONENT_OFFLINE);
					try {
						getSocket(st.toSend.getSender()).output.writeObject(tmp);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					st.toSend = null;
				}
			}
		}
	}
	
	//  
	/**
	 * It returns a socket for communication
	 * 
	 * @param nickname nickname of the client, for which I need the socket for managing the communication
	 * @return socket of the client I need to communicate with
	 */
	public ServerThread getSocket(String nickname)
	{
		for(ServerThread st: clients)
		{
			if(st.getNickname().equals(nickname))
				return st;
		}
		return null;
	}
	
	/**
	 * It checks if a nickname is available and save it in users.txt
	 * 
	 * @param user credentials to check (username;password)
	 * @return "ok" if the nickname is available, "This nickname is not available" otherwise (to communicate it to the user)
	 */
	public String checkRegistration(String user)
	{
		File f = new File(System.getProperty("user.dir")+"\\res\\txt\\users.txt");
		try 
		{
			file = new Scanner(f);
			while(file.hasNextLine())
			{
				// check if the nickname is not available (user is split in array("0"->nickname,"1"->password))
				if((user.split(";")[0]).equals(file.nextLine().split(";")[0]))
				{
					file.close();
					return "This nickname is not available";
				}
			}
			FileWriter fw = new FileWriter(f.getPath(),true);
			fw.write(user+"\n");
			fw.close();
			file.close();
			return "ok";
		} 
		catch (IOException  e) 
		{
			System.err.println("File not found: " + System.getProperty("user.dir")+"\\res\\txt\\users.txt");
			return "error";
		}
	}
	
	/**
	 * It checks if credentials are correct
	 * 
	 * @param user credentials to check (username;password)
	 * @return ok" if credentials are correct, "Wrong credentials" otherwise (to communicate it to the user)
	 */
	public String checkLogin(String user)
	{
		File f = new File(System.getProperty("user.dir")+"\\res\\txt\\users.txt");
		try 
		{
			file = new Scanner(f);
			while(file.hasNextLine())
			{
				if((user).equals(file.nextLine()))
				{
					file.close();
					return "ok";
				}
			}
			file.close();
			return "Wrong credentials";
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println("File not found: " + System.getProperty("user.dir")+"\\res\\txt\\users.txt");
			return "error";
		}
	}

	/**
	 * clean log file
	 */
	public void cleanLogFile()
	{
		File f = new File(System.getProperty("user.dir")+"\\res\\txt\\logs.txt");
		try 
		{
			Scanner file = new Scanner(f);
			FileWriter fw = new FileWriter(f.getPath());
			fw.write("");
			fw.close();
			file.close();
		} 
		catch (IOException  e) 
		{
			System.err.println("File not found: " + System.getProperty("user.dir")+"\\res\\txt\\users.txt");
		}
	}
	
	/**
	 * Run the program
	 * 
	 * @param args args
	 */
	public static void main(String[] args) {
		new Server();
	}
}
