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

/**
 * 
 * @author Christiaan Fernando
 * 
 */
public class CheckInController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String address = ""; // TODO Change me
	private CheckIn checkInModel;
	private Item itemModel;
	private Racer racerModel;
	private Race raceModel;

	public CheckInController() {

		try {
			checkInModel = new CheckIn();
			itemModel = new Item();
			racerModel = new Racer();
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

		String forward = "";

		try {
			// Creates the JSON Object from the request.
			JSONObject jsonObj = getJSON(request.getReader());

			// Adds the checkin to the database.
			createCheckIn(jsonObj, request);

			response.setStatus(HttpServletResponse.SC_OK);

			RequestDispatcher dispatcher = request
					.getRequestDispatcher(forward);
			dispatcher.forward(request, response);
		} catch (IOException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Problem when reading the JSON object.");
		} catch (JSONException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Problem with the JSON object.");
		} catch (ParseException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Problem with the Date object.");
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT,
					"Problem occured with the SQL.");
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * This function takes in a reader and tries to read a JSON Object out of
	 * it.
	 * 
	 * @param reader
	 *            predefined object that should have JSON object in it.
	 * @return The json object is successfully parsed.
	 * @throws IOException
	 * @throws JSONException
	 */
	private JSONObject getJSON(BufferedReader reader) throws IOException,
			JSONException {
		StringBuffer buffer = new StringBuffer();
		String line = null;
		JSONObject jsonObject;

		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}

		System.out.println(buffer.toString());

		jsonObject = new JSONObject(buffer.toString());

		return jsonObject;
	}

	private void createCheckIn(JSONObject json, HttpServletRequest request)
			throws ParseException, JSONException, SQLException,
			ServletException {

		// int userId = Integer.parseInt(json.getString("userId"));
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute("userid");
		int raceId = Integer.parseInt(json.getString("raceId"));
		String picture = json.getString("picture");
		String comment = json.getString("comment");
		String location = json.getString("location");
		
		if(json.getString("postTweet").equals("true")){
			Twitter twitter = (Twitter) session.getAttribute("twitter");
			TweetController.postTweet(comment + " " + picture, twitter);
		}
		int markerToDelete = Integer.parseInt(json.getString("markerToDelete"));
		// Starts adding things to the DB.
		checkInModel.addCheckIn(userId, raceId, picture, comment, location);

		int score = itemModel.getValue(markerToDelete);
		switch (itemModel.getType(markerToDelete)) {
		case 1:
			// do finish line
			raceModel.setFinished(raceId, true);
			racerModel.updateScore(userId, raceId, 1000);
			break;
		case 3:
			List<RacerObj> racers = racerModel.getRacersObj(raceId);
			Iterator<RacerObj> iter = racers.iterator();
			while (iter.hasNext()) {
				racerModel.updateScore(iter.next().getUserId(), raceId, score);
			}
			score *= -score;
			//fall through
		case 2:
			racerModel.updateScore(userId, raceId, score);
			break;
		default:
			// you should not be here
			break;
		}

		if (markerToDelete != -1)
			itemModel.setItemValue(markerToDelete, 0);

	}

}
