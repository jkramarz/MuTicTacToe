package client;

import static org.junit.Assert.assertNotNull;

public class BoardJFrameTest {

	//@Test
	public void testGomoku() {
		BoardJFrame boardJFrame = new BoardJFrame("localhost", 10001, null);
		assertNotNull("Gomoku can be created", boardJFrame);
	}


}
