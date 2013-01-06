package messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class NewGameRequestMessageTest {

	@Test
	public void testNewGameRequestMessage() {
		NewGameRequestMessage m = new NewGameRequestMessage("PVP");
		assertEquals("", m.getName());
	}

	@Test
	public void testNewGameRequestMessageString() {
		NewGameRequestMessage m = new NewGameRequestMessage("PVC", "test");
		assertEquals("test", m.getName());
	}

}
