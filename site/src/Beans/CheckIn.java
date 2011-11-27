package Beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**********************************************
 * Race class that contains all attributes * a race may contain and is mapped to
 * the DB *
 **********************************************/

public class CheckIn {
	private int id;
	private int raceId;
	private String picture;
	private String comment;
	private String geoLocation;

	private Connection c;

	public CheckIn(int raceId, String picture, String comment,
			String geoLocation) {
		this.raceId = raceId;
		this.picture = picture;
		this.comment = comment;
		this.geoLocation = geoLocation;
		// There is no int, since this variable is auto-incremented in the DB
	}

	public void addCheckInDB() {
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("INSERT INTO CheckIn VALUES (?,?,?,?,?)");

			ps.setInt(1, raceId);
			ps.setString(2, picture);
			ps.setString(3, comment);
			ps.setString(4, geoLocation);

			c.commit();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CheckIn getCheckInDB(int id) {
		PreparedStatement ps;
		ResultSet rs;
		CheckIn result = null;

		try {
			ps = c.prepareStatement("SELECT * FROM CheckIn WHERE id = ?");
			ps.setInt(1, id);

			rs = ps.executeQuery();

			while (rs.next()) {
				result = new CheckIn(Integer.parseInt(rs.getString(2)),
						rs.getString(3), rs.getString(4), rs.getString(5));
				result.setId(Integer.parseInt(rs.getString(1)));
			}

			c.commit();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRaceId() {
		return raceId;
	}

	public void setRaceId(int raceId) {
		this.raceId = raceId;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}
}