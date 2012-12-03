package server;

import static org.junit.Assert.*;

import org.junit.Test;

import server.Game.state;

public class GameTest {


	@Test
	public void testGameInt() {
		Game game = new Game(0);
		for(int x = 0; x < 10; x++){
			for(int y = 0; y < 10; y++){
				assertTrue("Field " + x + "x" + y + " should be blank.", game.fields[x][y] == Game.state.BLANK);
			}
		}
		assertTrue("Game with no name should not have name.", game.gameName == null);
	}
	
	@Test
	public void testGameIntString() {
		String name = "name";
		Game game = new Game(0, name);
		assertTrue("Game with name should have name.", game.gameName == name);
	}

	@Test
	public void testGetGameName() {
		Game game;
		game = new Game(0);
		assertTrue("Game with no name should not have name.", game.getGameName() == null);
		String name = "name";
		game = new Game(0, name);
		assertTrue("Game with name should have name.", game.getGameName() == name);
	}

	@Test
	public void testRun() {
		fail("Not yet implemented");
	}
}
