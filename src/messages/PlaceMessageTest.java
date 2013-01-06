package messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlaceMessageTest {

	PlaceMessage m;
	@Test
	public void testPlaceMessage() {
		m = new PlaceMessage(3, 5);
	}

	@Test
	public void testGetX() {
		assertTrue(m.getX() == 3);
	}

	@Test
	public void testGetY() {
		assertTrue(m.getY() == 5);
	}

}
