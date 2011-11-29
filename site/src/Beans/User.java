package Beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/******************************************************
 * This is a model class that will set and get any    *
 * properties that have to do with the user objects   *
 * that will be stored in the database.				  *
 ******************************************************/

public class User {
	private Connection c;
	
	/**
	 * This function is used to get a user's ID. If it is -1 then
	 * that means the user ID was not found in the DB.
	 * 
	 * @param userName		The user's user name.
	 * @return				The user's user ID.
	 * @throws SQLException
	 */
	public int getUserID(String userName) throws SQLException {
		PreparedStatement ps;
		ResultSet rs;
		int userID = -1;
		
		ps = c.prepareStatement("SELECT uid FROM User WHERE uname = ?");
		rs = ps.executeQuery();
		if (rs.next()) {
			userID = rs.getInt(1);
		}
		
		return userID;
	}
	
	/**
	 * This function is used to add a user to the database. It first
	 * checks if the user is in the database already meaning it will
	 * not need to add. It will return the user's ID in the end.
	 * 
	 * @param userName		The user's user name.
	 * @return				The user's user ID.
	 * @throws SQLException
	 */
	public int addUser(String userName) throws SQLException {
		int userID = getUserID(userName);
		
		if (userID == -1) {
			PreparedStatement ps;
			
			ps = c.prepareStatement("INSERT INTO User(uname) VALUES (?)");
			ps.setString(1, userName);		
			
			ps.executeUpdate();
			userID = getUserID(userName);
			
			c.commit();
		}
		
		return userID;
	}
	
	/**
	 * A function that utilizes an alternative way of adding users
	 * to the database more efficiently by accepting in an array
	 * of user names. Will not return the user IDs.
	 * 
	 * @param users			Array of users names.
	 * @throws SQLException
	 */
	public void addUsers(String[] users) throws SQLException {
		for (int k = 0; k < users.length; k++) {
			addUser(users[k]);
		}
	}
}
