package Tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Beans.Conn;
import Beans.Racer;

public class RacerTest {
	private Connection c;
	private int raceID;
	private int userId1;
	private int userId2;
	private int userId3;
	
	private Racer racer;
	
	@Before
	public void setUp() throws Exception {
		c = Conn.getInstance().getConnection();
		
		PreparedStatement ps;
		int row;
		racer = new Racer();
		
		ps = c.prepareStatement("DELETE * FROM Racer");
		row = ps.executeUpdate();
		ps = c.prepareStatement("DELETE * FROM User");
		row = ps.executeUpdate();
		ps = c.prepareStatement("DELETE * FROM Race");
		row = ps.executeUpdate();
		ps = c.prepareStatement("INSERT INTO Users(uname) VALUES 'Bob'");
		userId1 = ps.executeUpdate();
		ps = c.prepareStatement("INSERT INTO Users(uname) VALUES 'Alice'");
		userId2 = ps.executeUpdate();
		ps = c.prepareStatement("INSERT INTO Users(uname) VALUES 'Charles'");
		userId3 = ps.executeUpdate();
		ps = c.prepareStatement("INSERT INTO Race(Name, Start, CreatorID) VALUES ('Race From Hell'," + Calendar.getInstance().getTimeInMillis() + "," + row + ")");
		raceID = ps.executeUpdate();
		
		c.commit();
		ps.close();
	}

	@After
	public void tearDown() throws Exception {
		PreparedStatement ps;
		int row;
		
		ps = c.prepareStatement("DELETE * FROM Racer");
		row = ps.executeUpdate();
		ps = c.prepareStatement("DELETE * FROM User");
		row = ps.executeUpdate();
		ps = c.prepareStatement("DELETE * FROM Race");
		row = ps.executeUpdate();
		
		c.commit();
		ps.close();
	}

	@Test
	public final void testAddRacers() {
		String[] testValues = {"Alice", "Charles"};
		List<Integer> ltest = new ArrayList<Integer>();
		boolean test = false;
		
		try {
			test = racer.addRacers(testValues, raceID);
			ltest = racer.getRacersInt(raceID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertTrue(test);
		assertTrue(ltest.contains(new Integer(userId2)));
		assertTrue(ltest.contains(new Integer(userId3)));
		
	}

	@Test
	public final void testAddRacer() {
		PreparedStatement ps;
		
		try {
			ps = c.prepareStatement("INSERT INTO Users(uname) VALUES 'David'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public final void testGetRacersInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetRacersObj() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetAttend() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetScore() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetScore() {
		fail("Not yet implemented"); // TODO
	}

}
