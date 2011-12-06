package Beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Christiaan Fernando
 *
 */

/******************************************************
 * This is a model class that will set and get any    *
 * properties that have to do with the item objects   *
 * that will be stored in the database. This includes *
 * things like the finish line, positive point items  *
 * and negative point items.						  *
 ******************************************************/

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
		c = Conn.getInstance().getConnection();
	}
	
	/**
	 * This adds an item using a list of item objects. The object must be 
	 * the same as the one found in CreateRaceController.
	 * 
	 * @param items		An array of objects representing the items.
	 * @throws SQLException
	 */
	public void addItems(ItemObj[] items, int raceID) throws SQLException {
		for(int k = 0; k < items.length; k++) {
			addItem( items[k], raceID);
		}
	}
	
	/**
	 * This adds a single item using the item object as the parameter. The
	 * object must be the same as the one found in CreateRaceController.
	 * 
	 * @param itemObj	A single object representing an item.
	 * @throws SQLException
	 */
	public void addItem(ItemObj itemObj, int raceID) throws SQLException {
		ItemObj item = itemObj;
		addItem(item.getValue(), item.getLocation(), item.getType(), raceID);
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
	public void addItem(int value, String location, int type, int raceID) throws SQLException {
		addItem(value, location, type, raceID, true);
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
	public void addItem(int value, String location, int type, int raceID, boolean active) throws SQLException {
		PreparedStatement ps;
		
		ps = c.prepareStatement("INSERT INTO Item (TypeID,ValueWeight,Geolocation,Status, RaceID) VALUES (?,?,?,?,?)");
		
		ps.setInt(1, type);
		ps.setInt(2, value);
		ps.setString(3, location);
		ps.setBoolean(4, active);
		ps.setInt(5, raceID);
		
		//throw error if fail
		ps.executeUpdate();
		
		c.commit();
		ps.close();
		
	}
	
	public List<Integer> getItemsInt(int raceId) throws SQLException {
		PreparedStatement ps;
		List<Integer> results = new ArrayList<Integer>();
		
		ps = c.prepareStatement("SELECT ID FROM Item WHERE raceId = ?");
		ps.setInt(1, raceId);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			Integer temp = rs.getInt(1);
			results.add(temp);
		}
		
		return results;
	}
	
	public List<ItemObj> getItemsObj(int raceId) throws SQLException {
		List<ItemObj> results = new ArrayList<ItemObj>();
		PreparedStatement ps;

		ps = c.prepareStatement("SELECT id, typeid, status, valueweight, geolocation FROM Item WHERE raceid = ? AND status=1");
		ps.setInt(1, raceId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			ItemObj itemObj = new ItemObj();
			itemObj.setItemId(rs.getInt(1));
			itemObj.setType(rs.getInt(2));
			itemObj.setStatus(rs.getBoolean(3));
			itemObj.setValue(rs.getInt(4));
			itemObj.setLocation(rs.getString(5));
			itemObj.setRaceId(raceId);

			results.add(itemObj);
		}

		return results;
	}
	
	public int getType(int itemId) throws SQLException {
		PreparedStatement ps;
		int result = -1;
		
		ps = c.prepareStatement("SELECT TypeID FROM Item WHERE ID = ?");
		ps.setInt(1, itemId);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			result = rs.getInt(1);
		}
		
		return result;
	}
	
	public boolean getStatus(int itemId) throws SQLException {
		PreparedStatement ps;
		boolean result = false;
		
		ps = c.prepareStatement("SELECT Status FROM Item WHERE ID = ?");
		ps.setInt(1, itemId);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			result = rs.getBoolean(1);
		}
		
		return result;
	}
	
	public void setStatus(int itemId, boolean status) throws SQLException {
		PreparedStatement ps;
		
		ps = c.prepareStatement("UPDATE Item SET status = ? WHERE ID = ?");
		ps.setBoolean(1, status);
		ps.setInt(2, itemId);
		
		//throw error if fail
		ps.executeUpdate();
		
		c.commit();
		ps.close();
	}
	
	public int getValue(int itemId) throws SQLException {
		PreparedStatement ps;
		int result = -1;
		
		ps = c.prepareStatement("SELECT ValueWeight FROM Item WHERE ID = ?");
		ps.setInt(1, itemId);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			result = rs.getInt(1);
		}
		
		return result;
	}
	
	public String getLocation(int itemId) throws SQLException {
		PreparedStatement ps;
		String result = "";
		
		ps = c.prepareStatement("SELECT Geolocation FROM Item WHERE ID = ?");
		ps.setInt(1, itemId);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			result = rs.getString(1);
		}
		
		return result;
	}
	
	public int getRaceId(int itemId) throws SQLException {
		PreparedStatement ps;
		int result = -1;
		
		ps = c.prepareStatement("SELECT RaceId FROM Item WHERE ID = ?");
		ps.setInt(1, itemId);
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			result = rs.getInt(1);
		}
		
		return result;
	}

	public void setItemValue(int itemID, int status) throws SQLException {
		PreparedStatement ps;
		ps = c.prepareStatement("UPDATE Item SET status=? WHERE ID=?");
		ps.setInt(1, status);
		ps.setInt(2, itemID);
		
		ps.executeUpdate();
		c.commit();
		ps.close();
	}

	
}
