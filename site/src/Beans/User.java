package Beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/******************************************************
 * This is a model class that will set and get any * properties that have to do
 * with the user objects * that will be stored in the database. *
 ******************************************************/

public class User {
	private Connection c;

	/**
	 * This function is the constructor which will set up the connection
	 * required to communicate with the DB.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public User() throws ClassNotFoundException, SQLException {
		c = Conn.getInstance().getConnection();
	}

	/**
	 * This function is used to get a user's ID. If it is -1 then that means the
	 * user ID was not found in the DB.
	 * 
	 * @param userName
	 *            The user's user name.
	 * @return The user's user ID.
	 * @throws SQLException
	 */
	public int getUserID(String userName) throws SQLException {
		PreparedStatement ps;
		ResultSet rs;
		int userID = -1;

		ps = c.prepareStatement("SELECT uid FROM Users WHERE uname = ?");

		ps.setString(1, PrepString(userName));
		rs = ps.executeQuery();
		if (rs.next()) {
			userID = rs.getInt(1);
		}

		return userID;
	}
	
	/**
	 * This function is used to get a user's name. If it is "" then that means the
	 * user ID was not found in the DB.
	 * 
	 * @param userID
	 *            The user's user id.
	 * @return The user's user name.
	 * @throws SQLException
	 */
	public String getUserName(int userId) throws SQLException {
		PreparedStatement ps;
		ResultSet rs;
		String userName = "";

		ps = c.prepareStatement("SELECT uname FROM Users WHERE uid = ?");

		ps.setInt(1, userId);
		rs = ps.executeQuery();
		if (rs.next()) {
			userName = rs.getString(1);
		}

		return userName;
	}

	/**
	 * This function is used to add a user to the database. It first checks if
	 * the user is in the database already meaning it will not need to add. It
	 * will return the user's ID in the end.
	 * 
	 * @param userName
	 *            The user's user name.
	 * @return The user's user ID.
	 * @throws SQLException
	 */
	public int addUser(String userName) throws SQLException {
		int userID = getUserID(PrepString(userName));

		if (userID == -1) {
			PreparedStatement ps;

			ps = c.prepareStatement("INSERT INTO Users(uname) VALUES (?)");
			ps.setString(1, userName);

			ps.executeUpdate();
			userID = getUserID(userName);

			c.commit();
		}

		return userID;
	}

	/**
	 * A function that utilizes an alternative way of adding users to the
	 * database more efficiently by accepting in an array of user names. Will
	 * not return the user IDs.
	 * 
	 * @param users
	 *            Array of users names.
	 * @throws SQLException
	 */
	public void addUsers(String[] users) throws SQLException {
		for (int k = 0; k < users.length; k++) {
			addUser(users[k]);
		}
	}

	/**
	 * 
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> getRaces(int userId) throws SQLException {
		PreparedStatement ps;
		List<Integer> results = new ArrayList<Integer>();

		ps = c.prepareStatement("SELECT RaceId FROM Race WHERE userId = ?");
		ps.setInt(1, userId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Integer temp = rs.getInt(1);
			results.add(temp);
		}

		return results;
	}
	
	private String PrepString(String username) {
		if (!username.startsWith("@")) {
			username = "@" + username;
		}
		return username.toLowerCase();
	}
}
