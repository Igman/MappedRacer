package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.*;


public class CheckInController {
	int checkinId;
	int raceId;
	String picUrl;
	String comment;
	String location;
	String geolocation;
	
	CheckIn checkIn;
	CheckIn checkInItems[];
	private Connection c;
	private String address = "";	//Change me
	
	public void createCheckIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		PreparedStatement  ps = c.prepareStatement("INSERT INTO CheckIn VALUES (?,?,?,?,?)");
		
		raceId = Integer.parseInt(request.getParameter("raceId"));
		checkinId = Integer.parseInt(request.getParameter("checkinId"));
		picUrl = request.getParameter("picUrl");
		comment = request.getParameter("comment");
		location = request.getParameter("location");
		
		checkIn = new CheckIn(raceId, picUrl, comment, location);
		
		ps.setInt(1, raceId);
		ps.setString(2, picUrl);
		ps.setString(3, comment);
		ps.setString(4, location);
		
		c.commit();
		ps.close();
		request.setAttribute("checkin", checkIn); //SETS THE ITEM IN THE SESSION
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(address); //FORWARDS TO THE NEXT PAGE
		dispatcher.forward(request, response);
	}
	
}
