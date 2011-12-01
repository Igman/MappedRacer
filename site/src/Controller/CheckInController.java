package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import Beans.CheckIn;

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

			// Adds the race to the database.
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

	private void createCheckIn(JSONObject json, HttpServletRequest request) throws ParseException,
			JSONException, SQLException, ServletException{
		CheckIn checkInModel = new CheckIn();

		int userId = json.getInt("userId");
		int raceId = json.getInt("raceId");
		String picture = json.getString("picture");
		String comment = json.getString("comment");
		String location = json.getString("location");
		if(json.getBoolean("postTweet") == true){
			Twitter twitter = (Twitter) request.getAttribute("twitter");
			TweetController.postTweet(comment, twitter);
		}
		// Starts adding things to the DB.
		checkInModel.addCheckIn(userId, raceId, picture, comment, location);

	}

}
