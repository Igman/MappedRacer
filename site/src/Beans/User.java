package Beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/***************************************
 * User class that contains values     *
 * that are found in the DB User table *
 ***************************************/

public class User{
	private int id;
	private String username;
	private Connection c;
	
	public User () {
		
	}
	
	public User(String username){
		this.username = username;
		//There is no int, since this variable is auto-incremented in the DB
	}
	
	public int addUserDB(String username) {
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = c.prepareStatement("SELECT uid FROM User WHERE uname = ?");
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
			
			//if it's not there, add it
			ps = c.prepareStatement("INSERT INTO User(uname) VALUES (?)");
			ps.setString(1, username);		
			ps = c.prepareStatement("SELECT uid FROM User WHERE uname = ?");
			rs = ps.executeQuery();
			return rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}
}