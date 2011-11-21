package Beans;
import java.sql.Time;
import java.util.*;

/**********************************************
 * Racers class that contains all attributes  *
 * a racer may contain and is mapped to       *
 * a table in the database					  *
 **********************************************/

public class Racers{
	private int raceId;
	private int userId;
	private boolean attend;
	private Time totalTime;
	private int place;
	
	public Racers(int raceId, int userId, boolean attend, Time totalTime, int place){
			this.raceId = raceId;
			this.userId = userId;
			this.attend = attend;
			this.totalTime = totalTime;
			this.place = place;
	}
	
	public int getRaceId(){
		return raceId;
	}
	public void setRaceId(int raceId){
		this.raceId = raceId;
	}
	
	public int getUserId(){
		return userId;
	}
	public void setUserId(int userId){
		this.userId = userId;
	}
	
	public boolean getAttend(){
		return attend;
	}
	public void setAttend(boolean attend){
		this.attend = attend;
	}
	
	public Time getTotalTime(){
		return totalTime;
	}
	public void setTotalTime(Time totalTime){
		this.totalTime = totalTime;
	}
	
	public int getPlace(){
		return place;
	}
	public void setPlace(int place){
		this.place = place;
	}
}