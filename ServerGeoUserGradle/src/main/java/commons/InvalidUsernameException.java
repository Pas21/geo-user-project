package commons;

// TODO: Auto-generated Javadoc
/**
 * The Class InvalidUsernameException reports the exception on duplicate usernames.
 */
public class InvalidUsernameException extends Exception{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2161808073357292179L;
	
	/**
	 * Instantiates a new invalid username exception.
	 *
	 * @param msg the msg
	 */
	public InvalidUsernameException(String msg){
		super(msg);
	}

}
