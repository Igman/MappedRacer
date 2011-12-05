package Controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.Racer;
//import twitter4j.Twitter; // Not needed.

/******************************************************
 * This class is used by a user who is joining a race *											  *
 ******************************************************/
public class JoinRaceController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Racer racerModel;
	
	public JoinRaceController() {
		super();
		try {
			racerModel = new Racer();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This is the do get which will get the race id and user and set them as attending the race.
	 * 
	 * @param request 	Request with race id as attribute and user id in the session. 
	 * @param response	Sends back the response redirection.
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // Not used: Twitter twitter = (Twitter)request.getSession().getAttribute("twitter");
		String raceId = request.getParameter("raceId");

		try {
			racerModel.setAttend(Integer.parseInt(raceId),(Integer)request.getSession().getAttribute("userid"), true);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Problem with the Number formatting. Reason: "+ e.getMessage());
			} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT, "Problem occured with the SQL. Reason: "+ e.getMessage());
		}
		
		response.sendRedirect(request.getContextPath() + "/usr_home.html");
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
