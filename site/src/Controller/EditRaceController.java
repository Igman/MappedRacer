package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Beans.Item;
import Beans.ItemObj;
import Beans.Race;
import Beans.RaceObj;
import Beans.Racer;
import Beans.RacerObj;
import Beans.User;
import Exceptions.InsertException;

public class EditRaceController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Race raceModel;
	private User userModel;
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
			userModel = new User();
			racerModel = new Racer();
			itemModel = new Item();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");
		PrintWriter out = response.getWriter();

		int userID = (Integer) request.getSession().getAttribute("userId");
		int raceID = Integer.parseInt(request.getParameter("raceId"));

		String result = "";
		try {
			result += raceModel.getName(raceID);
			result += ",";

			raceObj = raceModel.getRace(raceID, userID);

			result += advDateGet();
			result += ",";

			result += advItemsGet();
			result += ",";

			result += advRacersGet();
			result += ",";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out.print(result);
	}

	private String advDateGet() {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");

		return sdf.format((java.util.Date) raceObj.getStart());
	}

	private String advItemsGet() throws SQLException {
		JSONArray result = new JSONArray();
		StringBuffer json = new StringBuffer("items:[");

		List<ItemObj> list = itemModel.getItemsObj(raceObj.getId());
		Iterator<ItemObj> iter = list.iterator();

		while (iter.hasNext()) {
			ItemObj temp = iter.next();
			json.append("{id:\"" + temp.getItemId() + "\", type:\""
					+ temp.getType() + "\", location:\"" + temp.getLocation()
					+ "\", value:\"" + temp.getValue() + "\"},");
		}
		if (list.isEmpty()) {
			json.append("]");
		} else {
			// replace last character with ]
			json.setCharAt(json.length() - 1, ']');
		}

		return json.toString();
	}

	private String advRacersGet() throws SQLException {
		StringBuffer json = new StringBuffer("racers:[");

		List<RacerObj> list = racerModel.getRacersObj(raceObj.getId());
		Iterator<RacerObj> iter = list.iterator();

		while (iter.hasNext()) {
			RacerObj temp = iter.next();
			json.append("{userID:\"" + temp.getUserId() + "\", username:\""
					+ temp.getUserName() + "\", score:\"" + temp.getScore()
					+ "\"},");
		}

		if (list.isEmpty()) {
			json.append("]");
		} else {
			// replace last character with ]
			json.setCharAt(json.length() - 1, ']');
		}
		return json.toString();
	}
}
