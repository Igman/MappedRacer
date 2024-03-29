package Beans;

/**
 * This class represents a CheckIn object.
 * 
 * @author Christiaan Fernando
 *
 */
public class CheckInObj {
	private int ID;
	private int userID;
	private int raceID;
	private String pic;
	private String comment;
	private String location;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getRaceID() {
		return raceID;
	}
	public void setRaceID(int raceID) {
		this.raceID = raceID;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
}
