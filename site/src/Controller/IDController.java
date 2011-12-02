package Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IDController {

	private static final long serialVersionUID = 1L;

	public IDController() {

	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		int userId = -1;
		int raceId = -1;

		if (Integer.parseInt(request.getParameter("user")) == 1) {
			userId = Integer.parseInt((String) request.getSession()
					.getAttribute("userId"));
		}

		if (Integer.parseInt(request.getParameter("race")) == 1) {
			raceId = Integer.parseInt((String) request.getSession()
					.getAttribute("raceId"));
		}
		
		out.println("(" + userId + "," + raceId + ")");

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
