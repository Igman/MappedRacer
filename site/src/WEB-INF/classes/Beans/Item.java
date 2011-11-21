package Beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Masterfod
 *
 */
public class Item {
	private int id;
	private int value;
	private String geoLocation;
	private boolean active;

	private Connection c;

	public Item(int value, String geoLocation, boolean active) {
		this.value = value;
		this.geoLocation = geoLocation;
		this.active = active;
	}

	/**
	 * 
	 */
	public void itemDB() {
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("INSERT INTO Item VALUES (?,?,?)");

			ps.setInt(1, value);
			ps.setString(2, geoLocation);
			ps.setBoolean(3, active);

			c.commit();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param raceId
	 * @param userId
	 * @param itemId
	 */
	public void getItemDB(int raceId, int userId, int itemId) {
		PreparedStatement ps;

		try {
			ps = c.prepareStatement("SELECT value = ? FROM Item WHERE itemId = ?");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				this.value = Integer.parseInt(rs.getString(1));
			}

			ps = c.prepareStatement("UPDATE Item SET active = ?, WHERE itemId = ?");

			ps.setBoolean(1, false);
			ps.setInt(2, itemId);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				// Throw Exception
			}
			
			ps = c.prepareStatement("UPDATE score = score + ? FROM Racer WHERE raceId = ?, userId = ?");
			
			ps.setInt(1, value);
			ps.setInt(2, raceId);
			ps.setInt(3, userId);
			
			rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				// Throw Exception
			}
			
			c.commit();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
