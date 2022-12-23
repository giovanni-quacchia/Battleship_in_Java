package server_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JOptionPane;

import controller.MatchState;
import controller.MatchState.Match_State;
import model.Manage;
import server_client.Message.Msg_type;
import view.Frame;
import view.Chat.Chat_message_type;
import view.Alert;

/**
 * Client class for managing communication with Server. 
 */
public class Client extends Thread
{
	/**
	 * Socket to connect with server
	 */
	private Socket connection;
	/**
	 * Object for receiving messages from server
	 */
	protected ObjectInputStream input;
	/**
	 * Object for sending messages to server
	 */
	protected ObjectOutputStream output;
	/**
	 * Player's nickname
	 */
	private String nickname="";
	/**
	 * Opponent's nickname
	 */
	private String opponent="";
	
	/**
	 * Default timer for socket, 15 min
	 */
	private int timer = 900000;
	/**
	 * Default port for connection
	 */
	private int port = 8081;
	/**
	 * Default host for connection
	 */
	private String host = "localhost";
	/**
	 * Reference of the Frame
	 */
	private Frame f;
	/**
	 * Reference of the Manage
	 */
	private Manage g;
	/**
	 * Player's state (to_start, my_turn...)
	 */
	private MatchState state = null;
	
	/**
	 * It creates client and connect it to the server, by providing username and password
	 * 
	 * @param nickname Username chosen by the user
	 * @param password User account password (to be validated)
	 * @param request  Login or sign up
	 * @param f		   Frame reference for showing alerts and managing other operations
	 * @param g		   Manage reference, for interacting with ships (e.g. checking if opponent hit a ship)
	 * @param state	   User's state (to_start, my_turn, opponent_turn...)
	 * @param host	   Server ip
	 */
	public Client(String nickname, String password, Msg_type request, Frame f, Manage g, MatchState state, String host) 
	{
		this.nickname = nickname;
		this.state = state;
		this.host=host;
		this.f = f;
		this.g = g;
		
		//connection
		connect(nickname,password,request);
	}
	
