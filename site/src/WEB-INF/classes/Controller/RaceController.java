package Controller;

import java.io.*;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.servlet.*;
import javax.servlet.http.*;

import Beans.Race;

/**
 * This class is responsible for handling any requests that directly relate to
 * setting up and running the game.
 * 
 * @author Adam
 */
public class RaceController extends HttpServlet {
	private final int CREATE_RACE = 1;
	private final int ADD_RACER = 2;
	private final int ADD_ITEM = 3;
	private final int END_RACE = 4;
	private final int CHECK_IN = 5;
	private final int CHECK_RACE = 6;
	
	private Race race = new Race();
	private RacerController racers = new RacerController();
	private CheckInController checkIn = new CheckInController();
	
	/**
	 * This is the doGet function which will be called when a request is made. This function is
	 * used as the meditator that will pass along the task to handle the specific request types 
	 * since there are many possibility functionality that is requested.
	 * 
	 * @param request		Request obj containing parameters passed and session returned.
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Parse values from the request to determine type of request.
		// Switch statement that checks first parameter and acts accordingly
		int requestType = Integer.parseInt(request.getParameter("request_type"));
		switch (requestType) {
		case 	CREATE_RACE:	createRace(request, response);	break;
		case 	ADD_RACER:		addRacer(request, response);	break;
		case 	ADD_ITEM:		addItem(request, response);		break;
		case 	END_RACE:		endRace(request, response);		break;
		case 	CHECK_IN:		checkIn(request, response);		break;
		case	CHECK_RACE:		checkRace(request, response);	break;
		default: 				error("Unknown request", request, response);
		}
	}
	
	/**
	 * This function will handle the request that initialises the race that the user is
	 * setting up. At this stage the user will have set the date, name, etc. It will
	 * Initialise the representation of the race in the DB.
	 * 
	 * @param request		Request obj containing parameters passed and session returned.
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void createRace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Parses all of the parameters out of the request.
		int userID = Integer.parseInt(request.getParameter("user_id"));
		Date startTime;
		// There can be errors if the string is not formatted correctly by the UI.
		try {
			startTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH).parse(request.getParameter("start_time"));
		} catch (ParseException e) {
			e.printStackTrace();
			
			error("Date not formated correctly.", request, response);
			return;
		}
		String endPoint = request.getParameter("end_point");
		String name = request.getParameter("name");
		Date createDate = new Date();
		ArrayList items = (ArrayList)request.getAttribute("items");
		ArrayList racers = (ArrayList)request.getAttribute("racers");
		
		// Calls the model to create the race and returns the race id
		int raceID = race.createRace(name, endPoint, startTime, createDate, userID);
		
		boolean successful = addRacers(racers, raceID);
		boolean successful2 = addItems(items, raceID);
		
		if (raceID != -1 && successful) {
			// Determines the next page.
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/JSPs/add_items.jsp");
			dispatcher.forward(request, response);
			
			
		} else {
			error("Race was unsuccessfully created.", request, response);
			return;
		}
			
	}
	
	
	private boolean addItems(ArrayList items) {
		// Take list add it to the DB
		for (int k = 0; k < items.size(); k++) {
			addItemitems[k];
		}
	}
	
	private boolean addRacers(ArrayList items) {
		// Take list add new racers to the db
	}
	
	
	// TODO if this is is done all on the same page how will the request work?
	/**
	 * This is where the race creator will add a new user to the race. It passes along the
	 * responsibility to the RacerController. 
	 * 
	 * @param request		Request obj containing parameters passed and session returned.
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addRacer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int raceID = Integer.parseInt(request.getParameter("race_id"));
		int userID = Integer.parseInt(request.getParameter("user_id"));
		
		// Calls racer controller to handle making the new racer.
		boolean successful = racers.addRacer(raceID, userID);
		
		if (successful) {
			// Determines the next page.
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/JSPs/race_created");
			dispatcher.forward(request, response);
		} else {
			error("Racer could not be added.", request, response);
		}
	}
	
	private void addItem(ArrayList items) {
		int points = Integer.parseInt(request.getParameter("points"));
		String location = request.getParameter("location");
		
		
		boolean successful = race.addItem(points, location);
		
		
	}
	
	/**
	 * This ends the race telling the DB that no further checkins will occur.
	 * 
	 * @param request		Request obj containing parameters passed and session returned.
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void endRace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int raceID = Integer.parseInt(request.getParameter("race_id"));
		
		// Sets the db to finished the race so players querying will 
		boolean successful = race.finishRace(raceID);
		//TODO: what else happens when race finishes? we should calculate scores here for the database.
		
		if (successful) {
			// Determines the next page.
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/JSPs/race_results.jsp");
			dispatcher.forward(request, response);
		} else {
			error("Could not end the race.", request, response);
		}	
	}
	
	//TODO: Check in has to determine where it is and if it is at end or at item.
	/**
	 * Check in from a user in a race that will then figure out if the check in is at
	 * the finish line or at a item. It also could include an image or comment.
	 * 
	 * @param request		Request obj containing parameters passed and session returned.
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void checkIn(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		int raceID = Integer.parseInt(request.getParameter("race_id"));
		int userID = Integer.parseInt(request.getParameter("user_id"));
		String picUrl = request.getParameter("pic_url");
		String comment = request.getParameter("comment");
		String location = request.getParameter("location");
		
		//TODO: Not sure the relationship between the two controllers will be here.
		// Checkins jobs will be to determine if the user was in a item or at the finish line.
		// Sets the db to finished the race so players querying will 
		boolean successful = checkIn.userCheckIn(raceID, userID, picUrl, comment, location);
		
		if (successful) {
			// Determines the next page.
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/JSPs/map.jsp");
			dispatcher.forward(request, response);
		} else {
			error("Unable to check in.", request, response);
		} 
	}
	
	/**
	 * The user can check if the race has started.
	 * 
	 * @param request		Request obj containing parameters passed and session returned.
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void checkRace(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		String raceID = request.getParameter("race_id");
		String userID = request.getParameter("user_id");
		
		boolean started = race.checkRace(raceID, userID);
		
		request.setAttribute("started", started);

		// Determines the next page.
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/JSPs/race.jsp");
		dispatcher.forward(request, response);
	}
	
	
	/**
	 * This is how this controller will handle problems that occur with the DB it will
	 * send a message back to UI and tell it to go to the error page. 
	 * 
	 * @param request		Request obj containing parameters passed and session returned.
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void error(String errMsg, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Does nothing tells the view to display that something went wrong.
		// Goes back to current page?
		// Adds the error message.
		request.setAttribute("error_msg", errMsg);
		// Determines the next page.
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/JSPs/error.jsp");
		dispatcher.forward(request, response);
	}
	
	
	private class Item {
		public String location;
		public int value;
		public int type;
	}
	
	private class Racer {
		public int 
	}
}


