package server;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class ServerTest {

	class Main implements Runnable {
		@Override
		public void run() {
			try {
				Server.main(null);
			} catch (IOException e) {
				fail(e.getMessage());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testMain() throws IOException, InterruptedException {
		Thread t = new Thread(new Main());
		t.start();
		Thread.sleep(3);
		if (Server.serversocket != null) {
			assertTrue("Server is bound to port", Server.serversocket.isBound());
			assertTrue("Server is bound to specified port",
					Server.serversocket.getLocalPort() == Server.port);
		}
		t.stop();
	}

}
