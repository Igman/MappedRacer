package Beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**********************************************
 * Race class that contains all attributes * a race may contain and is mapped to
 * the DB *
 **********************************************/

public class Race {
	private int id;
	private String name;
	private String endPoint;
	private Date createDate;
	private Time startTime;
	private Date startDate;
	private int creatorId;

	private Connection c;

	public Race(String name, String endPoint, Date createDate, Time startTime,
			Date startDate, int creatorId) {
		this.name = name;
		this.endPoint = endPoint;
		this.createDate = createDate;
		this.startTime = startTime;
		this.startDate = startDate;
		this.creatorId = creatorId;
		// There is no int, since this variable is auto-incremented in the DB
	}

	public List<Race> getRacesDB(int raceId, int userId) {
		PreparedStatement ps;
		List<Race> lsRace = new ArrayList<Race>();

		try {
			ps = c.prepareStatement("SELECT * FROM Race r LEFT JOIN User u ON ((u.id = ?) = (r.userid = ?))");

			SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss");
			SimpleDateFormat date = new SimpleDateFormat("MM:dd:yy");

			int id;
			String name;
			String endPoint;
			Date createDate = null;
			Time startTime = null;
			Date startDate = null;
			int creatorId;

			ps.setInt(2, raceId);
			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();
			ps.close();

			while (rs.next()) {
				id = Integer.parseInt(rs.getString(1));
				name = rs.getString(2);
				endPoint = rs.getString(3);
				try {
					createDate = (Date) date.parse(rs.getString(4));
					startTime = (Time) time.parse(rs.getString(5));
					startDate = (Date) date.parse(rs.getString(6));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				creatorId = Integer.parseInt(rs.getString(7));

				Race race = new Race(name, endPoint, createDate, startTime,
						startDate, creatorId);
				race.setId(id);

				lsRace.add(race);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return lsRace;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
}