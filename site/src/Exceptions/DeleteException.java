package Exceptions;

/**
 * Exception thrown on a DELETE SQL statement
 * 
 * @author Christiaan Fernando
 * 
 */
public class DeleteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeleteException() {
		super();
	}

	public DeleteException(String msg) {
		super(msg);
	}

}
