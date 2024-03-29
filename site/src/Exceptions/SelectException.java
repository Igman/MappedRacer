package Exceptions;

/**
 * Exception thrown on a SELECT SQL statement
 * 
 * @author Christiaan Fernando
 * 
 */
public class SelectException extends Exception {

	private static final long serialVersionUID = 1L;

	public SelectException() {
		super();
	}

	public SelectException(String msg) {
		super(msg);
	}

}
