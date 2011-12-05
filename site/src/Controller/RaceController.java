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

import Beans.CheckIn;
import Beans.CheckInObj;
import Beans.Item;
import Beans.ItemObj;
import Beans.Racer;
import Beans.RacerObj;
import Exceptions.SelectException;

/**********************************************************
 * This class is used for managing the race by retrieving *
 * data from the database for the view.					  *
 **********************************************************/
public class RaceController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private Racer racerModel;
    private Item itemModel;
    private CheckIn checkInModel;
    
    /**
     * Constructor that sets up the model classes that will be used by this
     * controller.
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
	 * This is the do get that will begin to create the json object representing the
	 * race id that was requested.
	 * 
	 * @param request		Contains the race's ID.
	 * @param response		Json object will be sent back through the response.
	 * @throws IOException
	 * @throws ServletException
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
			
			System.out.println("RaceController: "+json);
			out.println(json);
			
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT,	"Problem occured with the SQL.  Reason: "+ e.getMessage());
		}finally {
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
	 * This creates a JSON string for the checkins of the race requested.
	 * 
	 * @param raceID	The race id.
	 * @return			String representing the JSON.
	 * @throws SQLException
	 */
	private String getJSONCheckIn(int raceID) throws SQLException {
		// Gets list of checkins as check in objects.
		List<CheckInObj> checkIns = null;
		try {
			checkIns = checkInModel.getCheckInObjects(raceID);
		} catch (SelectException e) {
			System.out.println(e.toString());
		}
		Iterator<CheckInObj> iterator = checkIns.iterator();
		
		// Beginning of the JSON object.
		StringBuffer json = new StringBuffer("checkin:[");
		CheckInObj temp;
		
		// Loops through the list of checkins and sets the JSON object.
		while (iterator.hasNext()) {
			temp = iterator.next();
			
			json.append("{userID:\""+temp.getUserID()+"\", location:\""+temp.getLocation()+"\", comment:\""+temp.getComment()+"\", picture:\""+temp.getPic()+ "\"},");
		}
		
		// Sets the ending characters of the JSON object.
		if(checkIns.isEmpty()){
			json.append("]");
		}else{
			// replace last character with ]
			json.setCharAt( json.length()-1,']');
		}
		
		return json.toString();
	}

	/**
	 * This creates a JSON string for the racers of the race requested.
	 * 
	 * @param raceID	The race id.
	 * @return			String representing the JSON.
	 * @throws SQLException
	 */
	private String getJSONRacers(int raceID) throws SQLException {
		// Gets a list of racer objects of the race.
		List<RacerObj> racers = racerModel.getRacersObj(raceID);
		Iterator<RacerObj> iterator = racers.iterator();
		
		// The start of hte JSON object.
		StringBuffer json = new StringBuffer("racers:[");
		RacerObj temp;
		
		// Loops through all of the racers adding them to the JSON.
		while (iterator.hasNext()) {
			temp = iterator.next();
			
			json.append("{userID:\""+temp.getUserId()+"\", username:\""+temp.getUserName()+"\", score:\""+temp.getScore()+ "\"},");
		}
		
		// Sets the ending characters of the JSON object.
		if(racers.isEmpty()){
			json.append("]");
		}else{
			// replace last character with ]
			json.setCharAt( json.length()-1,']');
		}
        
		return json.toString();
	}
	
	/**
	 * This creates a JSON string for the items of the race requested.
	 * 
	 * @param raceID	The race id.
	 * @return			String representing the JSON.
	 * @throws SQLException
	 */
	private String getJSONItems(int raceID) throws SQLException {
		// Gets list of items for the race.
		List<ItemObj> items = itemModel.getItemsObj(raceID);
		Iterator<ItemObj> iterator = items.iterator();
		
		// Starts the json string.
		StringBuffer json = new StringBuffer("items:[");
		ItemObj temp;
		
		// Adds items to json string.
		while (iterator.hasNext()) {
			temp = iterator.next();
			
			json.append("{id:\""+temp.getItemId()+"\", type:\""+temp.getType()+"\", location:\""+temp.getLocation()+"\", value:\""+temp.getValue() + "\"},");
		}
		
		// Sets the ending characters of the JSON object.
		if(items.isEmpty()){
			json.append("]");
		}else{
			// replace last character with ]
			json.setCharAt( json.length()-1,']');
		}
		
		return json.toString();
	}
}
