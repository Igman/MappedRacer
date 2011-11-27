package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.Race;

/**
 * 
 * @author Christiaan Fernando
 *
 */
public class DisplayUserController {
	private int userId;
	private int raceId;
	private Race race;
	
	private String address = "";	//TODO Change me
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void DisplayRaces (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		raceId = Integer.parseInt(request.getParameter("raceId"));
		userId = Integer.parseInt(request.getParameter("userId"));
		
		List<Race> lsRace = race.getRacesDB(raceId, userId);
		
		request.setAttribute("lsRace", lsRace);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(address); //FORWARDS TO THE NEXT PAGE
		dispatcher.forward(request, response);
	}
}