	/**
	 * Try to connect to server (host:8081). Connection is attempted five times.
	 * 
	 * @param nickname	user's nickname
	 * @param password  user's password
	 * @param request   login or sign up
	 */
	public void connect(String nickname, String password, Msg_type request)
	{
		int attempt = 0;

		while(true)
		{
			try
			{
				// connection
				connection = new Socket(host,port);
				connection.setSoTimeout(timer);
				break;
			}
			catch(IOException  e)
			{
				// If last attempt fails --> execution ends
				if(attempt >= 5)
				{
					f.changePanel(f.getMenu(), f.getLoginPanel());
					return;
				}

				attempt++;

				// Ask to the user if he wants to retry the connection
				if(JOptionPane.showConfirmDialog(f, "Server offline - attempt: " + attempt + "/5\n\nRetry?", "Errore", JOptionPane.ERROR_MESSAGE)==1)
				{
					f.changePanel(f.getMenu(), f.getLoginPanel());
					return;
				}
				_sleep(500);
			}
		}

		try
		{
			// initialized input and output
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());

			// send credentials
			output.writeObject(new Message(nickname, "Server", nickname+";"+password, request));
			// check message received
			Object o = input.readObject();
			Message msg = null;
			if(o instanceof Message)
				msg = (Message)o;
			else 
				throw new ClassNotFoundException("Message received is not correct");
			// Check if nickname is available
			if(msg.msg_type == Msg_type.CONNECTION_ERROR)
			{
				// ERROR
				new Alert(f, (String)msg.object,"error.png").showAlert();
				msg.msg_type = Msg_type.CLOSE;
			}
			else
			{
				// connection established
				new Alert(f, (String)msg.object,"battleship.png").showAlert();
				// change panel for showing other users online
				f.changePanel(f.getUsersOnline(), f.getLoginPanel());
				this.start();
			}
		}
		catch(IOException | ClassNotFoundException e)
		{
			// ERROR
			new Alert(f, (String)e.getMessage(),"error.png").showAlert();
		}
	}
	
	/**
	 * It manages messages received from the server.
	 */
	public void run()
	{
		Message received = new Message();
		
			while(received.msg_type != Msg_type.CLOSE) 
			{
				try
				{
					// check message received
					Object o = input.readObject();
					if(o instanceof Message)
						received = (Message)o;
					else 
						break;

					if(received.msg_type != null)
					{
						switch(received.msg_type)
						{
							// update users_online JPanel
							case JPANEL_USERS_UPDATE:
								f.getUsersOnline().updateUsers((String[])received.object, nickname);

								// check that the opponent is still online, otherwise close the match
								if(!opponent.isEmpty())
								{
									String[] users_connected = (String[])received.object;
									if(!Arrays.asList(users_connected).contains(opponent))
									{
										new Alert(f, "Opponent is offline - match ends","error.png").showAlert();
										quitGame();
									}
								}
								break;
							// Manage a request for playing from another client
							case ASKING_FOR_PLAYING:
								askForPlay(received.getSender());
								break;
							// update chat
							case CHAT_MSG, CHAT_EVENT:
								updateChat(received);
								break;
							// Wait for a response from the opponent, when I ask him for playing
							case WAIT_OPPONENT_RESPONSE:
								manage_opponent_response((int)received.object);
								break;
							// when the opponent hit a cell
							case HIT_CELL:
								manage_received_attack((String)received.object);
								break;
							// match ends if the opponent is offline
							case OPPONENT_OFFLINE:
								new Alert(f, "Opponent is offline - match ends","error.png").showAlert();
								quitGame();
								break;
							// when I receive a response on a previous HIT_CELL msg
							case ATTACK_RESULT:
								manageAttackResult((String)received.object, received.getHit_cell());
								break;
							default:break;
						}
					}

					// if match is over
					if(state.getState() == Match_State.END)
						quitGame();
				}	
				// If the timeout expires, a SocketTimeoutException is raised
				catch(SocketTimeoutException e)
				{
					// if I was waiting for a reply from another client--> the other client is busy
					if(state.getState() == Match_State.WAITING_FOR_START)
					{
						new Alert(f, "The other user is busy","error.png").showAlert();
						// set default timeout
						setSocketTimer(timer);
						// enable user to send other requests
						f.getUsersOnline().getBtnPlay().setEnabled(true);
						state.setState(null);
					}
					// if the timer expires --> automatically disconnect
					else
					{
						new Alert(f, "You have been offline for too long, Bye","error.png").showAlert();
						_sleep(3000);
						System.exit(1);
					}
				}
				catch(IOException | ClassNotFoundException e)
				{
					// Data received in unknown format
					new Alert(f, e.getMessage(),"error.png").showAlert();
					_sleep(3000);
					//System.exit(1);
				}
			}
			// Connection ends
			try 
			{
				output.writeObject(new Message(Msg_type.CLOSE));
				connection.close();
				System.exit(1);
			} 
			catch (IOException e) 
			{
				// Connection already closed by server
				new Alert(f,"Server offline","error.png").showAlert();
				_sleep(3000);
				System.exit(1);
			}
			
	}
	
	/**
	 * Return oponent nickname.
	 * @return opponent nickname
	 */
	public String getOpponent()
	{
		return opponent;
	}

	/**
	 * Send a message to the server
	 * @param msg object to send
	 */
	public void sendMessage(Object msg)
	{
		try {
			if(output != null)
				output.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check ships position when game restarts
	 * @return true if ships' position is valid, false otherwise
	 */
	public boolean validateShips()
	{
		for(int i=0;i<g.getplayer_ships().length;i++)
		{
			if(!f.getDashboard().validateShip(g.getplayer_ships()[i]))
				return false;
		}
		return true;
	}

	// 
	/**
	 * It manages response received from the opponent, when I sent him a request for starting a new match
	 * 
	 * @param tmp_timer timer to use while waiting for a reply
	 * @throws ClassNotFoundException if message received is not of type Message
	 * @throws IOException if input.readObject() fails
	 */
	public void manage_opponent_response(int tmp_timer) throws ClassNotFoundException, IOException
	{
		connection.setSoTimeout(tmp_timer); // 30 sec
		// check message received
		Object o = input.readObject();
		Message msg = null;
		if(o instanceof Message)
			msg = (Message)o;
		else 
			throw new ClassNotFoundException("Message received is not correct");

		switch(msg.msg_type)
		{
			// if I receive an object from another client that confirm that he wants to play
			case YES_PLAYING:
				new Alert(f, msg.getSender() + " is ready to play","battleship.png").showAlert();
				opponent = msg.getSender();
				f.changePanel(f.getDashboard(), f.getUsersOnline());
				state.setState(Match_State.TO_START);
				break;
			// if the other client doesn't want to play
			case NO_PLAYING:
				new Alert(f, msg.getSender() + " doesn't want to play with you", "error.png").showAlert();
				state.setState(null);
				break;
			// if I send a request to play to a client, but he is already in a game
			case ALREADY_IN_A_GAME:
				new Alert(f, msg.getSender() + " is already in a game", "error.png").showAlert();
				state.setState(null);
				break;
			default:break;
		}
		// set default timer
		connection.setSoTimeout(timer); // 15 min

		// enable user to send other requests
		f.getUsersOnline().getBtnPlay().setEnabled(true);
	}

	/**
	 * It checks what kind of cell the opponent hit
	 * 
	 * @param coordinate coordiante of the hit cell
	 * @return What kind of cell the opponent hit (water, ship...)
	 */
	public String checkHitCell(String coordinate)
	{
		String res = "";
		if(state.getState() == Match_State.OPPONENT_TURN)
		{			
			// check if the opponent hit a ship
			if(g.isShip(coordinate))
			{	
				g.getShip(coordinate).getCell(coordinate).setShot(true);			
				// check if the ship is sunk
				if(g.getShip(coordinate).isSunk())
				{
					res = "sunk";
					// check if all ships are sunk
					if(g.AreShipsSunk())
						res = "allSunk";
				}
				else
					res = "ship";
			}
			else
				res = "water";
		}
		return res;
	}

	/**
	 * It manages an attack received from the opponent. After checking what kind of cell he hit, Player's grid is updated
	 * 
	 * @param coordinate coordinate of the hit cell
	 */
	public void manage_received_attack(String coordinate)
	{
		// prepare message to send
		Message msg = new Message(nickname, opponent);
		msg.setHit_cell(coordinate);
		msg.msg_type = Msg_type.ATTACK_RESULT;

		// check what type of cell the opponent hit
		String result = checkHitCell(coordinate);
		
		switch(result)
		{
			case "ship":
				f.getDashboard().getChat().addMsg(opponent + " hit a ship", Chat_message_type.EVENT);
				break;
			case "sunk":
				f.getDashboard().getChat().addMsg(opponent + " sank a ship", Chat_message_type.EVENT);
				break;
			case "allSunk":
				state.setState(Match_State.END);
				new Alert(f, "YOU LOST", "battleship.png").showAlert();
				break;
			case "water":
				// change my state if opponent hit water
				state.setState(Match_State.MY_TURN);
				// enable grid
				f.getDashboard().enableGrid(f.getDashboard().getOpponentCoor());
				// update chat
				f.getDashboard().getChat().addMsg(opponent + " hit water", Chat_message_type.EVENT);
				break;
		}

		// Update interface with X on hit cell
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				// find hitten cell
				if(f.getDashboard().getPlayerCoor()[i][j].getActionCommand().equals(coordinate))
				{
					f.getDashboard().getPlayerCoor()[i][j].setText("X");
				}
			}
		}

		msg.setObject(result);
		sendMessage(msg);
	}

	/**
	 * It manages the result received after attacking the opponent
	 * 
	 * @param result It specifies what kind of cell the player hit 
	 * @param hitCell Coordinate of the hit cell
	 */
	public void manageAttackResult(String result, String hitCell)
	{
		// update chat
		switch(result)
		{
			case "ship":
				f.getDashboard().getChat().addMsg(nickname + " hit a ship", Chat_message_type.EVENT);
				break;
			case "sunk":
				f.getDashboard().getChat().addMsg(nickname + " sank a ship", Chat_message_type.EVENT);
				break;
			case "water":
				f.getDashboard().getChat().addMsg(nickname + " hit water", Chat_message_type.EVENT);
				// change my state if I hit water
				state.setState(Match_State.OPPONENT_TURN);
				// disable grid
				f.getDashboard().disableGrid(f.getDashboard().getOpponentCoor());
				break;
			case "allSunk":
				f.getDashboard().getChat().addMsg(nickname + " sank all ships", Chat_message_type.EVENT);
				state.setState(Match_State.END);
				new Alert(f, "YOU WON", "battleship.png").showAlert();
				break;
		}
		
		// update graphic interface
		f.getDashboard().setHitCell(hitCell, (String)result);	
	}

	/**
	 * Exit the game and randomize the position of the ships
	 */
	public void quitGame()
	{
		_sleep(2000);

		f.changePanel(f.getUsersOnline(), f.getDashboard());
		f.resetDashboard();
		g.setRandomShipsPosition();
		
		// validate position
		while(!validateShips())
		{
			g.setRandomShipsPosition();
		}
			
		f.getDashboard().setShips();

		// clear chat
		f.getDashboard().getChat().getTextField().setText("");

		state.setState(null);
		opponent = "";
	}

	/**
	 * It allows the player to reply to a request for starting a new match, sent from another user.
	 * 
	 * @param requester nickname of the user who wants to play with me
	 */
	public void askForPlay(String requester)
	{
		Message msg = new Message(nickname, requester);

		// if Im not already in a game
		if(state.getState() == null)
		{
			int choice = JOptionPane.showConfirmDialog(f, "Do yuo want to play with " + msg.getSender() + "?", "Battleship", JOptionPane.YES_NO_OPTION);
			
			// check if client timer has expired
			if((new Date().getTime() - msg.timestamp) > Server.connection_timer)
			{
				new Alert(f,"You have waited too long", "error.png").showAlert();
				setSocketTimer(timer);
			}
			else
			{
				if(choice == JOptionPane.YES_OPTION)
				{
					// set new opponent
					opponent = requester;
					// send reply
					msg.setMsg_type(Msg_type.YES_PLAYING);
					state.setState(Match_State.TO_START);
					// update view
					f.changePanel(f.getDashboard(), f.getUsersOnline());
				}
				else
				{
					msg.setMsg_type(Msg_type.NO_PLAYING);
				}
			}
			
		}
		else
		{
			msg.setMsg_type(Msg_type.ALREADY_IN_A_GAME);
		}
		sendMessage(msg);
	}

	/**
	 * It updates game's chat with events or messages
	 * 
	 * @param msg Message sent from the opponent, which must be added to my chat
	 */
	public void updateChat(Message msg)
	{
		switch(msg.msg_type)
		{
			case CHAT_MSG:
				f.getDashboard().getChat().addMsg((String)msg.object,Chat_message_type.OPPONENT);
				break;
			case CHAT_EVENT:
				f.getDashboard().getChat().addMsg((String)msg.object,Chat_message_type.EVENT);
				// The first client who start the match send to the other client a CHAT_EVENT msg and that client achive the opponent_to_start state
				if(state.getState() == Match_State.TO_START)
					state.setState(Match_State.OPPONENT_TO_START);
				// When the second player is ready, the first one, that has MY_TURN state, receive a CHAT_EVENT msg, as a confirm that the opponent is ready
				if(state.getState() == Match_State.MY_TURN)
				// Game ready to start --> disable opponent grid
				f.getDashboard().enableGrid(f.getDashboard().getOpponentCoor());
				break;
			default: break;
		}
	}

	/**
	 * Set sleep
	 * @param milliseconds for Thread.sleep(milliseconds) 
	 */
	public void _sleep(int milliseconds)
	{
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 * Change Socket timer
	 * @param milliseconds for connection.setSoTimeout(milliseconds)
	 */
	public void setSocketTimer(int milliseconds)
	{
		try {
			connection.setSoTimeout(milliseconds);
		} catch (SocketException e) {e.printStackTrace();}
	}
}
