package Controller;

import java.io.IOException;

import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.Race;
import Exceptions.DeleteException;

/******************************************************
 * This class is used to delete a race because it is  *
 * so easy to create a race it should be just be      *
 * deleted and recreated to modify one.				  * 
 ******************************************************/
public class DeleteRaceController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Race raceModel;
	
	/**
	 * Constructor that just sets up the race model for the
	 * changes that may occur.
	 */
	public DeleteRaceController() {
		super();

		try {
			raceModel = new Race();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This is the do get and will simply delete the race whose ID is passed in the
	 * request's attributes.
	 * 
	 * @param request		Contains the race's ID.
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// The race id that will be deleted.
		int raceId = Integer.parseInt((String) request.getAttribute("raceId"));
		
		try {
			raceModel.deleteRace(raceId);
			
			// If it has got to this point it should mean everything went well.
			response.setStatus(HttpServletResponse.SC_OK);

			RequestDispatcher dispatcher = request.getRequestDispatcher("");
			dispatcher.forward(request, response);
			
		} catch (DeleteException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT, "Problem occured with the SQL. Reason: "+ e.getMessage());
		}
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
