package Exceptions;

/**
 * Exception thrown on a DELETE SQL statement
 * 
 * @author Christiaan Fernando
 * 
 */
public class DeleteException extends Exception {

	public DeleteException() {
		super();
	}

	public DeleteException(String msg) {
		super(msg);
	}

}
