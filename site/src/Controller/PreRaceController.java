package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.Race;
import Beans.RaceObj;

/**
 * Servlet implementation class PreRaceController
 */
public class PreRaceController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Race raceModel;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PreRaceController() {
        super();
        
        try {
			raceModel = new Race();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");
        PrintWriter out = response.getWriter();
        
        // Gets the race ID from the request
		int userID = Integer.parseInt(request.getParameter("user_id"));	
		
		try {
			// Adds list of races to JSON
			String json = getJSONRaceList(userID);
			
			System.out.println(json);
			out.println(json);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			out.close();
		}		
	}

	/**
	 * Will just pass request and response on to doGet.
	 * 
	 * @param request	The request which contains the JSON
	 * @param response	The response that will be passed back.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private String getJSONRaceList(int userID) throws SQLException {
		List<RaceObj> races = raceModel.getRacesObj(userID, false);
		Iterator<RaceObj> iterator = races.iterator();
		StringBuffer json = new StringBuffer("{races:[");
		
		RaceObj temp;
		
		while (iterator.hasNext()) {
			temp = iterator.next();
			
			json.append("{raceID:\""+temp.getId()+"\", creatorID:\""+temp.getCreatorId()+"\", name:\""+temp.getName()+ "\", time:\""+ temp.getStart().toString() + "\", score:\""+ temp.getScore() + "\"},");
		}
		if(races.isEmpty()){
			json.append("]");
		}else{
			// replace last character with ]
			json.setCharAt( json.length()-1,']');
		}
		
		json.append("}");
        
		return json.toString();
	}


}
