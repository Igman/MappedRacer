package Beans;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/******************************************************
 * This is a model class that will set and get any * properties that have to do
 * with the race objects * that will be stored in the database. *
 ******************************************************/

public class Race {
	private Connection c;

	/**
	 * This function is the constructor which will set up the connection
	 * required to communicate with the DB.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Race() throws ClassNotFoundException, SQLException {
		c = Conn.getInstance().getConnection();
	}

	/**
	 * This function is the one that will initialize the race and store it in
	 * the database. It will also get the race's ID.
	 * 
	 * @param name
	 *            The name of the race.
	 * @param dateTime
	 *            The start date/time.
	 * @param creator
	 *            The creator's user ID.
	 * @return The race ID.
	 * @throws SQLException
	 */
	public int createRace(String name, Calendar dateTime, int creator)
			throws SQLException {
		PreparedStatement ps;

		// Adds the new race to the database
		ps = c.prepareStatement("INSERT INTO Race(Name, Start, CreatorID) VALUES (?,?,?)");

		
		ps.setString(1, name);
		ps.setTimestamp(2, new java.sql.Timestamp(dateTime.getTimeInMillis()));
		ps.setInt(3, creator);

		//rowCount should be 1. Throw error otherwise
		int rowCount = ps.executeUpdate();

		// Retrieves the race id of the created race.
		ps = c.prepareStatement("SELECT MAX(ID) FROM Race");

		ResultSet rs = ps.executeQuery();

		int raceID = -1;
		while (rs.next()) {
			raceID = rs.getInt(1);
		}
		ps.close();
		return raceID;
	}
}