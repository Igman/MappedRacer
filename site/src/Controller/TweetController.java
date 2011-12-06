/**
 * A controller class responsible for posting tweets through the currently logged in user
 * 
 * Author: Ignacio Rodriguez
 * This is based largely off the sample code found at https://github.com/yusuke/sign-in-with-twitter
 */

package Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TweetController extends HttpServlet{
	private static final long serialVersionUID = 1L;

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String text = request.getParameter("text");
        Twitter twitter = (Twitter)request.getSession().getAttribute("twitter");
        postTweet(text, twitter);
	}

	/**
	 * Posts a tweet using a given twitter session
	 * 
	 * @param text - The text to tweet
	 * @param twitter - The twitter session to use
	 * @throws ServletException
	 */
	public static void postTweet(String text, Twitter twitter)
			throws ServletException {
		try {
            twitter.updateStatus(text);
        } catch (TwitterException e) {
            throw new ServletException(e);
        }
	}
	


}
