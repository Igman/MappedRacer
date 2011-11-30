/*
 * Author: Ignacio Rodriguez
 * This is based largely off the sample code found at https://github.com/yusuke/sign-in-with-twitter
 */

package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterCallbackServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
		RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
		HttpSession session = request.getSession();
		String verifier = request.getParameter("oauth_verifier");
		try{
			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
			setupUser(accessToken);
			session.removeAttribute("requestToken");
		}catch (TwitterException e) {
            throw new ServletException(e);
        }
        response.sendRedirect(request.getContextPath() + "/");
	}

	private void setupUser(AccessToken accessToken) {
		// TODO Auto-generated method stub
		
	}
}
