package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Beans.Race;
import Beans.RaceObj;
import Beans.Racer;

/*********************************************************
 * Controller class that gets all the history for a user *
 * and returns it as a json object. This includes the    *
 * races the user has been in and the rank and points    *
 * they achieved during those races.					 *
 *********************************************************/
public class ViewHistoryController extends HttpServlet{
	private static final long serialVersionUID = 1L;

	private Race raceModel;
	private Racer racerModel;
	
	/**
	 * Constuctor that sets the race model and the racers model that
	 * will be used to to get the history in this controller.
	 */
	public ViewHistoryController() {
		super();
		
		try {
			raceModel = new Race();
			racerModel = new Racer();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This is the do get that will take the request and get the session with the user id and 
	 * return the past races of that user in a JSON object.
	 * 
	 * @param request 		Contains the session with the user id in it.
	 * @param response		Out put the JSON object to.
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		// Get session -> get user ID.
		HttpSession session = request.getSession();
		int userID = (Integer) session.getAttribute("userid");

		try {
			// Adds the check ins to JSON
			String json =  getJSONRaces(userID);
			
			System.out.println(json);
			out.println(json);
		}  catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT, "Problem occured with the SQL. Reason: "+ e.getMessage());
		} finally {
			out.close();
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
	
	
	/**
	 * This function does the work of setting up the JSON object from the 
	 * request to the model about the races for a user.
	 * 
	 * @param userID	The user that the races will be for.
	 * @return			The list of races for the user.
	 * @throws SQLException
	 */
	private String getJSONRaces(int userID) throws SQLException {
		// Gets the list of races for the user.
		List<RaceObj> races = raceModel.getRacesObj(userID, true);
		
		Iterator<RaceObj> iterator = races.iterator();
		
		// Begins creating the json object.
		StringBuffer json = new StringBuffer("{races:[");
		RaceObj temp;
		
		// Loops through the list of races and sets the JSON object.
		while (iterator.hasNext()) {
			temp = iterator.next();
			
			json.append("{raceID:\""+temp.getId()+"\", creatorID:\""+temp.getCreatorId()+"\", name:\""+temp.getName()+ "\", time:\""+ temp.getStart().toString() +"\", rank:\""+racerModel.getRacerRank(userID, temp.getId()) +"\", score:\""+ temp.getScore() + "\"},");
		}
		
		// Sets the ending characters of the JSON object.
		if(races.isEmpty()){
			json.append("]");
		}else{
			// replace last character with ]
			json.setCharAt( json.length()-1,']');
		}
		
		json.append("}");
        
		return json.toString();
	}
}
