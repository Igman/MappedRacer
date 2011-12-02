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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import Beans.CheckIn;
import Beans.Item;
import Beans.Race;
import Beans.RaceObj;
import Beans.Racer;
import Beans.RacerObj;

public class ViewHistoryController extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String address = ""; // TODO Change me
	private Race raceModel;
	private Racer racerModel;
	
	public ViewHistoryController() {
		 super();
		try {
			raceModel = new Race();
			racerModel = new Racer();
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
		
		// Gets the race ID from the request
		//int userID = Integer.parseInt(request.getParameter("user_id"));	
		HttpSession session = request.getSession();
		int userID = (Integer) session.getAttribute("userid");

		try {
			// Adds the check ins to JSON
			String json =  getJSONRaces(userID);
			
			System.out.println(json);
			out.println(json);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT,
			"Problem occured with the SQL.");
		}finally {
			out.close();
		}		


	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private String getJSONRaces(int userID) throws SQLException {
		List<RaceObj> races = raceModel.getRacesObj(userID, true);
		
		Iterator<RaceObj> iterator = races.iterator();
		
		StringBuffer json = new StringBuffer("{races:[");
		RaceObj temp;
		
		while (iterator.hasNext()) {
			temp = iterator.next();
			
			json.append("{raceID:\""+temp.getId()+"\", creatorID:\""+temp.getCreatorId()+"\", name:\""+temp.getName()+ "\", time:\""+ temp.getStart().toString() +"\", rank:\""+racerModel.getRacerRank(userID, temp.getId()) +"\", score:\""+ temp.getScore() + "\"},");
		}
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
