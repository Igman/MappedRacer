package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import Beans.CheckIn;
import Beans.CheckInObj;
import Beans.Item;
import Beans.ItemObj;
import Beans.Racer;
import Beans.RacerObj;
import Beans.User;

/**
 * Servlet implementation class RaceController
 */
public class RaceController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Racer racerModel;
    private Item itemModel;
    private CheckIn checkInModel;
    
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RaceController() {
        super();

        try {
			racerModel = new Racer();
			itemModel = new Item();
			checkInModel = new CheckIn();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");
        PrintWriter out = response.getWriter();
        
        // Gets the race ID from the request
		int raceID = Integer.parseInt(request.getParameter("race_id"));		
        
		// Create JSON object
		String json = "";
		
		try {
			json += "{";
			
			// Adds items to JSON
			json += getJSONItems(raceID);
			
			json += ",";
			
			// Adds racers to JSON
			json += getJSONRacers(raceID);
			
			json += ",";
			
			// Adds the check ins to JSON
			json += getJSONCheckIn(raceID);
			
			json += "}";
			
			System.out.println(json);
			out.println(json);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT,
			"Problem occured with the SQL.");
		}finally {
			out.close();
		}		
	}

	private String getJSONCheckIn(int raceID) throws SQLException {
		List<CheckInObj> checkIns = checkInModel.getCheckInObjects(raceID);
		Iterator<CheckInObj> iterator = checkIns.iterator();
		
		StringBuffer json = new StringBuffer("checkin:[");
		CheckInObj temp;
		
		while (iterator.hasNext()) {
			temp = iterator.next();
			
			json.append("{userID:\""+temp.getUserID()+"\", location:\""+temp.getLocation()+"\", comment:\""+temp.getComment()+"\", picture:\""+temp.getPic()+ "\"},");
		}
		
		if(checkIns.isEmpty()){
			json.append("]");
		}else{
			// replace last character with ]
			json.setCharAt( json.length()-1,']');
		}
		
		return json.toString();
	}

	// Score uname uiD of those attending raceID
	private String getJSONRacers(int raceID) throws SQLException {
		List<RacerObj> racers = racerModel.getRacersObj(raceID);
		Iterator<RacerObj> iterator = racers.iterator();
		
		StringBuffer json = new StringBuffer("racers:[");
		RacerObj temp;
		
		while (iterator.hasNext()) {
			temp = iterator.next();
			
			json.append("{userID:\""+temp.getUserId()+"\", username:\""+temp.getUserName()+"\", score:\""+temp.getScore()+ "\"},");
		}
		// replace last character with ]
		json.setCharAt( json.length()-1,']');
        
		return json.toString();
	}

	private String getJSONItems(int raceID) throws SQLException {
		List<ItemObj> items = itemModel.getItemsObj(raceID);
		Iterator<ItemObj> iterator = items.iterator();
		
		StringBuffer json = new StringBuffer("items:[");
		ItemObj temp;
		
		while (iterator.hasNext()) {
			temp = iterator.next();
			
			json.append("{id:\""+temp.getItemId()+"\", type:\""+temp.getType()+"\", location:\""+temp.getLocation()+"\", value:\""+temp.getValue() + "\"},");
		}
		// replace last character with ]
		json.setCharAt( json.length()-1,']');
        
        
		return json.toString();
	}

	/**
	 * Will just pass request and response on to doGet.
	 * 
	 * @param request	The request which contains the JSON
	 * @param response	The response that will be passed back.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
