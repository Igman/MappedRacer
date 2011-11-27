package Controller;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.*;

import Beans.Item;
import Beans.Race;
import Beans.Racer;

/**
 * This class is responsible for handling any requests that directly relate to
 * setting up and running the game.
 * 
 * @author Adam
 */
public class CreateRaceController extends HttpServlet {
	private Race raceModel = new Race();

	/**
	 * 
	 * @param racers
	 * @return
	 * @throws JSONException
	 */
	private boolean addRacersHelper(JSONArray racers) throws JSONException {
		boolean result = true;

		for (int i = 0; i < racers.length(); ++i) {
			JSONObject racer = racers.getJSONObject(i);
			Racer temp = new Racer(racer.getInt("userId"),
					racer.getInt("raceId"), false, null, 0);
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
	private boolean addItemsHelper(JSONArray items) throws JSONException {
		boolean result = true;
		
		JSONObject finalDestination = items.getJSONObject(1);
		
		for (int i = 1; i < items.length(); i++) {
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
			startTime.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm",
					Locale.ENGLISH).parse(request.getParameter("start_time")));
		} catch (ParseException e) {
			e.printStackTrace();

			// ("Date not formated correctly.", request, response);
			return;
		}
		
		//add race
		
		try {
			int raceId = raceModel.createRace(jsonObject.getString("name"), "",
					new java.sql.Time(startTime.getTime().getTime()),
					new java.sql.Date(startTime.getTime().getTime()), 0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean racerSuccess, itemSuccess;
		// add racers -> add users
		
		try {
			racerSuccess = addRacersHelper(jsonObject.getJSONArray("racers"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// add items

		// return response

	}
}
