package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class GomokuTest {

	@Test
	public void testGomoku() {
		Client client = new Client(0);
		assertNotNull("Gomoku can be created", client);
	}

	@Test
	public void testMain() {
		Client.main(null);
	}

}
