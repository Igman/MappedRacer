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

public class TwitterSearchController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Twitter twitter = (Twitter)request.getSession().getAttribute("twitter");
		String raceId = request.getParameter("raceId");
		String responseString;
		try {
			responseString = getRaceTweets(Integer.parseInt(raceId), twitter);
			PrintWriter out = response.getWriter();
			out.println(responseString);
		} catch (NumberFormatException e) {
			throw new ServletException(e);
		} catch (SQLException e) {
			throw new ServletException(e);
		}

	}
	
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
			userIds.add(racer.getUserName());
		}
		return search(userIds, twitter);
	}
	
	private static String search(Collection<String> userNames, Twitter twitter) throws ServletException{
		StringBuilder queryString = new StringBuilder();
		for(String userName : userNames){
			queryString.append("from:" + userName + " OR ");
		}
		queryString.replace(queryString.lastIndexOf("O"), queryString.length(), "");
		QueryResult results = null;
		try {
			results = twitter.search(new Query(queryString.toString()));
		} catch (TwitterException e) {
            throw new ServletException(e); 
		}
		for (Tweet tweet : results.getTweets()) {
	        System.out.println(tweet.getFromUser() + ":" + tweet.getText());
	    }
		return results.toString();
	}

}
