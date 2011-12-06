package Beans;

import java.sql.Timestamp;

public class RaceObj {
	private int id;
	private int creatorId;
	private String name;
	private Timestamp start;
	//Used for the ViewHistory Controller
	private int score;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getStart() {
		return start;
	}
	public void setStart(Timestamp timestamp) {
		this.start = timestamp;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
