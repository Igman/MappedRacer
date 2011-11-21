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

import Beans.*;
/**
 * 
 * @author Masterfod
 *
 */
public class RacerController {
	private int raceId;
	private int userId;
	private boolean attend;
	private Time totalTime;
	private int place;
	private int score;
	private Racers racer;
	
	private String address = "";	//Change me
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
		racer.racersDB();
	
		request.setAttribute("racer", racer); //SETS THE ITEM IN THE SESSION
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(address); //FORWARDS TO THE NEXT PAGE
		dispatcher.forward(request, response);
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void addPoints (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		raceId = Integer.parseInt(request.getParameter("raceId"));
		userId = Integer.parseInt(request.getParameter("userId"));
		score = Integer.parseInt(request.getParameter("score"));
				  
		racer.racersScoreDB(raceId, userId, score);
	}
}
