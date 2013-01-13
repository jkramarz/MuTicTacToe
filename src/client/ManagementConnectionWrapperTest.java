package client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.ConnectException;

import org.junit.Test;

public class ManagementConnectionWrapperTest {

	@Test
	public void testGameHostValue() throws IOException {
		String expected_host = "localhost";
		Integer expected_port = 6666;
		ManagementConnectionWrapper t = null;
		try {
			t = new ManagementConnectionWrapper(expected_host, expected_port);
		} catch (ConnectException e) {
		}
		if (t != null) {
			assertEquals(expected_host, t.getGamehost());
			assertEquals(expected_port, t.getGameport());
		}
	}

}
