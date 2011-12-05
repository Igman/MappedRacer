package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import Beans.Racer;
import Beans.RacerObj;

/**
 * This is controller designed to search for all the tweets from users in volved in a given race
 * 
 * @author Ignacio Rodirguez
 *
 */
public class TwitterSearchController extends HttpServlet {
	
	/**
	 * Responds with a JSON object representing all the tweets from the users in a given race
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Twitter twitter = (Twitter)request.getSession().getAttribute("twitter");
		String raceId = request.getParameter("raceId");
		String responseString;
		try {
			responseString = getRaceTweets(Integer.parseInt(raceId), twitter);
			PrintWriter out = response.getWriter();
			System.out.println(responseString);
			out.println(responseString);
		} catch (NumberFormatException e) {
			throw new ServletException(e);
		} catch (SQLException e) {
			throw new ServletException(e);
		}

	}
	
	/**
	 * Searches for all tweets from users in a given race
	 * 
	 * @param raceId - The race id to search for
	 * @param twitter - A twitter session to use
	 * @return A JSON object with tweets from the rquested users
	 * @throws ServletException
	 * @throws SQLException
	 */
	public static String getRaceTweets(int raceId,Twitter twitter) throws ServletException, SQLException{
		Racer racerController;
		try {
			racerController = new Racer();
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
		List<RacerObj> racers = racerController.getRacersObj(raceId);
		Collection<String> userIds = new HashSet();
		for(RacerObj racer : racers){
			userIds.add(racer.getUserName().replace("@",""));
		}
		return search(userIds, twitter);
	}
	
	/**
	 * Helper method to search for tweets from a collection of users
	 * 
	 * @param userNames The usernames to search twitter for
	 * @param twitter The twittersession to use
	 * @return A Json object representing the last 3 resulting tweets
	 * @throws ServletException
	 */
	private static String search(Collection<String> userNames, Twitter twitter) throws ServletException{
		StringBuilder queryString = new StringBuilder();
		for(String userName : userNames){
			queryString.append("from:" + userName + " OR ");
		}
		queryString.replace(queryString.lastIndexOf("O"), queryString.length(), "");
		QueryResult results = null;
		try {
			Query query = new Query(queryString.toString());
			query.setRpp(3);
			query.setPage(1);
			results = twitter.search(query);
		} catch (TwitterException e) {
            throw new ServletException(e); 
		}
		return results.toString();
	}

}
