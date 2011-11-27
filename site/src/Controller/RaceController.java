package Controller;

import java.io.*;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.*;

import Beans.Item;
import Beans.Race;
import Beans.Racer;
import Beans.User;

/**
 * This class is responsible for handling any requests that directly relate to
 * setting up and running the game.
 * 
 * @author Adam
 */
public class RaceController extends HttpServlet {
	private Race raceModel = new Race();
	private User userModel = new User();

	/**
	 * 
	 * @param racers
	 * @return
	 * @throws JSONException
	 */
	private boolean addRacersHelper(JSONArray racers, int raceId) throws JSONException {
		boolean result = true;

		for (int i = 0; i < racers.length(); ++i) {
			JSONObject racer = racers.getJSONObject(i);
			Racer temp = new Racer(userModel.addUserDB(racer.getString("uname")),
					raceId, false, null, 0);
			if (!temp.addRacerDB())
				result = false;
		}
		return result;
	}

	/**
	 * 
	 * @param items
	 * @return
	 * @throws JSONException
	 */
	private boolean addItemsHelper(JSONArray items, int raceId) throws JSONException {
		boolean result = true;

		for (int i = 0; i < items.length(); i++) {
			JSONObject item = items.getJSONObject(i);
			Item temp = new Item(item.getInt("val"), true,
					item.getString("loc"));
			if (!temp.addItemDB())
				result = false;
		}
		return result;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuffer buffer = new StringBuffer();
		String line = null;
		JSONObject jsonObject;
		Calendar startTime = Calendar.getInstance();

		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			System.err.println("Unable to read request line by line.");
		}

		try {
			jsonObject = new JSONObject(buffer.toString());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON request string");
			throw new IOException("Error parsing JSON request string");
		}

		// add race

		try {
			startTime.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
					Locale.ENGLISH).parse(jsonObject.getString("dateTime")));
		} catch (ParseException | JSONException e) {
			e.printStackTrace();

			// ("Date not formated correctly.", request, response);
			return;
		}
		
		//add race
		
		int raceId = -1;
		try {
			raceId = raceModel.createRace(jsonObject.getString("name"), "",
					new java.sql.Time(startTime.getTime().getTime()),
					new java.sql.Date(startTime.getTime().getTime()), 0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean racerSuccess, itemSuccess;
		// add racers -> add users
		
		try {
			racerSuccess = addRacersHelper(jsonObject.getJSONArray("racers"), raceId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// add items

		// return response

	}

	// private final int CREATE_RACE = 1;
	// private final int ADD_RACER = 2;
	// private final int ADD_ITEM = 3;
	// private final int END_RACE = 4;
	// private final int CHECK_IN = 5;
	// private final int CHECK_RACE = 6;
	//
	// private Race race = new Race();
	// private RacerController racers = new RacerController();
	// private CheckInController checkIn = new CheckInController();
	//

	//
	// /**
	// *
	// * @param items
	// * @return
	// */

	//
	// /**
	// * This is the doGet function which will be called when a request is made.
	// * This function is used as the meditator that will pass along the task to
	// * handle the specific request types since there are many possibility
	// * functionality that is requested.
	// *
	// * @param request
	// * Request obj containing parameters passed and session returned.
	// * @param response
	// * @throws ServletException
	// * @throws IOException
	// */
	// public void doGet(HttpServletRequest request, HttpServletResponse
	// response)
	// throws ServletException, IOException {
	// // Parse values from the request to determine type of request.
	// // Switch statement that checks first parameter and acts accordingly
	// int requestType = Integer
	// .parseInt(request.getParameter("request_type"));
	// switch (requestType) {
	// case CREATE_RACE:
	// createRace(request, response);
	// break;
	// /*
	// case ADD_RACER:
	// addRacers(request, response);
	// break;
	// case ADD_ITEM:
	// addItems(request, response);
	// break;
	// */
	// default:
	// error("Unknown request", request, response);
	// }
	// }
	//
	// /**
	// * This function will handle the request that initialises the race that
	// the
	// * user is setting up. At this stage the user will have set the date,
	// name,
	// * etc. It will Initialise the representation of the race in the DB.
	// *
	// * @param request
	// * Request obj containing parameters passed and session returned.
	// * @param response
	// * @throws ServletException
	// * @throws IOException
	// */
	// private void createRace(HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException {
	// JSONObject items, racers;
	// // Parses all of the parameters out of the request.
	// int userID = Integer.parseInt(request.getParameter("user_id"));
	// Date startTime;
	// // There can be errors if the string is not formatted correctly by the
	// // UI.
	// try {
	// startTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
	// Locale.ENGLISH).parse(request.getParameter("start_time"));
	// } catch (ParseException e) {
	// e.printStackTrace();
	//
	// error("Date not formated correctly.", request, response);
	// return;
	// }
	// String endPoint = request.getParameter("end_point");
	// String raceName = request.getParameter("name");
	// Date createDate = new Date();
	//
	// items = (JSONObject) JSONSerializer.toJSON(request
	// .getAttribute("items"));
	// addItemsHelper(items);
	//
	// racers = (JSONObject) JSONSerializer.toJSON(request
	// .getAttribute("racers"));
	// addRacersHelper(racers);
	//
	// // Calls the model to create the race and returns the race id
	// int raceID = race.createRace(raceName, endPoint, startTime, createDate,
	// userID);
	//
	// boolean raceSuccess = addRacersHelper(racers);
	// boolean itemSuccess = addItemsHelper(items);
	//
	// if (raceID != -1 && raceSuccess) {
	// // Determines the next page.
	// RequestDispatcher dispatcher = request
	// .getRequestDispatcher("/WEB-INF/JSPs/add_items.jsp");
	// dispatcher.forward(request, response);
	//
	// } else {
	// error("Race creation was unsuccessful.", request, response);
	// return;
	// }
	//
	// }
	//
	// private void error(String errMsg, HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException {
	//
	// // Does nothing tells the view to display that something went wrong.
	// // Goes back to current page?
	// // Adds the error message.
	// request.setAttribute("error_msg", errMsg);
	// // Determines the next page.
	// RequestDispatcher dispatcher = request
	// .getRequestDispatcher("/WEB-INF/JSPs/error.jsp");
	// dispatcher.forward(request, response);
	// }
}
