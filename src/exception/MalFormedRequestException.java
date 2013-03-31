package exception;

/**
 * this exception is throw when the user try to execute an unwell formed request
 * @author Florent
 */
@SuppressWarnings("serial")
public class MalFormedRequestException extends Exception {
	
	/**
	 * dafault constructor for an exception
	 */
	public MalFormedRequestException(){
		super();
	}
	
	/**
	 * constructor of an exception who allow you to change the message display when the exception is throw
	 * @param message, a message dysplay when the exception is throw
	 */
	public MalFormedRequestException(String message){
		super(message);
	}
}
