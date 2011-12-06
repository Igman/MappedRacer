package Beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Exceptions.InsertException;
import Exceptions.SelectException;

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
	 * Adds a string of racers to the database.
	 * 
	 * This method adds multiple racers by utilizing an array of racers names
	 * and adding each name individually.
	 * 
	 * @param racers
	 *            An array of racers user names.
	 * @param raceID
	 *            The race ID that the racers are in.
	 * @throws InsertException
	 *             Thrown if any of the racers fail to be inserted into the
	 *             database.
	 */
	public boolean addRacers(String[] racers, int raceID)
			throws InsertException {
		boolean success = false;
		for (int k = 0; k < racers.length; k++) {
			addRacer(racers[k], raceID);
		}
		return success;
	}

	/**
	 * Adds a single racer to the database.
	 * 
	 * This function takes in a user's name and a race ID and adds that user to
	 * the database as a racer. If that user does not exist, an account is
	 * created for him in the user database.
	 * 
	 * @param racers
	 *            The racer's user name.
	 * @param raceID
	 *            The race ID that the racer is in.
	 * @throws InsertException
	 * @throws SQLException
	 */
	public void addRacer(String username, int raceID) throws InsertException {
		PreparedStatement ps;
		// If user exists, get user ID
		// Else, create user, then get user ID

		try {
			username = PrepString(username);
			int userID = userModel.addUser(username);
			boolean attend = false;

			ps = c.prepareStatement("INSERT INTO Racers(raceId, userId, attend) VALUES (?,?,?)");

			ps.setInt(1, raceID);
			ps.setInt(2, userID);
			ps.setBoolean(3, attend);

			if (ps.executeUpdate() != 1)
				throw new InsertException("Failed to insert racer.");

			c.commit();
			ps.close();
		} catch (SQLException e) {
			throw new InsertException("Database error on racer insertion.");
		}

	}

	/**
	 * Get a list of racer IDs for the given race.
	 * 
	 * Given a race ID, this returns a list of Integer objects of all the racer
	 * IDs.
	 * 
	 * @param raceId
	 * @return List<Integer> of race IDs
	 * @throws SelectException
	 */
	public List<Integer> getRacersInt(int raceId) throws SelectException {
		PreparedStatement ps;
		List<Integer> results = new ArrayList<Integer>();
		ResultSet rs = null;

		try {
			ps = c.prepareStatement("SELECT userId FROM Racers WHERE raceId = ?");
			ps.setInt(1, raceId);

			rs = ps.executeQuery();
		} catch (SQLException e) {
			throw new SelectException(
					"Could not find the selected userId from raceId: " + raceId);
		}

		try {
			while (rs.next()) {
				Integer temp = rs.getInt(1);
				results.add(temp);
			}
		} catch (SQLException e) {
			throw new SelectException("Returned ResultSet was invalid");
		}

		return results;
	}

	/**
	 * Returns a list of RacerObj for the given race.
	 * 
	 * Given a race ID, this returns a list of RacerObj objects of all the
	 * racers in the race.
	 * 
	 * @param raceId
	 * @return List<RacerObj> of racers
	 * @throws SQLException
	 */
	public List<RacerObj> getRacersObj(int raceId) throws SQLException {
		List<RacerObj> results = new ArrayList<RacerObj>();
		PreparedStatement ps;

		ps = c.prepareStatement("SELECT r.attend, r.score, u.uid, u.uname FROM Racers r LEFT JOIN Users u ON (r.userid = u.uid) WHERE r.raceid = ? ORDER BY score DESC");
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

	/**
	 * Getter method for the attend status of a racer.
	 * 
	 * @param raceId
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Setter method for the attend status of a racer.
	 * 
	 * @param raceId
	 * @param userId
	 * @param attend
	 * @throws SQLException
	 */
	public void setAttend(int raceId, int userId, boolean attend)
			throws SQLException {
		PreparedStatement ps;

		ps = c.prepareStatement("UPDATE Racers SET Attend = ? WHERE RaceId = ? AND UserId = ?");
		ps.setBoolean(1, attend);
		ps.setInt(2, raceId);
		ps.setInt(3, userId);

		ps.executeUpdate();

		c.commit();
		ps.close();
	}

	/**
	 * Getter method for the score of a racer.
	 * 
	 * @param userID
	 * @param raceID
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Setter method for the score of a racer.
	 * 
	 * @param userID
	 * @param raceID
	 * @param score
	 * @throws SQLException
	 */
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

	/**
	 * Update method for the score of a racer
	 * 
	 * This method adds score to the racer's total score.
	 * 
	 * @param userID
	 * @param raceID
	 * @param score
	 * @throws SQLException
	 */
	public void updateScore(int userID, int raceID, int score)
			throws SQLException {
		PreparedStatement ps;

		ps = c.prepareStatement("UPDATE Racers SET score = score + ? WHERE userID = ? AND raceID = ?");

		ps.setInt(1, score);
		ps.setInt(2, userID);
		ps.setInt(3, raceID);

		ps.executeUpdate();

		c.commit();
		ps.close();
	}

	private String PrepString(String username) {
		if (!username.startsWith("@")) {
			username = "@" + username;
		}
		return username.toLowerCase();
	}
	
	public int getRacerRank(int userID, int raceID) throws SQLException{
		PreparedStatement ps;
		int rank = -1;

		ps = c.prepareStatement("SELECT (SELECT count(*) FROM racers r2 WHERE r2.score > r.score AND r2.raceID = ?) + 1 AS rank FROM racers r WHERE r.raceid = ? AND userid = ?");
		ps.setInt(1, raceID);
		ps.setInt(2, raceID);
		ps.setInt(3, userID);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			rank = rs.getInt(1);
		}

		return rank;
	}
}