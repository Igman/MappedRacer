package Beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**********************************************
 * Race class that contains all attributes * a race may contain and is mapped to
 * the DB *
 **********************************************/

public class CheckIn {
	private Connection c;

	public CheckIn() {
		c = Conn.getInstance().getConnection();
	}

	public void addCheckIn(int userId, int raceId, String picture,
			String comment, String geoLocation) {
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("INSERT INTO CheckIn(userid, raceid, picture, comment, geolocation) VALUES (?,?,?,?,?)");

			ps.setInt(1, userId);
			ps.setInt(2, raceId);
			ps.setString(3, picture);
			ps.setString(4, comment);
			ps.setString(5, geoLocation);
			
			ps.executeUpdate();
			c.commit();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addCheckIn(CheckInObj checkInObj) {
		addCheckIn(checkInObj.getUserID(), checkInObj.getRaceID(),
				checkInObj.getPic(), checkInObj.getComment(),
				checkInObj.getLocation());
	}

	public List<Integer> getCheckInIDs(int raceId) throws SQLException {
		PreparedStatement ps;
		List<Integer> results = new ArrayList<Integer>();

		ps = c.prepareStatement("SELECT ID FROM CheckIn WHERE raceId = ?");
		ps.setInt(1, raceId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Integer temp = rs.getInt(1);
			results.add(temp);
		}

		return results;
	}

	public List<CheckInObj> getCheckInObjects(int raceId) throws SQLException {
		List<CheckInObj> results = new ArrayList<CheckInObj>();
		PreparedStatement ps;

		ps = c.prepareStatement("SELECT id, userid, picture, comment, geolocation FROM CheckIn WHERE raceid = ?");
		ps.setInt(1, raceId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			CheckInObj checkInObj = new CheckInObj();
			checkInObj.setID(rs.getInt(1));
			checkInObj.setUserID(rs.getInt(2));
			checkInObj.setPic(rs.getString(3));
			checkInObj.setComment(rs.getString(4));
			checkInObj.setLocation(rs.getString(5));
			checkInObj.setRaceID(raceId);

			results.add(checkInObj);
		}

		return results;
	}
	
	public int getUserId(int checkInId) throws SQLException {
		PreparedStatement ps;
		int result = -1;

		ps = c.prepareStatement("SELECT userId FROM CheckIn WHERE ID = ?");
		ps.setInt(1, checkInId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			result = rs.getInt(1);
		}

		return result;
	}

	public int getRaceId(int checkInId) throws SQLException {
		PreparedStatement ps;
		int result = -1;

		ps = c.prepareStatement("SELECT raceId FROM CheckIn WHERE ID = ?");
		ps.setInt(1, checkInId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			result = rs.getInt(1);
		}

		return result;
	}

	public String getPic(int checkInId) throws SQLException {
		PreparedStatement ps;
		String result = "";

		ps = c.prepareStatement("SELECT picture FROM CheckIn WHERE ID = ?");
		ps.setInt(1, checkInId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			result = rs.getString(1);
		}

		return result;
	}

	public void setPic(int checkInId, String pic) throws SQLException {
		PreparedStatement ps;

		ps = c.prepareStatement("UPDATE Checkin SET picture = ? WHERE ID = ?");
		ps.setString(1, pic);
		ps.setInt(2, checkInId);

		// throw error if fail
		int rows = ps.executeUpdate();

		c.commit();
		ps.close();
	}

	public String getComment(int checkInId) throws SQLException {
		PreparedStatement ps;
		String result = "";

		ps = c.prepareStatement("SELECT comment FROM CheckIn WHERE ID = ?");
		ps.setInt(1, checkInId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			result = rs.getString(1);
		}

		return result;
	}

	public void setComment(int checkInId, String comment) throws SQLException {
		PreparedStatement ps;

		ps = c.prepareStatement("UPDATE CheckIn SET comment = ? WHERE ID = ?");
		ps.setString(1, comment);
		ps.setInt(2, checkInId);

		// throw error if fail
		int rows = ps.executeUpdate();

		c.commit();
		ps.close();
	}

	public String getLocation(int checkInId) throws SQLException {
		PreparedStatement ps;
		String result = "";

		ps = c.prepareStatement("SELECT geolocation FROM CheckIn WHERE ID = ?");
		ps.setInt(1, checkInId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			result = rs.getString(1);
		}

		return result;
	}

	public void setLocation(int checkInId, String location) throws SQLException {
		PreparedStatement ps;

		ps = c.prepareStatement("UPDATE CheckIn SET location = ? WHERE ID = ?");
		ps.setString(1, location);
		ps.setInt(2, checkInId);

		// throw error if fail
		int rows = ps.executeUpdate();

		c.commit();
		ps.close();
	}
}