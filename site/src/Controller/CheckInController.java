package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
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
import Beans.Racer;
import Beans.RacerObj;
import Exceptions.InsertException;
import Exceptions.SelectException;
import Exceptions.UpdateException;

/******************************************************
 * This is a controller class that is responsible for * handling the check ins
 * that occur during the race. * It must ensure that the view and database stay
 * * consistent. *
 ******************************************************/
public class CheckInController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CheckIn checkInModel;
	private Item itemModel;
	private Racer racerModel;
	private Race raceModel;

	/**
	 * The constructor which sets up the model classes that will be used through
	 * out this controller.
	 */
	public CheckInController() {
		super();

		try {
			checkInModel = new CheckIn();
			itemModel = new Item();
			racerModel = new Racer();
			raceModel = new Race();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is the do get where the requests to the controller will come in.
	 * This function is directly responsible for handling the requests and
	 * parsing the json object that will be sent in.
	 * 
	 * @param request
	 *            The request containing the json object.
	 * @param response
	 *            The response that will be used to send back the status.
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Creates the JSON Object from the request.
			JSONObject jsonObj = getJSON(request.getReader());

			// Adds the checkin to the database.
			createCheckIn(jsonObj, request);

			// If it has got to this point it should mean everything went well.
			response.setStatus(HttpServletResponse.SC_OK);

			RequestDispatcher dispatcher = request.getRequestDispatcher("");
			dispatcher.forward(request, response);

		} catch (IOException e) {
			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Problem when reading the JSON object. Reason: "
							+ e.getMessage());
		} catch (JSONException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Problem with the JSON object. Reason: " + e.getMessage());
		} catch (ParseException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Problem with the Date object. Reason: " + e.getMessage());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT,
					"Problem occured with the SQL. Reason: " + e.getMessage());
		}
	}

	/**
	 * Will just pass request and response on to doGet.
	 * 
	 * @param request
	 *            The request which contains the JSON
	 * @param response
	 *            The response that will be passed back.
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * This function takes in a reader and tries to read a JSON Object out of
	 * it.
	 * 
	 * @param reader
	 *            Predefined object that should have JSON object in it.
	 * @return The json object that is successfully parsed.
	 * @throws IOException
	 * @throws JSONException
	 */
	private JSONObject getJSON(BufferedReader reader) throws IOException,
			JSONException {
		StringBuffer buffer = new StringBuffer();
		String line = null;
		JSONObject jsonObject;

		// Cycles through the reader.
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}

		System.out.println("CheckInController: "+buffer.toString());
		// Gets the object as a json string.
		jsonObject = new JSONObject(buffer.toString());

		return jsonObject;
	}

	/**
	 * This function is used for handling the checkin that the request has made.
	 * It takes in the parsed json object and request and updates the database.
	 * 
	 * @param json
	 *            The check in.
	 * @param request
	 *            Used to get session.
	 * @throws ParseException
	 * @throws JSONException
	 * @throws SQLException
	 * @throws ServletException
	 */
	private void createCheckIn(JSONObject json, HttpServletRequest request)
			throws ParseException, JSONException, SQLException,
			ServletException {
		// Retrieves the session from the request.
		HttpSession session = request.getSession();
		// Gets the user who checked in from the session.
		int userId = (Integer) session.getAttribute("userid");

		// Parses out the details of the check from the json object.
		int raceId = Integer.parseInt(json.getString("raceId"));
		String picture = json.getString("picture");
		String comment = json.getString("comment");
		String location = json.getString("location");

		// Posts the comment to twitter if the user wanted to.
		if (json.getString("postTweet").equals("true")) {
			Twitter twitter = (Twitter) session.getAttribute("twitter");
			TweetController.postTweet(comment + " " + picture, twitter);
		}

		// Adds the check in to DB so everyone can access it.
		try {
			checkInModel.addCheckIn(userId, raceId, picture, comment, location);
		} catch (InsertException e) {
			System.out.println(e.toString());
		}

		// Gets the id of an item that might be marked to be deleted. Ignored if
		// -1.
		int markerToDelete = Integer.parseInt(json.getString("markerToDelete"));

		// If the marker to delete is -1 that means the user is not in any item
		// and they will not need to do the rest of the following. TODO?
		if (markerToDelete == -1) {
			return;
		}

		// Check in was on a item!

		// Gets the value associated with the item.
		int score = 0;
		try {
			score = itemModel.getValue(markerToDelete);

			// Acts accordingly depending on the type of the item.
			switch (itemModel.getType(markerToDelete)) {
			case 1:
				// This is the finish line therefore user will retrieve 1000
				// points to their score.
				raceModel.setFinished(raceId, true);
				racerModel.updateScore(userId, raceId, 1000);
				break;
			case 3:
				// This is a negative item value that minus score from other
				// users.
				List<RacerObj> racers = racerModel.getRacersObj(raceId);
				Iterator<RacerObj> iter = racers.iterator();

				// Loops through all the racers. updating their score.
				while (iter.hasNext()) {
					racerModel.updateScore(iter.next().getUserId(), raceId,
							score);
				}

				// This will fall through to next case to negate the effects to
				// the user
				// who picked up the item since they should not lose points.
				// TODO?
				score *= -2;
			case 2:
				racerModel.updateScore(userId, raceId, score);
				break;
			default:
				// you should not be here
				break;

			}

			itemModel.setValue(markerToDelete, 0);
		} catch (UpdateException e) {
			System.out.println(e.toString());
		} catch (SelectException e) {
			System.out.println(e.toString());
		}

	}

}
