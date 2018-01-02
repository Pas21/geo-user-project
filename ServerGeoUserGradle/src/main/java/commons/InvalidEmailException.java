package commons;

/**
 * The Class InvalidEmailException reports the exception on duplicate emails.
 */
public class InvalidEmailException extends Exception{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2161808073357292179L;
	
	/**
	 * Instantiates a new invalid email exception.
	 *
	 * @param msg the msg
	 */
	public InvalidEmailException(String msg){
		super(msg);
	}

}
