package Beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Exceptions.InsertException;
import Exceptions.SelectException;
import Exceptions.UpdateException;

/**
 * 
 * @author Christiaan Fernando
 *
 */

/******************************************************
 * This is a model class that will set and get any * properties that have to do
 * with the item objects * that will be stored in the database. This includes *
 * things like the finish line, positive point items * and negative point items.
 * *
 ******************************************************/

public class Item {
	private Connection c;

	/**
	 * Default Constructor
	 * 
	 * This function is the constructor which will set up the connection
	 * required to communicate with the DB.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Item() {
		c = Conn.getInstance().getConnection();
	}

	/**
	 * Adds a list of ItemObjs to the database.
	 * 
	 * This adds an item using a list of item objects. The object must be the
	 * same as the one found in CreateRaceController.
	 * 
	 * @param items
	 *            An array of objects representing the items.
	 * @throws InsertException
	 */
	public void addItems(ItemObj[] items, int raceID) throws InsertException {
		for (int k = 0; k < items.length; k++) {
			addItem(items[k], raceID);
		}
	}

	/**
	 * Adds an ItemObj to the database.
	 * 
	 * This adds a single item using the item object as the parameter. The
	 * object must be the same as the one found in CreateRaceController.
	 * 
	 * @param itemObj
	 *            A single object representing an item.
	 * @throws InsertException
	 */
	public void addItem(ItemObj itemObj, int raceID) throws InsertException {
		ItemObj item = itemObj;
		addItem(item.getValue(), item.getLocation(), item.getType(), raceID);
	}

	/**
	 * Adds an item to the database. This item is set to active by default.
	 * 
	 * This is a function for adding an object by specifying the parameters of
	 * an item.
	 * 
	 * @param value
	 *            The value in points the item possesses.
	 * @param location
	 *            The location of the item.
	 * @param type
	 *            The type of item. 1-destination 2-positive, 3-negative
	 * @throws InsertException
	 */
	public void addItem(int value, String location, int type, int raceID)
			throws InsertException {
		addItem(value, location, type, raceID, true);
	}

	/**
	 * Adds an item to the database.
	 * 
	 * This function is for creating an object with all of the parameters. It
	 * also allows you to set whether or not you would like the item to be
	 * active.
	 * 
	 * @param value
	 *            The value in points the item possesses.
	 * @param location
	 *            The location of the item.
	 * @param type
	 *            The type of item. 1-destination 2-positive, 3-negative
	 * @param active
	 *            Booleans as to whether item will be active or not.
	 * @throws UpdateException
	 */

	public void addItem(int value, String location, int type, int raceID,
			boolean active) throws InsertException {
		PreparedStatement ps = null;
		int rows = -1;

		try {
			ps = c.prepareStatement("INSERT INTO Item (TypeID,ValueWeight,Geolocation,Status, RaceID) VALUES (?,?,?,?,?)");
			ps.setInt(1, type);
			ps.setInt(2, value);
			ps.setString(3, location);
			ps.setBoolean(4, active);
			ps.setInt(5, raceID);
		} catch (SQLException e) {
			throw new InsertException("Failed to insert Item");
		}

		// throw error if fail
		try {
			if ((rows = ps.executeUpdate()) != 1)
				throw new InsertException(
						"Item wasn't updated in the database " + rows
								+ " rows updated.");
		} catch (SQLException e) {
			throw new InsertException("Database error");
		}

		try {
			c.commit();
			ps.close();
		} catch (SQLException e) {
			throw new InsertException("Database error");
		}
	}

