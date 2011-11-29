package Beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

/******************************************************
 * This is a model class that will set and get any    *
 * properties that have to do with the racer objects  *
 * that will be stored in the database. A racer is a  *
 * user who is associated with a race.				  *
 ******************************************************/

public class Racer {
	private Connection c;
	private User userModel;
	
	/**
	 * This function is the constructor which will set up the connection
	 * required to communicate with the DB.
	 * 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public Racer() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		c = DriverManager.getConnection("jdbc:mysql://localhost/mappedrace", "test", "");
		
		userModel = new User();
	}
	
	/**
	 * This is a function for a method of adding racers by utilizing an
	 * array of racers names.
	 * 
	 * @param racers		An array of  racers user names. 
	 * @param raceID		The race ID that the racers are in.
	 * @throws SQLException
	 */
	public void addRacers(String[] racers, int raceID) throws SQLException {
		for (int k = 0; k < racers.length; k++) {
			addRacer(racers[k], raceID);
		}
	}
	
	/**
	 * This function adds a single racer to the database.
	 * 
	 * @param racers		The racer's user name. 
	 * @param raceID		The race ID that the racer is in.
	 * @throws SQLException
	 */
	public void addRacer(String racer, int raceID) throws SQLException {
		//TODO: Is this where we should keep the default values for the racers?
		PreparedStatement ps;
		int userID = userModel.addUser(racer);
		boolean attend = false;
		Time totalTime = new Time(0);
		int place = 0;
		
		ps = c.prepareStatement("INSERT INTO Racer VALUES (?,?,?,?,?)");
		
		ps.setInt(1, raceID);
		ps.setInt(2, userID);
		ps.setBoolean(3, attend);
		ps.setTime(4, totalTime);
		ps.setInt(5, place);
		
		ps.executeUpdate();

		c.commit();
		ps.close();
	}
}