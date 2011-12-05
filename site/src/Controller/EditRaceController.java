package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.Item;
import Beans.ItemObj;
import Beans.Race;
import Beans.RaceObj;
import Beans.Racer;
import Beans.RacerObj;

/******************************************************
 * This class returns all the details for a race to   *
 * the view to allow for eventual modications.		  *
 ******************************************************/
public class EditRaceController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Race raceModel;
	private Racer racerModel;
	private Item itemModel;
	private RaceObj raceObj;

	/**
	 * Constructor which has the responsibility for initializing the model
	 * objects.
	 */
	public EditRaceController() {
		super();

		try {
			raceModel = new Race();
			racerModel = new Racer();
			itemModel = new Item();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This is the do get that will create the json object to be sent back to the viwe.
	 * 
	 * @param request		Contains the race's ID.
	 * @param response		Json object will be sent back through the response.
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");
		PrintWriter out = response.getWriter();
		
		int userID = (Integer) request.getSession().getAttribute("userId");
		int raceID = Integer.parseInt(request.getParameter("raceId"));
		
		// Starts the json string object to be sent back to the view.
		String result = "";
		try {
			raceObj = raceModel.getRace(raceID, userID);
			
			result += raceModel.getName(raceID);
			result += ",";			

			result += advDateGet();
			result += ",";

			result += advItemsGet();
			result += ",";

			result += advRacersGet();
			result += ",";
			
			System.out.println("EditRaceController: "+result);
			out.print(result);
		} catch (SQLException e) {
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
	
	/**
	 * This function retrieves the formatted date.
	 * 
	 * @return		The date.
	 */
	private String advDateGet() {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");

		return sdf.format((java.util.Date) raceObj.getStart());
	}

	/**
	 * This function is responsible for get the list of points/items on
	 * the map.
	 * 
	 * @return 		The list of items as json string.
	 * @throws SQLException
	 */
	private String advItemsGet() throws SQLException {
		// Starts the json string.
		StringBuffer json = new StringBuffer("items:[");
		
		// Gets the list of the item objects.
		List<ItemObj> list = itemModel.getItemsObj(raceObj.getId());
		Iterator<ItemObj> iter = list.iterator();

		while (iter.hasNext()) {
			ItemObj temp = iter.next();
			// Adds new value.
			json.append("{id:\"" + temp.getItemId() + "\", type:\""
					+ temp.getType() + "\", location:\"" + temp.getLocation()
					+ "\", value:\"" + temp.getValue() + "\"},");
		}
		// Adds the end of the of the string.
		if (list.isEmpty()) {
			json.append("]");
		} else {
			// replace last character with ]
			json.setCharAt(json.length() - 1, ']');
		}

		return json.toString();
	}
	
	/**
	 * Gets the racers as a JSON string.
	 * 
	 * @return				JSON string.
	 * @throws SQLException
	 */
	private String advRacersGet() throws SQLException {
		// Starts the json string.
		StringBuffer json = new StringBuffer("racers:[");
		
		// Gets the list of the item objects.
		List<RacerObj> list = racerModel.getRacersObj(raceObj.getId());
		Iterator<RacerObj> iter = list.iterator();

		while (iter.hasNext()) {
			RacerObj temp = iter.next();
			// Adds new value.
			json.append("{userID:\"" + temp.getUserId() + "\", username:\""
					+ temp.getUserName() + "\", score:\"" + temp.getScore()
					+ "\"},");
		}
		// Adds the end of the of the string.
		if (list.isEmpty()) {
			json.append("]");
		} else {
			// replace last character with ]
			json.setCharAt(json.length() - 1, ']');
		}
		return json.toString();
	}
}
