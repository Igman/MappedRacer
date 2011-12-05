package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Beans.Race;
import Beans.User;
import Beans.Racer;
import Beans.Item;
import Beans.ItemObj;
import Exceptions.InsertException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;

/******************************************************
 * This is a controller class that is responsible for * 
 * setting up the race for a user. It calls the model * 
 * to ensure that the database is updated accordingly *
 ******************************************************/

public class CreateRaceController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Race raceModel;
	private User userModel;
	private Racer racerModel;
	private Item itemModel;

	/**
	 * Constructor which has the responsibility for initializing the model
	 * objects.
	 */
	public CreateRaceController() {
		super();

		try {
			raceModel = new Race();
			userModel = new User();
			racerModel = new Racer();
			itemModel = new Item();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is the doGet method which will take a request and a response and
	 * work with the request then inform the page where to go next.
	 * 
	 * @param request 	The request which contains the JSON
	 * @param response	The response that will be passed back.
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// Creates the JSON Object from the request.
			JSONObject jsonObj = getJSON(request.getReader());

			// Adds the race to the database.
			createRace(jsonObj, request);
			
			// If it has got to this point it should mean everything went well.
			response.setStatus(HttpServletResponse.SC_OK);

			RequestDispatcher dispatcher = request.getRequestDispatcher("");
			dispatcher.forward(request, response);
			
		} catch (IOException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Problem when reading the JSON object. Reason: "+ e.getMessage());
		} catch (JSONException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Problem with the JSON object. Reason: "+ e.getMessage());
		} catch (ParseException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Problem with the Date object. Reason: "+ e.getMessage());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT, "Problem occured with the SQL. Reason: "+ e.getMessage());
		} catch (InsertException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT, "Problem inserting into database. Reason: "+ e.getMessage());
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
	 * This function takes in a reader and tries to read a JSON Object out of
	 * it.
	 * 
	 * @param reader	predefined object that should have JSON object in it.
	 * @return			The json object is successfully parsed.
	 * @throws IOException
	 * @throws JSONException
	 */
	private JSONObject getJSON(BufferedReader reader) throws IOException, JSONException {
		StringBuffer buffer = new StringBuffer();
		String line = null;
		JSONObject jsonObject;
		
		// Cycles throught the reader.
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		
		// Print it out to console for debug purposes.
		System.out.println(buffer.toString());
		
		// Changes it to a string
		jsonObject = new JSONObject(buffer.toString());

		return jsonObject;
	}

	/**
	 * This function is responsible for preparing the data for the race and then
	 * adding it to the database.
	 * 
	 * @param json		This is the JSON object containing everything.
	 * @param request	Request which is used to get the current session.
	 * @throws ParseException
	 * @throws JSONException
	 * @throws SQLException
	 * @throws InsertException
	 */
	private void createRace(JSONObject json, HttpServletRequest request) throws ParseException, JSONException, SQLException, InsertException {
		// Race name
		String name = json.getString("name");
		// The date the race starts
		Calendar dateTime = advDateParse(json.getString("dateTime"));
		// The list of racer names
		String[] racers = advRacersParse(json.getJSONArray("racers"));
		// The list of item objects.
		ItemObj[] items = advItemsParse(json.getJSONArray("items"));

		// Starts adding things to the DB.

		// Adds/makes sure user are in DB.
		userModel.addUsers(racers);

		// Gets the creator from the session since the creator is the user logged in
		HttpSession session = request.getSession();
		int creatorID = (Integer) session.getAttribute("userid");
		String creatorName = userModel.getUserName(creatorID);

		// Creates the new race in the race DB.
		int raceID = raceModel.createRace(name, dateTime, creatorID);

		// Adds the items in the race to the items DB.
		itemModel.addItems(items, raceID);

		// Adds the racers of the race to the racer DB.
		sendInvites(racers, raceID, request); // TODO Users id auto created when racers created.
		racerModel.addRacers(racers, raceID);
		racerModel.addRacer(creatorName, raceID);
		racerModel.setAttend(raceID, creatorID, true);
	}

	/**
	 * This is helper function that parses through the JSONArray and makes it
	 * into a readable format that can be sent to the DB.
	 * 
	 * @param itemsArray	The items in a JSON array.
	 * @return 				The items in a ItemObj array.
	 * @throws JSONException
	 */
	private ItemObj[] advItemsParse(JSONArray itemsArray) throws JSONException {
		ItemObj[] itemList = new ItemObj[itemsArray.length()];

		for (int i = 0; i < itemsArray.length(); i++) {
			JSONObject item = itemsArray.getJSONObject(i);

			itemList[i] = new ItemObj();
			itemList[i].setLocation(item.getString("location"));
			itemList[i].setType(Integer.parseInt(item.getString("type")));
			itemList[i].setValue(Integer.parseInt(item.getString("value")));
		}

		return itemList;
	}

	/**
	 * This helper function parses through the racers in the JSON Array and
	 * returns their string names in just an array.
	 * 
	 * @param racersArray
	 *            JSON array containing all the user names.
	 * @return String array of the user names.
	 * @throws JSONException
	 */
	private String[] advRacersParse(JSONArray racersArray) throws JSONException {
		String[] racerList = new String[racersArray.length()];

		for (int i = 0; i < racersArray.length(); i++) {
			JSONObject racer = racersArray.getJSONObject(i);

			racerList[i] = racer.getString("name");
		}

		return racerList;
	}

	/**
	 * Helper function that formats the date object from string into a calendar
	 * object.
	 * 
	 * @param dateStr
	 *            The date as a string.
	 * @return The date as a Calendar.
	 * @throws ParseException
	 */
	private Calendar advDateParse(String dateStr) throws ParseException {
		Calendar date = Calendar.getInstance();
		// This must be the same way the date is formatted in the view.
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		date.setTime(formatter.parse(dateStr));

		return date;
	}

	/**
	 * This function is used to send out the invitiations to racers of the race via
	 * twitter. 
	 * 
	 * @param racers	The list of racer names.
	 * @param raceID	The race's ID.
	 * @param request	The request that contains the session.
	 */
	private void sendInvites(String[] racers, int raceID, HttpServletRequest request) {
		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
		
		// The message that will end up being posted on twitter.
		String invite = "%USER% Join my awesome race at http://ec2-50-112-43-245.us-west-2.compute.amazonaws.com:8080/MappedRacer/joinRace?raceId="	+ raceID;
		
		// Loops through all the racers sending out the invitation.
		for (String racer : racers) {
			String userInvite = invite.replaceAll("%USER%", racer);
			
			try {
				TweetController.postTweet(userInvite, twitter);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
	}
}