	/**
	 * Gets a list of item ids for a given race.
	 * 
	 * Given a race id, this function returns a list of Integers of the item
	 * id's present in the race.
	 * 
	 * @param raceId
	 *            Race id of the race which holds the items
	 * @return A list of item ids
	 * @throws SelectException
	 */
	public List<Integer> getItemsInt(int raceId) throws SelectException {
		PreparedStatement ps;
		ResultSet rs = null;
		List<Integer> results = new ArrayList<Integer>();

		try {
			ps = c.prepareStatement("SELECT ID FROM Item WHERE raceId = ?");
			ps.setInt(1, raceId);

			rs = ps.executeQuery();
		} catch (SQLException e) {
			throw new SelectException("Unable to get items");
		}

		try {
			while (rs.next()) {
				Integer temp = rs.getInt(1);
				results.add(temp);
			}
			c.commit();
			ps.close();
		} catch (SQLException e) {
			throw new SelectException("Error on returned items");
		}

		return results;
	}

	/**
	 * Gets a list of ItemObjs for a given race.
	 * 
	 * Given a race id, this function returns a list of ItemObjs present in the
	 * race.
	 * 
	 * @param raceId
	 *            Race id of the race which holds the items
	 * @return A list of ItemObjs
	 * @throws SelectException
	 */
	public List<ItemObj> getItemsObj(int raceId) throws SelectException {
		List<ItemObj> results = new ArrayList<ItemObj>();
		PreparedStatement ps;
		ResultSet rs = null;

		try {
			ps = c.prepareStatement("SELECT id, typeid, status, valueweight, geolocation FROM Item WHERE raceid = ? AND status=1");
			ps.setInt(1, raceId);

			rs = ps.executeQuery();
		} catch (SQLException e) {
			throw new SelectException("Unable to get items");
		}

		try {
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
			c.commit();
			ps.close();
		} catch (SQLException e) {
			throw new SelectException("Error on returned items");
		}

		return results;
	}

	/**
	 * Gets item type
	 * 
	 * Given an item id, this method obtains the item type.
	 * 
	 * @param itemId
	 *            The item's id
	 * @return The type of the item
	 * @throws SelectException
	 */
	public int getType(int itemId) throws SelectException {
		PreparedStatement ps;
		ResultSet rs = null;
		int result = -1;

		try {
			ps = c.prepareStatement("SELECT TypeID FROM Item WHERE ID = ?");
			ps.setInt(1, itemId);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			throw new SelectException("Unable to get item type");
		}

		try {
			while (rs.next()) {
				result = rs.getInt(1);
			}
			c.commit();
			ps.close();
		} catch (SQLException e) {
			throw new SelectException("Error on returned item type");
		}

		return result;
	}

	/**
	 * Gets item status
	 * 
	 * Given an item id, this method obtains the item status.
	 * 
	 * @param itemId
	 *            The item's id
	 * @return The status of the item
	 * @throws SelectException
	 */
	public boolean getStatus(int itemId) throws SelectException {
		PreparedStatement ps;
		ResultSet rs = null;
		boolean result = false;

		try {
			ps = c.prepareStatement("SELECT Status FROM Item WHERE ID = ?");
			ps.setInt(1, itemId);

			rs = ps.executeQuery();
		} catch (SQLException e) {
			throw new SelectException("Unable to get item status");
		}

		try {
			while (rs.next()) {
				result = rs.getBoolean(1);
			}
			c.commit();
			ps.close();
		} catch (SQLException e) {
			throw new SelectException("Error on returned item status");
		}

		return result;
	}

	/**
	 * Sets status
	 * 
	 * Given an item's id, this sets the status to the input boolean.
	 * 
	 * @param itemId
	 *            The item's id
	 * @param status
	 *            The new status
	 * @throws UpdateException
	 */
	public void setStatus(int itemId, boolean status) throws UpdateException {
		PreparedStatement ps;
		int rows = -1;

		try {
			ps = c.prepareStatement("UPDATE Item SET status = ? WHERE ID = ?");
			ps.setBoolean(1, status);
			ps.setInt(2, itemId);
		} catch (SQLException e) {
			throw new UpdateException("Unable to update the status");
		}

		// throw error if fail
		try {
			if ((rows = ps.executeUpdate()) != 1)
				throw new UpdateException(
						"Status wasn't updated in the database " + rows
								+ " rows updated.");
		} catch (SQLException e) {
			throw new UpdateException("Failed to update the database");
		}

		try {
			c.commit();
			ps.close();
		} catch (SQLException e) {
			throw new UpdateException("Database error");
		}
	}

