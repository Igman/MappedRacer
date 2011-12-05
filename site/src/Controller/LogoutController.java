package Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/******************************************************
 * Controller class that will log the user out. 	  *	
 ******************************************************/
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Simple constructor that calls it super.
	 */
	public LogoutController() {
		super();
	}
	
	/**
	 * Simple do get that takes the request and makes it so the user is no 
	 * longer logged in anymore. 
	 * 
	 * @param request	The request containing the session
	 * @param response	The response that has the redirect back.
	 */
	protected void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();

		response.sendRedirect(request.getContextPath()+ "/");

	}
	
	/**
	 * Will just pass request and response on to doGet.
	 * 
	 * @param request	The request which contains the JSON
	 * @param response	The response that will be passed back.
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
