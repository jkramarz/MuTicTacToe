package messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class NewGameMessageTest {

	NewGameMessage m;
	@Test
	public void testNewGameMessage() {
		m = new NewGameMessage(100);
	}

	@Test
	public void testGetPort() {
		m = new NewGameMessage(100);
		assertTrue(100 == m.getPort());
	}

}
