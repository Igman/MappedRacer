package jsp;
import java.sql.Time;
import java.util.*;

/**********************************************
 * Race class that contains all attributes    *
 * a race may contain and is mapped to the DB *
 **********************************************/

public class Race{
	private int id;
	private String name;
	private String endPoint;
	private Date createDate;
	private Time startTime;
	private Date startDate;
	private int creatorId;
	
	public Race(String name, String endPoint, Date createDate, Time startTime, 
				Date startDate, int creatorId){
			this.name = name;
			this.endPoint = endPoint;
			this.createDate = createDate;
			this.startTime = startTime;
			this.startDate = startDate;
			this.creatorId = creatorId;
			//There is no int, since this variable is auto-incremented in the DB
	}
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String getEndPoint(){
		return endPoint;
	}
	public void setEndPoint(String endPoint){
		this.endPoint = endPoint;
	}
	
	public Date getCreateDate(){
		return createDate;
	}
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	
	public Time getStartTime(){
		return startTime;
	}
	public void setStartTime(Time startTime){
		this.startTime = startTime;
	}
	
	public Date getStartDate(){
		return startDate;
	}
	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}
	
	public int getCreatorId(){
		return creatorId;
	}
	public void setCreatorId(int creatorId){
		this.creatorId = creatorId;
	}
}