package commons;

/**
 * The Class InvalidPositionException reports the exception on duplicate emails.
 */
public class InvalidPositionException extends Exception{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8670370241677898998L;
	
	/**
	 * Instantiates a new invalid position exception.
	 *
	 * @param msg the msg
	 */
	public InvalidPositionException(String msg) {
		super(msg);
	}

}
