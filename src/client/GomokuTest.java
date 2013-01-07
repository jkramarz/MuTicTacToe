package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class GomokuTest {

	//@Test
	public void testGomoku() {
		BoardFrame boardFrame = new BoardFrame("localhost", 10001, null);
		assertNotNull("Gomoku can be created", boardFrame);
	}


}
