package Exceptions;

/**
 * Exception thrown on an UPDATE SQL statement
 * 
 * @author Christiaan Fernando
 * 
 */
public class UpdateException extends Exception {

	private static final long serialVersionUID = 1L;

	public UpdateException() {
		super();
	}

	public UpdateException(String msg) {
		super(msg);
	}
}
