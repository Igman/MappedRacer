package Tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import Beans.Conn;
import Beans.Racer;
import Beans.RacerObj;
import Exceptions.InsertException;
import Exceptions.SelectException;

public class RacerTest {
	private static Connection c;
	private static int raceID;
	private static int userId1;
	private static int userId2;
	private static int userId3;
	
	private static Racer racer;
	
	@BeforeClass
	public static void setUp() throws Exception {
		c = Conn.getInstance().getConnection();
		
		PreparedStatement ps;
		racer = new Racer();
		Time time = new Time(Calendar.getInstance().getTimeInMillis());
		
		ps = c.prepareStatement("DELETE FROM Racers WHERE 1 = 1");
		ps.executeUpdate();
		ps = c.prepareStatement("DELETE FROM Users WHERE 1 = 1");
		ps.executeUpdate();
		ps = c.prepareStatement("DELETE FROM Race WHERE 1 = 1");
		ps.executeUpdate();
		
		System.out.println("Cleared Tables");
		
		ps = c.prepareStatement("INSERT INTO Users(uname) VALUES (?)");
		ps.setString(1, "Bob");
		userId1 = ps.executeUpdate();
		ps = c.prepareStatement("INSERT INTO Users(uname) VALUES (?)");
		ps.setString(1, "Alice");
		userId2 = ps.executeUpdate();
		ps = c.prepareStatement("INSERT INTO Users(uname) VALUES (?)");
		ps.setString(1, "Charles");
		userId3 = ps.executeUpdate();
		
		System.out.println("Added Users");
		
		c.commit();
		ps.close();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		PreparedStatement ps;
		int row;
		
		ps = c.prepareStatement("DELETE * FROM Racer WHERE 1 = 1");
		row = ps.executeUpdate();
		ps = c.prepareStatement("DELETE * FROM User WHERE 1 = 1");
		row = ps.executeUpdate();
		ps = c.prepareStatement("DELETE * FROM Race WHERE 1 = 1");
		row = ps.executeUpdate();
		
		c.commit();
		ps.close();
	}

	@Test
	public final void testAddRacers() {
		PreparedStatement ps;
		
		String[] testValues = {"Alice", "Charles"};
		List<Integer> ltest = new ArrayList<Integer>();
		boolean test = false;
		
		System.out.println("Start Test 1");
		try {
			test = racer.addRacers(testValues, raceID);
			ltest = racer.getRacersInt(raceID);
			ps = c.prepareStatement("INSERT INTO Race(Name, Start, CreatorID) VALUES (?,?,?)");
			ps.setString(1, "Race From Hell");
			ps.setString(2, "2011-10-10 20:23");
			ps.setInt(3, userId1);
			System.out.println(ps.toString());
			raceID = ps.executeUpdate();
			test = true;
		} catch (InsertException e) {
			e.printStackTrace();
			test = false;
		} catch (SelectException e) {
			e.printStackTrace();
			test = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			test = false;
		}
		System.out.println("End Test 1");
		assertTrue(test);
		assertTrue(ltest.contains(new Integer(userId2)));
		assertTrue(ltest.contains(new Integer(userId3)));
	}

	@Test
	public final void testAddRacer() {
		PreparedStatement ps;
		
		System.out.println("Start Test 2");
		try {
			ps = c.prepareStatement("INSERT INTO Users(uname) VALUES ?");
			ps.setString(1, "David");
			ps.executeUpdate();
			System.out.println(ps.toString());
			
			c.commit();
			ps.close();
			
			racer.addRacer("David", raceID);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InsertException e) {
			e.printStackTrace();
		}
		System.out.println("End Test 2");
		assertTrue(true);
	}

	@Test
	public final void testGetRacersObj() {
		boolean test1 = false;
		boolean test2 = false;
		boolean test3 = false;
		
		String[] uname = {"Bob", "Alice", "Charles", "David"};
		int[] uid = {userId1, userId2, userId3};
		
		try {
			List<RacerObj> lracers = racer.getRacersObj(raceID);
			test1 = (lracers.size() != 0);
			int i = 0;
			
			Iterator<RacerObj> iter = lracers.iterator();
			while (iter.hasNext()) {
				RacerObj temp = iter.next();
				test2 = (temp.getUserId() == uid[i]);
				test3 = (temp.getUserName().compareTo(uname[i]) == 0);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertTrue(test1);
		assertTrue(test2);
		assertTrue(test3);
	}
}
