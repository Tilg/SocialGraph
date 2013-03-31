package exception;

public class MalFormedFileException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public MalFormedFileException(){
		super();
	}
	
	public MalFormedFileException(String message){
		super(message);
	}
}
