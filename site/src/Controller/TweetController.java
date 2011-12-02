/*
 * Author: Ignacio Rodriguez
 * This is based largely off the sample code found at https://github.com/yusuke/sign-in-with-twitter
 */

package Controller;

import java.io.IOException;
import java.util.Collection;

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

public class TweetController extends HttpServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String text = request.getParameter("text");
        Twitter twitter = (Twitter)request.getSession().getAttribute("twitter");
        postTweet(text, twitter);
	}

	public static void postTweet(String text, Twitter twitter)
			throws ServletException {
		try {
            twitter.updateStatus(text);
        } catch (TwitterException e) {
            throw new ServletException(e);
        }
	}
	


}
