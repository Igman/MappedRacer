package Tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Beans.Conn;

public class RacerTest {
	private Connection c;
	private int raceId;
	private int userId1;
	private int userId2;
	private int userId3;
	
	@Before
	public void setUp() throws Exception {
		c = Conn.getInstance().getConnection();
		
		PreparedStatement ps;
		int row;
		
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
		raceId = ps.executeUpdate();
		
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
		int[] testValues = {userId2, userId3};
		
		
	}

	@Test
	public final void testAddRacer() {
		fail("Not yet implemented"); // TODO
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
	public final void testGetTotalTime() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetTotalTime() {
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
