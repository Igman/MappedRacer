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
		ps = c.prepareStatement("INSERT INTO Race(Name, Start, CreatorID,FINISHED) VALUES (?,?,?,0)");

		ps.setString(1, name);
		java.sql.Timestamp temp = new java.sql.Timestamp(dateTime.getTimeInMillis());
		ps.setTimestamp(2, temp);
		ps.setInt(3, creator);

		// rowCount should be 1. Throw error otherwise
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
	
	/**
	 * 
	 * @param raceId
	 * @param creatorId
	 * @return
	 * @throws SQLException
	 */
	public RaceObj getRace(int raceId, int creatorId)
			throws SQLException {
		PreparedStatement ps;
		RaceObj raceObj = new RaceObj();

		ps = c.prepareStatement("SELECT name, start FROM race WHERE ID = ? AND creator = ?");
		ps.setInt(1, raceId);
		ps.setInt(2, creatorId);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			raceObj.setName(rs.getString(1));
			raceObj.setStart(rs.getTimestamp(2));
		}
		
		raceObj.setCreatorId(creatorId);
		raceObj.setId(raceId);
		return raceObj;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public List<RaceObj> getRacesObj(int userId, boolean isFinished) throws SQLException {
		List<RaceObj> results = new ArrayList<RaceObj>();
		PreparedStatement ps;

		ps = c.prepareStatement("SELECT r.id, r.name, r.start, r.creatorid, rs.score FROM Race r LEFT JOIN Racers rs ON (r.id = rs.raceID) WHERE userID = ? AND rs.attend = 1 AND r.finished = ? ORDER BY r.start DESC");
		ps.setInt(1, userId);
		ps.setBoolean(2, isFinished);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			RaceObj raceObj = new RaceObj();

			raceObj.setId(rs.getInt(1));
			raceObj.setName(rs.getString(2));
			raceObj.setStart(rs.getTimestamp(3));
			raceObj.setCreatorId(rs.getInt(4));
			raceObj.setScore(rs.getInt(5));
			results.add(raceObj);
		}

		return results;
	}
	
	public void deleteRace(int raceId) throws SQLException {
		PreparedStatement ps;
		
		ps = c.prepareStatement("DELETE * FROM Race WHERE id = ?");
		ps.setInt(1, raceId);
		
		ps.executeUpdate();
		
		c.commit();
		ps.close();
	}

	public String getName(int raceId) throws SQLException {
		PreparedStatement ps;
		String result = "";
		
		ps = c.prepareStatement("SELECT name FROM Race WHERE ID = ?");
		ps.setInt(1, raceId);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			result = rs.getString(1);
		}
		
		return result;
	}
	
	public void setFinished(int raceId, boolean status) throws SQLException {
		PreparedStatement ps;
		
		ps = c.prepareStatement("UPDATE Race SET finished = ? WHERE ID = ?");
		ps.setBoolean(1, status);
		ps.setInt(2, raceId);
		
		//throw error if fail
		int rows = ps.executeUpdate();
		
		c.commit();
		ps.close();
	}
}