package messages;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class ErrorMessageTest {

	@Test
	public void testGetErrorText() {
		assertTrue(Message.getErrorMessage(503).getErrorText() == "brak mo�liwo�ci stworzenia gry ze wzgledu na brak dostepnych wolnych portow");
		assertTrue(Message.getErrorMessage(501).getErrorText() == "dana metoda jest niezaimplementowana");
		assertTrue(Message.getErrorMessage(409).getErrorText() == "przes�any komunikat nieadekwatny do obecnego stanu");
		assertTrue(Message.getErrorMessage(404).getErrorText() == "przes�any komunikat niezgodny z protoko�em");
		assertTrue(Message.getErrorMessage(403).getErrorText() == "pr�ba po�o�enia pionka na ju� zajetym miejscu");
		assertTrue(Message.getErrorMessage(0).getErrorText() == "");
	}
	
	@Test
	public void testGetErrorCode(){
		Random r = new Random();
		int i = r.nextInt();
		assertTrue(Message.getErrorMessage(i).getErrorCode() == i);
	}

}
