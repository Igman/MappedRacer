package Tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Beans.Conn;

public class RacerTest {
	private Connection c;
	
	
	@Before
	public void setUp() throws Exception {
		c = Conn.getInstance().getConnection();
		
		PreparedStatement ps;
		
		ps = c.prepareStatement(sql)
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAddRacers() {
		fail("Not yet implemented"); // TODO
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
