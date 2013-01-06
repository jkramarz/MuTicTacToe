package messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class SidesMessageTest {
	SidesMessage m;
	@Test
	public void testSidesMessage() {
		m = new SidesMessage(1);
	}

	@Test
	public void testGetSide() {
		m = new SidesMessage(1);
		assertTrue(m.getSide() == 1);
	}

}
