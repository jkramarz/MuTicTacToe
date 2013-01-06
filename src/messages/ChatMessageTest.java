package messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChatMessageTest {

	@Test
	public void testGetChat() {
		assertEquals("test", new ChatMessage("test").getChat());
	}

}
