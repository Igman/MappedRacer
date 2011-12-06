package Exceptions;

/**
 * Exception thrown on an INSERT SQL statement
 * 
 * @author Christiaan Fernando
 * 
 */
public class InsertException extends Exception {

	private static final long serialVersionUID = 1L;

	public InsertException() {
		super();
	}

	public InsertException(String msg) {
		super(msg);
	}

}
