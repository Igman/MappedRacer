package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.Racer;

/**
 * 
 * @author Christiaan Fernando
 * 
 */
public class RacerController {
	private final int CREATE_RACER = 1;
	private final int ADD_POINTS = 2;

	private int raceId;
	private int userId;
	private boolean attend;
	private Time totalTime;
	private int place;
	private int score;
	private Racer racer;

	private String address = ""; //TODO Change me

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
		DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		int requestType = Integer
				.parseInt(request.getParameter("request_type"));
		RequestDispatcher dispatcher;
		attend = true;

		raceId = Integer.parseInt(request.getParameter("raceId"));
		userId = Integer.parseInt(request.getParameter("userId"));

		switch (requestType) {
		case (CREATE_RACER):

			// This might not work
			try {
				totalTime = (Time) sdf.parse(request.getParameter("totalTime"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			place = Integer.parseInt(request.getParameter("place"));
			score = Integer.parseInt(request.getParameter("score"));

			racer = new Racer(raceId, userId, attend, totalTime, place);
			racer.addRacerDB();

			request.setAttribute("racer", racer); // SETS THE ITEM IN THE
													// SESSION

			dispatcher = request.getRequestDispatcher(address); // FORWARDS TO
																// THE NEXT PAGE
			dispatcher.forward(request, response);
			break;

		case (ADD_POINTS):
			score = Integer.parseInt(request.getParameter("score"));

			racer.updateRacerScoreDB(raceId, userId, score);

			dispatcher = request.getRequestDispatcher(address); // FORWARDS TO
																// THE NEXT PAGE
			dispatcher.forward(request, response);
			break;
		}
	}
}
