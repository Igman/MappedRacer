package Beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		c = Conn.getInstance().getConnection();
		
		userModel = new User();
	}
	
	/**
	 * This is a function for a method of adding racers by utilizing an
	 * array of racers names.
	 * 
	 * @param racers		An array of racers user names. 
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
		//If user exists, get user ID
		//Else, create user, then get user ID
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
	
	public void updateScore(int userID, int raceID, int score) throws SQLException {
		PreparedStatement ps;
		
		ps = c.prepareStatement("UPDATE Racer SET score = score + ? WHERE userID = ? AND raceID = ?");
		
		ps.setInt(1, score);
		ps.setInt(2, userID);
		ps.setInt(3, raceID);
		
		ps.executeUpdate();
		
		c.commit();
		ps.close();
	}
	
	public int getScore(int userID, int raceID) throws SQLException {
		PreparedStatement ps;
		int result = 0;
		
		ps = c.prepareStatement("SELECT score FROM Racer WHERE userID = ? AND raceID = ?");
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			result = rs.getInt(6);
		}
		
		return result;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		User user = new User();
		Racer racer = new Racer();
		int userID = user.addUser("Joe");
		racer.addRacer("Joe", 1);
		racer.updateScore(userID, 1, 100);
		System.out.println(racer.getScore(userID, 1));
	}
}