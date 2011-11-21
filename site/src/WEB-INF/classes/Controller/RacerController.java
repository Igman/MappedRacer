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
import javax.servlet.http.*;

import Beans.Racers;

import Beans.*;

public class RacerController {
	private int raceId;
	private int userId;
	private boolean attend;
	private Time totalTime;
	private int place;
	private int score;
	private Racers racer;
	
	private Connection c;
	private String address = "";	//Change me
	
	public void createRacer (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		PreparedStatement  ps = c.prepareStatement("INSERT INTO Racer VALUES (?,?,?,?,?,?)");
		DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		attend = true;
		
		raceId = Integer.parseInt(request.getParameter("raceId"));
		userId = Integer.parseInt(request.getParameter("userId"));
		
		//This might not work
		try {
			totalTime = (Time) sdf.parse(request.getParameter("totalTime"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		place = Integer.parseInt(request.getParameter("place"));
		score = Integer.parseInt(request.getParameter("score"));
		
		racer = new Racers(raceId, userId, attend, totalTime, place);
		
		ps.setInt(1, raceId);
		ps.setInt(2, userId);
		ps.setBoolean(3, attend);
		ps.setTime(4, totalTime);
		ps.setInt(5, place);
		ps.setInt(6, score);
		
		c.commit();
		ps.close();
		request.setAttribute("racer", racer); //SETS THE ITEM IN THE SESSION
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(address); //FORWARDS TO THE NEXT PAGE
		dispatcher.forward(request, response);
	}
	
	public void addPoints (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		PreparedStatement  ps = c.prepareStatement("UPDATE Racer SET score = ?, WHERE raceId = ?, userId = ?");
		
		raceId = Integer.parseInt(request.getParameter("raceId"));
		userId = Integer.parseInt(request.getParameter("userId"));
		score = Integer.parseInt(request.getParameter("score"));
				  
		ps.setInt(1, raceId);
		ps.setInt(2, userId);
		ps.setInt(3, score);
		
		int rowCount = ps.executeUpdate();
		if (rowCount == 0) 
			//Throw Exception
		
		c.commit();
		ps.close();
	}
}
