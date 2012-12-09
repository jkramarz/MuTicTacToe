package server;

import static org.junit.Assert.assertTrue;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.junit.Test;

public class MessageTest {

	int[] codes = { 503, 501, 409, 403, 0 };

	@Test
	public void testGetErrorMessage() {
		for (int i : codes) {
			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
					.getErrorMessage(i));
			assertTrue(jsonObject.getString("status") == "ERROR");
			assertTrue(jsonObject.getString("error_code") == new Integer(i)
					.toString());
			assertTrue(jsonObject.getString("error_message") == Message
					.getErrorText(i));
		}
	}

	@Test
	public void testGetErrorText() {
		assertTrue(Message.getErrorText(503) == "brak mo¿liwoœci stworzenia gry ze wzgledu na brak dostepnych wolnych portow");
		assertTrue(Message.getErrorText(501) == "dana metoda jest niezaimplementowana");
		assertTrue(Message.getErrorText(409) == "przes³any komunikat nieadekwatny do obecnego stanu");
		assertTrue(Message.getErrorText(404) == "przes³any komunikat niezgodny z protoko³em");
		assertTrue(Message.getErrorText(403) == "próba po³o¿enia pionka na ju¿ zajetym miejscu");
		assertTrue(Message.getErrorText(0) == null);
	}

	@Test
	public void testGetNewGameMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getNewGameMessage(0));
		assertTrue(jsonObject.getString("status") == "OK");
		assertTrue(jsonObject.getString("port") == new Integer(0).toString());
	}

	@Test
	public void testGetChooseSideMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getChooseSideMessage());
		assertTrue(jsonObject.getString("status") == "WHO STARTS");
	}

	@Test
	public void testGetPlayerTurnMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getPlayerTurnMessage());
		assertTrue(jsonObject.getString("status") == "TURN");
		assertTrue(jsonObject.getString("attribute") == "YOUR");
	}

	@Test
	public void testGetOponentTurnMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getPlayerTurnMessage());
		assertTrue(jsonObject.getString("status") == "TURN");
		assertTrue(jsonObject.getString("attribute") == "OPONENT");
	}

	@Test
	public void testGetDisconnectedMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getDisconnectedMessage());
		assertTrue(jsonObject.getString("status") == "DISCONNECTED");
	}

	@Test
	public void testGetOponentPlaceMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getOponentPlaceMessage(1, 2));
		assertTrue(jsonObject.getString("status") == "PLACE");
		assertTrue(jsonObject.getString("x") == new Integer(1).toString());
		assertTrue(jsonObject.getString("y") == new Integer(2).toString());
	}

	@Test
	public void testGetConnectedMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getConnectedMessage());
		assertTrue(jsonObject.getString("status") == "CONNECTED");
	}

	@Test
	public void testGetPongMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getPongMessage());
		assertTrue(jsonObject.getString("status") == "PONG");
	}

}
