package commons;

/**
 * The Class InvalidDateException reports the exception on incorrect date/dates.
 */
public class InvalidDateException extends Exception{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2161808073357292179L;
	
	/**
	 * Instantiates a new invalid date exception.
	 *
	 * @param msg the msg
	 */
	public InvalidDateException(String msg){
		super(msg);
	}
}
