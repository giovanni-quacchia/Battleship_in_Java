package server_client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import server_client.Message.Msg_type;

/**
 * Class used by the server for creating threads when a client connects and managing the communication with it
 */
public class ServerThread extends Thread
{
	/**
	 * socket created after a ServerSocket.accept() to communicate with client
	 */
	protected Socket connection;
	/**
	 * value used for writing gameplay operations on server CLI
	 */
	protected boolean print_operations;
	/**
	 * object used for reading objects sent by the client
	 */
	protected ObjectInputStream input;
	/**
	 * object used for writing objects to the client
	 */
	protected ObjectOutputStream output;
	/**
	 * client's nickname
	 */
	private String nickname;
	/**
	 * message used to establish the connection
	 */
	protected Message connection_request;
	/**
	 * object used for sending a Message to an other client
	 */
	protected Message toSend = null;
	
	/**
	 * constructor used for managing communication with a client
	 * 
	 * @param request socket created after a ServerSocket.accept() to communicate with client
	 */
	public ServerThread(Socket request)
	{
		connection=request;
		
		try 
		{
			input = new ObjectInputStream(connection.getInputStream());
			output = new ObjectOutputStream(connection.getOutputStream());

			connection_request = (Message)input.readObject();
			nickname = ((String)connection_request.object).split(";")[0];
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		this.start();
	}
	
	/**
	 * It manages messages received from client. It saves operations in a log file and redirects messages (with toSend != null)
	 */
	public void run()
	{
		Message msg = new Message();
		
		try 
		{
			while(msg.msg_type != Msg_type.CLOSE)
			{
				Object o = input.readObject();
				if(o instanceof Message)
					msg = (Message)o;
				else 
					break;

				if(!msg.getReceiver().isEmpty())
					toSend = msg;

				saveOperation(msg);
				
				// if client wants to play with another user, put him on a waiting state for a reply.
				if(msg.msg_type == Msg_type.ASKING_FOR_PLAYING)
				{
					Message t = new Message("Server",nickname);
					t.msg_type = Msg_type.WAIT_OPPONENT_RESPONSE;
					t.object = Server.connection_timer;
					sendMsg(t);
				}

			}
			connection.close();
		} 
		catch(IOException | ClassNotFoundException e)
		{
			// Server closes connection and communicates it to client. input.readObject() raises an exception because connection has been closed.
			try 
			{
				connection.close();
			} 
			catch (Exception exc) 
			{
				exc.printStackTrace();
			}
		}
		
		// communicate to opponent the disconnection (In this case, msg.msg_type is already Msg_type.CLOSE, because serverthread exited from the while)
		if(!msg.getReceiver().isEmpty())
		{
			toSend = msg;
		}
		
	}

	/**
	 * It saves operations and messages sent from a client to another during a match in a file called logs.txt
	 * 
	 * @param text text of the operation to save
	 */
	public void writeOperations(String text)
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		File f = new File(System.getProperty("user.dir")+"\\res\\txt\\logs.txt");
		try 
		{
			Scanner file = new Scanner(f);
			FileWriter fw = new FileWriter(f.getPath(),true);
			fw.write("   " + dtf.format(LocalDateTime.now()) + " - " + text+"\n");
			fw.close();
			file.close();
		} 
		catch (IOException  e) 
		{
			System.err.println("File not found: " + System.getProperty("user.dir")+"\\res\\txt\\users.txt");
		}
	}
	
	/**
	 * It returns information about a match
	 * 
	 * @param user player's nickname
	 * @param opponent opponent's nickname
	 * @return a string that defines a unique match in the form of "[client vs client] - "
	 */
	public String printMatchInformations(String user, String opponent)
	{
		// print match informations [client_nickname vs client_nickname] (nicknames in ascenging order)
		if(user.compareTo(opponent)>0)
			return "[" + user+ " vs " + opponent + "] - ";
		else
			return "[" + opponent + " vs " + user + "] - ";
	}
	
	/**
	 * It analyzes every message sent from the user and saves them depending on msg_type
	 * 
	 * @param msg message to save
	 */
	public void saveOperation(Message msg)
	{
		if(msg.object != null && msg.object.equals("user connect"))
		{
			writeOperations(msg.getSender() + " has successfully connected");
			return;
		}

		String match_info = printMatchInformations(msg.getSender(), msg.getReceiver());
		// print event, chat_msg, cell hit or final result
		if(msg.msg_type != null)
		{
			switch(msg.msg_type)
			{
				case CHAT_EVENT:
					writeOperations(match_info + msg.object);
					break;
				case CHAT_MSG:
					writeOperations(match_info + msg.getSender() + ": " + msg.object);
					break;
				case HIT_CELL:
					writeOperations(match_info + msg.getSender() + " hit " + msg.object);
					break;
				case ATTACK_RESULT:
					if(msg.object.equals("allSunk"))
					{
						
						writeOperations(match_info + msg.getReceiver() + " won");
						writeOperations(match_info + msg.getSender() + " lost");
					}
					break;
				case CLOSE:
					writeOperations(msg.getSender() + " disconnected");
					break;
				case ASKING_FOR_PLAYING:
					writeOperations(msg.getSender() + " asked " + msg.getReceiver() + " to play");
					break;
				case YES_PLAYING:
					writeOperations(msg.getSender() + " started a match with " + msg.getReceiver());
					break;
				case NO_PLAYING:
					writeOperations(msg.getSender() + " doesn't want to play with " + msg.getReceiver());
					break;
				case ALREADY_IN_A_GAME:
					writeOperations(msg.getSender() + " was already in a match");
					break;
				default:
					break;
			}
		}
		
	}
	
	/**
	 * Send a message to the client
	 * 
	 * @param msg message to send
	 */
	public void sendMsg(Message msg)
	{
		try {
			output.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * It returns user's nickname
	 * 
	 * @return nickname
	 */
	public String getNickname() 
	{
		return nickname;
	}
}
