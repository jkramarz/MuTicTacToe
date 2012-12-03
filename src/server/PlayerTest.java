package server;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;

import javax.naming.directory.InvalidAttributesException;

import org.junit.Test;

public class PlayerTest {

	@Test
	public void testPlayer() throws IOException, InvalidAttributesException {
		@SuppressWarnings("unused")
		Player p;
		try{
			p = new Player(null);
		}catch(InvalidAttributesException e){
			assertTrue(e.getMessage() == "Player socket must exists!");
		}
		
		try{
			Socket s = new Socket();
			p = new Player(s);
		}catch(InvalidAttributesException e){
			assertTrue(e.getMessage() == "Player socket must be connected!");
		}
		
	}
}
