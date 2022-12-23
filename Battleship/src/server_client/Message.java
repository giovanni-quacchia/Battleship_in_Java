package server_client;

import java.io.Serializable;
import java.util.Date;

/**
 * Class used for creating objects to send between clients and server
 */
public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**
	 * Object to send
	 */
	protected Object object;
	/**
	 * nickname of the client to send the message to
	 */
	private String receiver="";
	/**
	 * nickname of the client who sends the message
	 */
	private String sender="";
	/**
	 * cell hit (sent with ATTACK_RESULT for allowing the opponent to update his grid)
	 */
	private String hit_cell = "";
	
	/**
	 * types of messages that can be sent
	 * 
	 * @author Giovanni Quacchia
	 * 
	 */
	public enum Msg_type
	{
		/**
		 * message to add to the chat
		 */
		CHAT_MSG,
		/**
		 * event to add to the chat (Player is ready to play / player hit a cell)
		 */
		CHAT_EVENT,
		/**
		 * update the panel that shows users online on the server
		 */
		JPANEL_USERS_UPDATE,
		/**
		 * server communicates to client that the chosen nickname is not available
		 */
		CONNECTION_ERROR,
		/**
		 * client wants to play with another client
		 */
		ASKING_FOR_PLAYING,
		/**
		 * client accepts to play
		 */
		YES_PLAYING,
		/**
		 * client does not accept to play
		 */
		NO_PLAYING,
		/**
		 * client is playing another match
		 */
		ALREADY_IN_A_GAME,
		/**
		 * server changes client's timer for waiting messages (When a client waits for a reply to an ASKING_FOR_PLAYING request)
		 */
		WAIT_OPPONENT_RESPONSE,
		/**
		 * server communicates to the client that the opponent has disconnected
		 */
		OPPONENT_OFFLINE,
		/**
		 * client communicates to the opponent that he is ready to play
		 */
		READY_TO_START,
		/**
		 * client communicates to the opponent the coordinates of the cell to attack
		 */
		HIT_CELL,
		/**
		 * client communicates to the opponent what he hit
		 */
		ATTACK_RESULT,
		/**
		 * server ends the connection
		 */
		CLOSE,
		/**
		 * User wants to login to the server
		 */
		LOGIN,
		/**
		 * User wants to sign up to the server
		 */
		SIGN_UP
	}
	/**
	 * type of the message
	 */
	protected Msg_type msg_type;
	
	/**
	 * timestamp of the message (Used for checking when the receiver of an ASKING_FOR_PLAYING request replys on time)
	 */
	protected long timestamp = new Date().getTime();
	
	/**
	 * It creates a new message to send
	 * 
	 * @param sender nickname of the client who sends the message
	 * @param receiver nickname of the client to send the message to
	 */
	public Message(String sender, String receiver)
	{
		this.sender=sender;
		this.receiver=receiver;
	}
	/**
	 * It creates an empty new message
	 */
	public Message(){}
	/**
	 * It creates a new message, where the object is specified
	 * @param obj object to send
	 */
	public Message(Object obj)
	{
		this.object = obj;
	}
	
	/**
	 * It creates a new Message, ready to be sent
	 * @param sender nickname of the client who sends the message
	 * @param receiver nickname of the client to send the message to
	 * @param obj object to send
	 * @param msg_type type of message to send
	 */
	public Message(String sender, String receiver, Object obj, Msg_type msg_type)
	{
		this.sender=sender;
		this.receiver=receiver;
		this.object=obj;
		this.msg_type=msg_type;
	}
	/**
	 * It creates a new Message, where msg_type is specified
	 * @param msg_type msg_type
	 */
	public Message(Msg_type msg_type)
	{
		this.msg_type=msg_type;
	}
	/**
	 * It sets object value
	 * 
	 * @param object new object value
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	/**
	 * It return the type of the message
	 * 
	 * @return msg_type
	 */
	public Msg_type getMsg_type() {
		return msg_type;
	}
	/**
	 * It sets msg_type value
	 * 
	 * @param msg_type new msg_type value
	 */
	public void setMsg_type(Msg_type msg_type) {
		this.msg_type = msg_type;
	}
	/**
	 * It returns receiver name
	 * 
	 * @return receiver
	 */
	public String getReceiver() {
		return receiver;
	}
	/**
	 * It sets receiver value
	 * 
	 * @param receiver new receiver name
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	/**
	 * It returns sender's name
	 * 
	 * @return sender
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * It sets sender's name
	 * 
	 * @param sender new sender name
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * It returns hit_cell value
	 * 
	 * @return hit_cell
	 */
	public String getHit_cell() {
		return hit_cell;
	}
	/**
	 * It sets hit_cell value
	 * 
	 * @param hit_cell new hit_cell value
	 */
	public void setHit_cell(String hit_cell) {
		this.hit_cell = hit_cell;
	}
	//TODO
	@Override
	public String toString() {
		return "receiver,sender,msg_type,obj: [" + receiver + "," + sender + "," + msg_type + "," + object;
	}

}
