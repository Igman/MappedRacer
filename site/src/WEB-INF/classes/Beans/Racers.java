package Beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

/**********************************************
 * Racers class that contains all attributes * a racer may contain and is mapped
 * to * a table in the database *
 **********************************************/

public class Racers {
	private int raceId;
	private int userId;
	private boolean attend;
	private Time totalTime;
	private int place;

	private Connection c;

	public Racers(int raceId, int userId, boolean attend, Time totalTime,
			int place) {
		this.raceId = raceId;
		this.userId = userId;
		this.attend = attend;
		this.totalTime = totalTime;
		this.place = place;
	}

	public void racersDB() {
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("INSERT INTO Racer VALUES (?,?,?,?,?)");

			ps.setInt(1, raceId);
			ps.setInt(2, userId);
			ps.setBoolean(3, attend);
			ps.setTime(4, totalTime);
			ps.setInt(5, place);

			c.commit();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void racersScoreDB(int raceId, int userId, int score) {
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("UPDATE Racer SET score = ?, WHERE raceId = ?, userId = ?");

			ps.setInt(2, raceId);
			ps.setInt(3, userId);
			ps.setInt(1, score);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0)
				// Throw Exception

				c.commit();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getRaceId() {
		return raceId;
	}

	public void setRaceId(int raceId) {
		this.raceId = raceId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean getAttend() {
		return attend;
	}

	public void setAttend(boolean attend) {
		this.attend = attend;
	}

	public Time getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Time totalTime) {
		this.totalTime = totalTime;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}
}