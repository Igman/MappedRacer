package Controller;

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
import twitter4j.conf.ConfigurationBuilder;

public class LoginController extends HttpServlet{

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("AAta0WC9XNVhiq6jm5O7w").setOAuthConsumerSecret("jpzpbeRb4P2LE4Dq5N9soZwClqHPVIJ7zSJrfkC1f4");
		TwitterFactory tf = new TwitterFactory(cb.build());
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