	/**
	 * Gets item value
	 * 
	 * Given an item id, this method obtains the item's value.
	 * 
	 * @param itemId
	 *            The item's id
	 * @return The point value of the item
	 * @throws SelectException
	 */
	public int getValue(int itemId) throws SelectException {
		PreparedStatement ps;
		ResultSet rs = null;
		int result = -1;

		try {
			ps = c.prepareStatement("SELECT ValueWeight FROM Item WHERE ID = ?");
			ps.setInt(1, itemId);

			rs = ps.executeQuery();
		} catch (SQLException e) {
			throw new SelectException("Unable to get item value");
		}

		try {
			while (rs.next()) {
				result = rs.getInt(1);
			}
			c.commit();
			ps.close();
		} catch (SQLException e) {
			throw new SelectException("Error on returned item value");
		}

		return result;
	}

	/**
	 * Sets value
	 * 
	 * Given an item's id, this sets the value to the input point amount.
	 * 
	 * @param itemId
	 *            The item's id
	 * @param status
	 *            The new point value
	 * @throws UpdateException
	 */
	public void setValue(int itemID, int value) throws UpdateException {
		PreparedStatement ps;
		int rows = -1;

		try {
			ps = c.prepareStatement("UPDATE Item SET ValueWeight=? WHERE ID=?");
			ps.setInt(1, value);
			ps.setInt(2, itemID);
		} catch (SQLException e) {
			throw new UpdateException("Unable to update the value");
		}

		try {
			if ((rows = ps.executeUpdate()) != 1)
				throw new UpdateException(
						"Value wasn't updated in the database " + rows
								+ " rows updated.");
		} catch (SQLException e) {
			throw new UpdateException("Failed to update the database");
		}
		try {
			c.commit();
			ps.close();
		} catch (SQLException e) {
			throw new UpdateException("Database Error");
		}

	}

	/**
	 * Gets item location
	 * 
	 * Given an item id, this method obtains the item's location.
	 * 
	 * @param itemId
	 *            The item's id
	 * @return The location of the item
	 * @throws SelectException
	 */
	public String getLocation(int itemId) throws SelectException {
		PreparedStatement ps;
		ResultSet rs = null;
		String result = "";

		try {
			ps = c.prepareStatement("SELECT Geolocation FROM Item WHERE ID = ?");
			ps.setInt(1, itemId);

			rs = ps.executeQuery();
		} catch (SQLException e) {
			throw new SelectException("Unable to get item location");
		}

		try {
			while (rs.next()) {
				result = rs.getString(1);
			}
			c.commit();
			ps.close();
		} catch (SQLException e) {
			throw new SelectException("Error on returned item location");
		}

		return result;
	}

	/**
	 * Gets item's race id
	 * 
	 * Given an item id, this method obtains the id of the race the item belongs
	 * to.
	 * 
	 * @param itemId
	 *            The item's id
	 * @return The race id
	 * @throws SelectException
	 */
	public int getRaceId(int itemId) throws SelectException {
		PreparedStatement ps;
		int result = -1;
		ResultSet rs = null;

		try {
			ps = c.prepareStatement("SELECT RaceId FROM Item WHERE ID = ?");
			ps.setInt(1, itemId);

			rs = ps.executeQuery();
		} catch (SQLException e) {
			throw new SelectException("Unable to get race id");
		}

		try {
			while (rs.next()) {
				result = rs.getInt(1);
			}
			c.commit();
			ps.close();
		} catch (SQLException e) {
			throw new SelectException("Error on returned race id");
		}

		return result;
	}

}
