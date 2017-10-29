package commons;

public class InvalidUsernameException extends Exception{
	private static final long serialVersionUID = -2161808073357292179L;
	public InvalidUsernameException(String msg){
		super(msg);
	}

}
