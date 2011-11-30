package Beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

<<<<<<< HEAD
/**
 * 
 * @author Christiaan Fernando
 *
 */
public class Item {
	private int id;
	private int value;
	private String geoLocation;
	private boolean active;
=======
/******************************************************
 * This is a model class that will set and get any    *
 * properties that have to do with the item objects   *
 * that will be stored in the database. This includes *
 * things like the finish line, positive point items  *
 * and negative point items.						  *
 ******************************************************/
>>>>>>> 5180461b27bd500a41594d10089a53af6f2478da

public class Item {
	private Connection c;
	
	
	/**
	 * This function is the constructor which will set up the connection
	 * required to communicate with the DB.
	 * 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public Item() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		c = DriverManager.getConnection("jdbc:mysql://localhost/mappedrace", "test", "");
	}
	
	/**
	 * This adds an item using a list of item objects. The object must be 
	 * the same as the one found in CreateRaceController.
	 * 
	 * @param items		An array of objects representing the items.
	 * @throws SQLException
	 */
	public void addItems(Object[] items) throws SQLException {
		for(int k = 0; k < items.length; k++) {
			addItem((ItemObj) items[k]);
		}
	}
	
	/**
	 * This adds a single item using the item object as the parameter. The
	 * object must be the same as the one found in CreateRaceController.
	 * 
	 * @param itemObj	A single object representing an item.
	 * @throws SQLException
	 */
	public void addItem(Object itemObj) throws SQLException {
		ItemObj item = (ItemObj) itemObj;
		addItem(item.value, item.location, item.type);
	}
	
	/**
	 * This is a function for adding an object by specifying the parameters
	 * of an item.
	 * 
	 * @param value			The value in points the item possesses.
	 * @param location		The location of the item.
	 * @param type			The type of item. 1-destination 2-positive, 3-negative
	 * @throws SQLException
	 */
	public void addItem(int value, String location, int type) throws SQLException {
		addItem(value, location, type, true);
	}
	
	/**
	 * This function is for creating an object with all of the parameters. It
	 * also allows you to set whether or not you would like the item to be 
	 * active.
	 * 
	 * @param value			The value in points the item possesses.
	 * @param location		The location of the item.
	 * @param type			The type of item. 1-destination 2-positive, 3-negative
	 * @param active		Booleans as to whether item will be active or not.
	 * @throws SQLException
	 */
	public void addItem(int value, String location, int type, boolean active) throws SQLException {
		PreparedStatement ps;
		
		ps = c.prepareStatement("INSERT INTO Item VALUES (?,?,?)");
		
		ps.setInt(1, type);
		ps.setInt(2, value);
		ps.setString(3, location);
		ps.setBoolean(4, active);
		
		ps.executeUpdate();
		
		c.commit();
		ps.close();
		
	}
	
	/**
	 * Class for the item object.
	 * TODO: Is this the best way to pass an object as an array and have it
	 * mapped to the one in the controller class...?
	 */
	private class ItemObj {
		public String location;
		public int type;
		public int value;
	}
}
