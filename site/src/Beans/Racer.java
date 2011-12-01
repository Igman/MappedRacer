package Beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/******************************************************
 * This is a model class that will set and get any * properties that have to do
 * with the racer objects * that will be stored in the database. A racer is a *
 * user who is associated with a race. *
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
	 * This is a function for a method of adding racers by utilizing an array of
	 * racers names.
	 * 
	 * @param racers
	 *            An array of racers user names.
	 * @param raceID
	 *            The race ID that the racers are in.
	 * @throws SQLException
	 */
	public boolean addRacers(String[] racers, int raceID) throws SQLException {
		boolean success = false;
		for (int k = 0; k < racers.length; k++) {
			addRacer(racers[k], raceID);
		}
		return success;
	}

	/**
	 * This function adds a single racer to the database.
	 * 
	 * @param racers
	 *            The racer's user name.
	 * @param raceID
	 *            The race ID that the racer is in.
	 * @throws SQLException
	 */
	public void addRacer(String racer, int raceID){
		PreparedStatement ps;
		// If user exists, get user ID
		// Else, create user, then get user ID
		
		try {
		int userID = userModel.addUser(racer);
		boolean attend = false;

		ps = c.prepareStatement("INSERT INTO Racers(raceId, userId, attend) VALUES (?,?,?)");

		ps.setInt(1, raceID);
		ps.setInt(2, userID);
		ps.setBoolean(3, attend);

		if (ps.executeUpdate() != 1)
			//throw InsertException ("Failed to insert racer");

		c.commit();
		ps.close();
		}
		catch(SQLException e) {
			
		}
		
	}

	/**
	 * 
	 * @param raceId
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> getRacersInt(int raceId) throws SQLException {
		PreparedStatement ps;
		List<Integer> results = new ArrayList<Integer>();

		ps = c.prepareStatement("SELECT userId FROM Racers WHERE raceId = ?");
		ps.setInt(1, raceId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Integer temp = rs.getInt(1);
			results.add(temp);
		}

		return results;
	}

	public List<RacerObj> getRacersObj(int raceId) throws SQLException {
		List<RacerObj> results = new ArrayList<RacerObj>();
		PreparedStatement ps;

		ps = c.prepareStatement("SELECT r.attend, r.score, u.uid, u.uname FROM Racers r LEFT JOIN Users u ON (r.userid = u.uid) WHERE r.raceid = ?");
		ps.setInt(1, raceId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			RacerObj racerObj = new RacerObj();
			racerObj.setAttend(rs.getBoolean(1));
			racerObj.setScore(rs.getInt(2));
			racerObj.setUserId(rs.getInt(3));
			racerObj.setUserName(rs.getString(4));
			racerObj.setRaceId(raceId);

			results.add(racerObj);
		}

		return results;
	}

	public boolean getAttend(int raceId, int userId) throws SQLException {
		PreparedStatement ps;
		boolean result = false;

		ps = c.prepareStatement("SELECT Attend FROM Racers WHERE RaceId = ? AND UserId = ?");
		ps.setInt(1, raceId);
		ps.setInt(2, userId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			result = rs.getBoolean(1);
		}

		return result;
	}

	public int getScore(int userID, int raceID) throws SQLException {
		PreparedStatement ps;
		int result = 0;

		ps = c.prepareStatement("SELECT score FROM Racers WHERE userID = ? AND raceID = ?");

		ps.setInt(1, userID);
		ps.setInt(2, raceID);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			result = rs.getInt(1);
		}

		return result;
	}

	public void setScore(int userID, int raceID, int score) throws SQLException {
		PreparedStatement ps;

		ps = c.prepareStatement("UPDATE Racers SET score = ? WHERE userID = ? AND raceID = ?");

		ps.setInt(1, score);
		ps.setInt(2, userID);
		ps.setInt(3, raceID);

		ps.executeUpdate();

		c.commit();
		ps.close();
	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		/*
		 * User user = new User(); User user2 = new User(); Race race = new
		 * Race(); Racer racer = new Racer(); int userID = user.addUser("Joe");
		 * int userID2 = user2.addUser("Bob"); int raceID =
		 * race.createRace("Race Madness", Calendar.getInstance(), userID); int
		 * raceID2 = race.createRace("Race Insanity", Calendar.getInstance(),
		 * userID); racer.addRacer("Bob", raceID); racer.updateScore(userID2,
		 * raceID, 100); System.out.println("Bob's score: " +
		 * racer.getScore(userID2, raceID)); System.out.println("Race ID 1: " +
		 * raceID); System.out.println("Race ID 2: " + raceID2);
		 */
	}
}