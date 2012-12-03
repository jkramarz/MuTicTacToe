package client;

import static org.junit.Assert.*;

import java.awt.Dimension;

import org.junit.Test;

public class MainMenuTest {

	@Test
	public void testMainMenu() {
		MainMenu m = new MainMenu();
		Dimension d = m.getSize();
		assertTrue("Is actual height declared height?", d.height == m.HEIGHT);
		assertTrue("Is actual width declared width?", d.width == m.WIDTH);
		assertTrue("Is main menu visible?", m.isVisible());
	}

}
