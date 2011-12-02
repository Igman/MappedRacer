package Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Twitter;
import Beans.Racer;

public class JoinRaceController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Twitter twitter = (Twitter)request.getSession().getAttribute("twitter");
		String raceId = request.getParameter("raceId");
		try {
			Racer racer = new Racer();
			racer.addRacer(twitter.getScreenName(), Integer.parseInt(raceId));
		} catch (Exception e) {
			throw new ServletException(e);
		} 
		response.sendRedirect(request.getContextPath() + "/usr_home.html");
	}
}
