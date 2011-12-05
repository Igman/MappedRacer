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


/******************************************************
 * This is that class responsible for handling the    *
 * logins by the program. It very reliant on twitter  *	
 ******************************************************/
public class LoginController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Simple constructor that calls it super.
	 */
	public LoginController() {
		super();
	}
	
	/**
	 * This is the do get where the requests to the controller will come in. This function
	 * is directly responsible for handling the requests and parsing the json object that
	 * will be sent in.
	 * 
	 * @param request	The request containing the json object.
	 * @param response	The response that will be used to send back the status.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// Sets the config required to access twitter.
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("AAta0WC9XNVhiq6jm5O7w").setOAuthConsumerSecret("jpzpbeRb4P2LE4Dq5N9soZwClqHPVIJ7zSJrfkC1f4");
		
		// Sets up the twitter factory.
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
		// Adds twitter to the session.
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
	
	/**
	 * Will just pass request and response on to doGet.
	 * 
	 * @param request	The request which contains the JSON
	 * @param response	The response that will be passed back.
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}