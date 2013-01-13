package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardJPanelTest {

	@Test
	public void testBoard() {
		try {
			BoardJPanel boardJPanel = new BoardJPanel(null, null, 0, null);
			assertTrue(boardJPanel.img.length == boardJPanel.nImages);
			boardJPanel = null;
			fail();
		} catch (Exception e) {

		}
	}

	@Test
	public void testNewGame() {
		try {
			BoardJPanel boardJPanel = new BoardJPanel(null, null, 0, null);

			boardJPanel.newGame();
			assertTrue(boardJPanel.rows == 10);
			assertTrue(boardJPanel.rows == boardJPanel.cols);
			assertTrue(boardJPanel.all_cells == boardJPanel.rows * boardJPanel.cols);
			assertTrue(boardJPanel.all_cells == boardJPanel.left_cells);
			fail();
		} catch (Exception e) {

		}
	}

	@Test
	public void testPaintGraphics() {
		try {
			BoardJPanel boardJPanel = new BoardJPanel(null, null, 0, null);
			boardJPanel.paint(null);
			fail();
		} catch (Exception e) {

		}
	}

}
