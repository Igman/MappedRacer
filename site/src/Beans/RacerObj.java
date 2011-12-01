package Beans;

import java.sql.Time;

public class RacerObj {
	private boolean attend;
	private Time totalTime;
	private int score;
	
	public boolean isAttend() {
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
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

}
