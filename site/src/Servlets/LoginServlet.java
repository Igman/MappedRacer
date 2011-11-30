/*
 * Author: Ignacio Rodriguez
 * This is based largely off the sample code found at https://github.com/yusuke/sign-in-with-twitter
 */

package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

public class LoginServlet extends HttpServlet{

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		TwitterFactory tf = new TwitterFactory();
		Twitter twitter = tf.getInstance();
		HttpSession session = request.getSession();
		session.setAttribute("twitter", twitter);
		
		try{
			StringBuffer callbackURL = request.getRequestURL();
			callbackURL.replace(callbackURL.lastIndexOf("/"), callbackURL.length(), "").append("/callback");
			
			RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
			request.getSession().setAttribute("requestToken", requestToken);
			response.sendRedirect(requestToken.getAuthenticationURL());
		}catch (TwitterException e) {
            throw new ServletException(e);
        }
	}
}
