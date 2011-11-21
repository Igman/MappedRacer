package Beans;

/***************************************
 * User class that contains values     *
 * that are found in the DB User table *
 ***************************************/

public class User{
	private int id;
	private String username;
	
	public User(String username){
		this.username = username;
		//There is no int, since this variable is auto-incremented in the DB
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