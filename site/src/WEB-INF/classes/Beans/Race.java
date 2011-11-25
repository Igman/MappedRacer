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
	
	/**
	 * Constructor that allows you to create the race model without associating
	 * it with a single race.
	 */
	public Race() {
		
	}
	
	/**
	 * This function will allow its caller to create a new race in the database and get
	 * back the race id for future reference.
	 * 
	 * @param name			The name of the race.
	 * @param endPoint		A string representing the end location of the race.
	 * @param startTime		The start date and time.
	 * @param createDate	The the creation date of the race.
	 * @param creatorId		The user id of the user who is creating the race.
	 * @return				The id of the race that was just created.
	 */
	public int createRace(String name, String endPoint, Date startTime, Date createDate, int creatorId){
		PreparedStatement ps;
		
		try {
			// Adds the new race to the database
			ps = c.prepareStatement("INSERT INTO Race VALUES (?,?,?,?,?)");
			
			ps.setInt(1, creatorId);
			ps.setString(2, name);
			ps.setString(3, endPoint);
			ps.setDate(4, new java.sql.Date(startTime.getTime()));
			ps.setDate(5, new java.sql.Date(createDate.getTime()));
			
			c.commit();
			
			//TODO how to get race id out?!
			// Gets the raceId to return
			ps = c.prepareStatement("SELECT MAX(RaceId) FROM Race");
			
			ResultSet rs = ps.executeQuery();
			ps.close();
			
			rs.next();
			int raceId = rs.getInt(1);
			
			return raceId;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	
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