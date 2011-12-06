package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/******************************************************
 * This class was a very simple class used to 		  *
 * retrieve the user id or the race id from the		  *
 * session.											  *
 ******************************************************/
public class IDController extends HttpServlet{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that doesn't do much.
	 */
	public IDController() {
		super();
	}

	/**
	 * Returns both the user id and race id if requested. If they are not it will just return
	 * -1 as the id.
	 * 
	 * @param request 	Request contain race and/or user set to 1 meaning it is requested.
	 * @param response	The ids that were requested.
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		// Will return -1 if it is not desired.
		int userId = -1;
		int raceId = -1;
		
		// Does the requestor want the user id?
		if (Integer.parseInt(request.getParameter("user")) == 1) {
			userId = Integer.parseInt((String) request.getSession().getAttribute("userId"));
		}
		
		// Does the requestor want the race id?
		if (Integer.parseInt(request.getParameter("race")) == 1) {
			raceId = Integer.parseInt((String) request.getSession().getAttribute("raceId"));
		}
		
		System.out.println("IDController: "+"(" + userId + "," + raceId + ")");
		out.println("(" + userId + "," + raceId + ")");
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
