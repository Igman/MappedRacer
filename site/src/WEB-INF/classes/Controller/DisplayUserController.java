package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.*;

/**
 * 
 * @author Masterfod
 *
 */
public class DisplayUserController {
	private int userId;
	private int raceId;
	
	private String address = "";	//Change me
	private Connection c;
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public List<Race> DisplayRaces (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Race r = ? LEFT JOIN User u = ? ON (u.id = r.userid)");
		SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss");
		SimpleDateFormat date = new SimpleDateFormat("MM:dd:yy");
		
		List<Race> lsRace = new ArrayList<Race>();
		
		int id;
		String name;
		String endPoint;
		Date createDate = null;
		Time startTime = null;
		Date startDate = null;
		int creatorId;
		
		raceId = Integer.parseInt(request.getParameter("raceId"));
		userId = Integer.parseInt(request.getParameter("userId"));
		
		ps.setInt(1, raceId);
		ps.setInt(2, userId);
		
		ResultSet rs = ps.executeQuery();
		ps.close();
		
		while (rs.next()){
			id = Integer.parseInt(rs.getString(1));
			name = rs.getString(2);
			endPoint = rs.getString(3);
			try {
				createDate = (Date) date.parse(rs.getString(4));
				startTime = (Time) time.parse(rs.getString(5));
				startDate = (Date) date.parse(rs.getString(6));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			creatorId = Integer.parseInt(rs.getString(7));
			
			Race race = new Race(name, endPoint, createDate, startTime, startDate, creatorId);
			race.setId(id);
			
			lsRace.add(race);
		}
		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(address); //FORWARDS TO THE NEXT PAGE
		dispatcher.forward(request, response);
		
		return lsRace;
	}
}
