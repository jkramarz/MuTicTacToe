package messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class MessageTest {

	@Test
	public void testGetErrorMessage() {
		assertTrue(Message.getErrorMessage(0) instanceof ErrorMessage);
	}

	@Test
	public void testGetNewGameMessage() {
		assertTrue(Message.getNewGameMessage(0) instanceof NewGameMessage);
	}

	@Test
	public void testGetPingMessage() {
		assertTrue(Message.getPingMessage() instanceof PingMessage);
	}

	@Test
	public void testGetPongMessage() {
		assertTrue(Message.getPongMessage() instanceof PongMessage);
	}

	@Test
	public void testGetWinMessage() {
		assertTrue(Message.getWinMessage() instanceof WinMessage);
	}

	@Test
	public void testGetLoseMessage() {
		assertTrue(Message.getLoseMessage() instanceof LoseMessage);
	}

	@Test
	public void testGetYourTurnMessage() {
		assertTrue(Message.getYourTurnMessage() instanceof YourTurnMessage);
	}

	@Test
	public void testGetChooseSidesMessage() {
		assertTrue(Message.getChooseSidesMessage() instanceof ChooseSidesMessage);
	}

	@Test
	public void testGetDisconnectMessage() {
		assertTrue(Message.getDisconnectMessage() instanceof DisconnectMessage);
	}

}
