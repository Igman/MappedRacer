package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import Beans.CheckIn;
import Beans.Item;
import Beans.Race;
import Beans.RaceObj;
import Beans.RacerObj;

public class ViewHistoryController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String address = ""; // TODO Change me
	private Race raceModel;
	
	public ViewHistoryController() {
		 
		try {
			raceModel = new Race();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		String forward = "";

		try {
			//Get user id from request
			int userID = Integer.parseInt(request.getParameter("user_id"));	
			
			// Create JSON object
			String json = "";
			
			try {
				json += "{";
			
				// Adds the check ins to JSON
				json += getJSONRaces(userID);
				
				json += "}";
				
				System.out.println(json);
				out.println(json);
			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_CONFLICT,
				"Problem occured with the SQL.");
			}finally {
				out.close();
			}		

			response.setStatus(HttpServletResponse.SC_OK);

			RequestDispatcher dispatcher = request
					.getRequestDispatcher(forward);
			dispatcher.forward(request, response);
		} catch (IOException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Problem when reading the JSON object.");
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private String getJSONRaces(int userID) throws SQLException {
		List<RaceObj> races = raceModel.getRacesObj(userID);
		
		Iterator<RaceObj> iterator = races.iterator();
		
		StringBuffer json = new StringBuffer("racers:[");
		RaceObj temp;
		
		while (iterator.hasNext()) {
			temp = iterator.next();
			
			json.append("{raceID:\""+temp.getId()+"\", creatorID:\""+temp.getCreatorId()+"\", name:\""+temp.getName()+ "\", time:\""+ temp.getStart().toString() + "\", time:\""+ temp.getScore() + "\"},");
		}
		// replace last character with ]
		json.setCharAt( json.length()-1,']');
        
		return json.toString();
	}
}
