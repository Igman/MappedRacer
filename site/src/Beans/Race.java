package Beans;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.PreparedStatement;
import java.util.Calendar;

/******************************************************
 * This is a model class that will set and get any    *
 * properties that have to do with the race objects   *
 * that will be stored in the database.				  *
 ******************************************************/

public class Race {
	private Connection c;
	
	/**
	 * This function is the one that will initialize the race and store it
	 * in the database. It will also get the race's ID.
	 * 
	 * @param name			The name of the race.
	 * @param dateTime		The start date/time.
	 * @param creator		The creator's user ID.
	 * @return				The race ID.
	 * @throws SQLException
	 */
	public int createRace(String name, Calendar dateTime, int creator) throws SQLException {
		PreparedStatement ps;
		
		// Adds the new race to the database
		ps = c.prepareStatement("INSERT INTO Race VALUES (?,?,?,?,?)");
		
		ps.setInt(1, creator);
		ps.setString(2, name);
		ps.setDate(4, new Date(dateTime.getTime().getTime()));
		ps.setTime(5, new Time(dateTime.getTime().getTime()));
		
		c.commit();
		
		// Retrieves the race id of the created race.
		ps = c.prepareStatement("SELECT MAX(RaceId) FROM Race");
		
		ResultSet rs = ps.executeQuery();
		ps.close();
		
		rs.next();
		int raceID = rs.getInt(1);
		
		return raceID;
	}
}