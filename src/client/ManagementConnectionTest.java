package client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ManagementConnectionTest {

	@Test
	public void testGameHostValue() {
		String expected_host = "name of the host";
		Integer expected_port = 6666;
		ManagementConnection t = new ManagementConnection(expected_host, expected_port);
		assertEquals(expected_host, t.getGamehost());
		assertEquals(expected_port, t.getGameport());
	}

}
