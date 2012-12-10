package server;

import static org.junit.Assert.assertEquals;
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
			assertEquals("ERROR", jsonObject.getString("status").trim());
			assertEquals(new Integer(i).toString(),
					jsonObject.getString("error_code").trim());
			assertEquals(Message.getErrorText(i),
					jsonObject.getString("error_message").trim());
		}
	}

	@Test
	public void testGetErrorText() {
		assertTrue(Message.getErrorText(503) == "brak mo¿liwoœci stworzenia gry ze wzgledu na brak dostepnych wolnych portow");
		assertTrue(Message.getErrorText(501) == "dana metoda jest niezaimplementowana");
		assertTrue(Message.getErrorText(409) == "przes³any komunikat nieadekwatny do obecnego stanu");
		assertTrue(Message.getErrorText(404) == "przes³any komunikat niezgodny z protoko³em");
		assertTrue(Message.getErrorText(403) == "próba po³o¿enia pionka na ju¿ zajetym miejscu");
		assertTrue(Message.getErrorText(0) == "");
	}

	@Test
	public void testGetNewGameMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getNewGameMessage(0));
		assertEquals("OK", jsonObject.getString("status").trim());
		assertEquals("0", jsonObject.getString("port").trim());
	}

	@Test
	public void testGetChooseSideMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getChooseSideMessage());
		assertEquals("WHO STARTS", jsonObject.getString("status").trim());
	}

	@Test
	public void testGetPlayerTurnMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getPlayerTurnMessage());
		assertEquals("TURN", jsonObject.getString("status").trim());
		assertEquals("YOUR", jsonObject.getString("attribute").trim());
	}

	@Test
	public void testGetOponentTurnMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getOponentTurnMessage());
		assertEquals("TURN", jsonObject.getString("status").trim());
		assertEquals("OPONENT", jsonObject.getString("attribute").trim());
	}

	@Test
	public void testGetDisconnectedMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getDisconnectedMessage());
		assertEquals("DISCONNECTED", jsonObject.getString("status").trim());
	}

	@Test
	public void testGetOponentPlaceMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getOponentPlaceMessage(1, 2));
		assertEquals("PLACE", jsonObject.getString("status").trim());
		assertEquals("1", jsonObject.getString("x").trim());
		assertEquals("2", jsonObject.getString("y").trim());
	}

	@Test
	public void testGetConnectedMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getConnectedMessage());
		assertEquals("CONNECTED", jsonObject.getString("status").trim());
	}

	@Test
	public void testGetPongMessage() {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(Message
				.getPongMessage());
		assertEquals("PONG", jsonObject.getString("status").trim());
	}

}
