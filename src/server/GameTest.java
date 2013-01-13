package server;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class GameTest {

	@Test
	public void testGameInt() throws IOException {
		Game game = new Game("PVP", 0);
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				assertTrue("Field " + x + "x" + y + " should be blank.",
						game.board[x][y] == Marker.BLANK);
			}
		}
		assertTrue("Game with no name should not have name.",
				game.gameName == null);
	}

	@Test
	public void testGameIntString() throws IOException {
		String name = "name";
		Game game = new Game(0, "PVP", name);
		assertTrue("Game with name should have name.", game.gameName == name);
	}

	@Test
	public void testGetGameName() throws IOException {
		Game game;
		game = new Game("PVP", 0);
		assertTrue("Game with no name should not have name.",
				game.getGameName() == null);
		String name = "name";
		game = new Game(0, "PVP", name);
		assertTrue("Game with name should have name.",
				game.getGameName() == name);
	}

}
