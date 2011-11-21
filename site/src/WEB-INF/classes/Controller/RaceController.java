package Controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.beans.*;
// What the heck was
//import Beans.*;

/**
 * 
 */
public class RaceController extends HttpServlet {
	
	
	private Connection connection = null;
	private String address = "/WEB-INF/JSPs/map.jsp";
	
	private final int CREATE_RACE = 1;
	private final int ADD_RACER = 2;
	private final int ADD_ITEM = 3;
	private final int END_RACE = 4;
	private final int CHECK_IN = 5;
	
	/**
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Parse values from the request to determine type of request.
		// Switch statement that checks first parameter and acts accordingly
		int requestType = Integer.parseInt(request.getParameter("request_type"));
		
		switch (requestType) {
		case CREATE_RACE:	createRace();	break;
		case ADD_RACER:		addRacer();		break;
		case ADD_ITEM:		addItem();		break;
		case END_RACE:
			endRace();
			break;
		case CHECK_IN:
			checkIn();
			break;
		default: error();
		break;
		}
		
		
		
		
		// Adds the change into the session to be passed.
		request.setAttribute("items",items);
		
		// Determines the next page for that request
		RequestDispatcher dispatcher = request.getRequestDispatcher(address); 
		dispatcher.forward(request, response);
	}
	
	private void createRace(HttpServletRequest request) {
		String creatorID = request.getParameter("creator_id");
		String startTime = request.getParameter("start_time");
		String endPoint = request.getParameter("end_point");
		String name = request.getParameter("name");
		
		// Calls the model to create the race and returns the race id
		
		
	}
	
	private void addRacer(HttpServletRequest request) {
		String raceID = request.getParameter("creator_id");
		String userID = request.getParameter("start_time");
		
		
		
		
		
	}
	
	private void addItem() {
		
	}
	
	private void endRace() {
		
		
	}
	
	private void checkIn() {
		
	}
	
	private void error() {
		
	}
						
}
