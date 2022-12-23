package controller;

/**
 * MatchState is used for sharing user's state between Control class and Client one.
 * 
 * @author Giovanni Quacchia
 * 
 */
public class MatchState {

	/**
	 * User's states that can be established
	 */
	public enum Match_State
	{
		/**
		 * TO_START, set when a user starts a game.
		 */
		TO_START,
		/**
		 * WAITING_FOR_START, set when a user is ready to start, but not the opponent
		 */
		WAITING_FOR_START,
		/**
		 * MY_TURN, set for the first user that is ready to play. Then set when it is the users turn, to attack.
		 */
		MY_TURN,
		/**
		 * OPPONENT_TO_START, set for the user when the opponent is ready to start, but I'm not and I want to keep moving the ships.
		 */
		OPPONENT_TO_START, 
		/**
		 * OPPONENT_TURN, set for the second user that is ready to play. Then set when it is the opponent's turn to attack.
		 */
		OPPONENT_TURN,
		/**
		 * END, set when game ends
		 */
		END
	} 
	
	/**
	 * state attribute, which can take the values of the enumerator defined above.
	 * It is protected, so Control can access to "state" without using get or set.
	 */
	protected Match_State state;
	
	/**
	 * Constructor, that sets attribute state to null.
	 */
	public MatchState() 
	{
		state = null;
	}

	/**
	 * Returns state value
	 * 
	 * @return state value
	 */
	public Match_State getState() {
		return state;
	}

	/**
	 * Sets the state
	 * 
	 * @param state state to set
	 */
	public void setState(Match_State state) {
		this.state = state;
	}

}
