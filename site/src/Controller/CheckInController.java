package Controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.CheckIn;

/**
 * 
 * @author Christiaan Fernando
 *
 */
public class CheckInController {
	int checkinId;
	int raceId;
	String picUrl;
	String comment;
	String location;

	CheckIn checkIn;
	private String address = ""; //TODO Change me

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
			 {
		
		raceId = Integer.parseInt(request.getParameter("raceId"));
		checkinId = Integer.parseInt(request.getParameter("checkinId"));
		picUrl = request.getParameter("picUrl");
		comment = request.getParameter("comment");
		location = request.getParameter("location");

		//checkIn = new CheckIn(raceId, picUrl, comment, location);
		//checkIn.addCheckInDB();

		request.setAttribute("checkin", checkIn); // SETS THE ITEM IN THE
													// SESSION

		RequestDispatcher dispatcher = request.getRequestDispatcher(address); // FORWARDS
																				// TO
																				// THE
																				// NEXT
																				// PAGE
		dispatcher.forward(request, response);
	}

}
