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
	public void addRacers(String[] racers, int raceID) throws SQLException {
		for (int k = 0; k < racers.length; k++) {
			addRacer(racers[k], raceID);
		}
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
	public void addRacer(String racer, int raceID) throws SQLException {
		// TODO: Is this where we should keep the default values for the racers?
		PreparedStatement ps;
		// If user exists, get user ID
		// Else, create user, then get user ID
		int userID = userModel.addUser(racer);
		boolean attend = false;
		Time totalTime = new Time(0);
		int place = 0;

		ps = c.prepareStatement("INSERT INTO Racers(raceId, userId, attend, totalTime) VALUES (?,?,?,?)");

		ps.setInt(1, raceID);
		ps.setInt(2, userID);
		ps.setBoolean(3, attend);
		ps.setTime(4, totalTime);

		ps.executeUpdate();

		c.commit();
		ps.close();
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

		List<Integer> racers = getRacersInt(raceId);
		Iterator<Integer> itr = racers.iterator();
		while (itr.hasNext()) {
			int racer = itr.next();

			RacerObj temp = new RacerObj();
			temp.setAttend(getAttend(raceId, racer));
			temp.setTotalTime(getTotalTime(raceId, racer));
			temp.setScore(getScore(raceId, racer));

			results.add(temp);
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

	public Time getTotalTime(int raceId, int userId) throws SQLException {
		PreparedStatement ps;
		Time result = new Time(0);

		ps = c.prepareStatement("SELECT TotalTime FROM Racers WHERE RaceId = ? AND UserId = ?");
		ps.setInt(1, raceId);
		ps.setInt(2, userId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			result = rs.getTime(1);
		}

		return result;
	}

	public void setTotalTime(int raceId, int userId, Time totalTime)
			throws SQLException {
		PreparedStatement ps;

		ps = c.prepareStatement("UPDATE Racers SET TotalTime = ? WHERE raceID = ? AND userID = ?");

		ps.setTime(1, totalTime);
		ps.setInt(2, raceId);
		ps.setInt(3, userId);

		ps.executeUpdate();

		c.commit();
		ps.close();
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