package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest {

	@Test
	public void testBoard() {
		try {
			Board board = new Board(null, null, 0);
			assertTrue(board.img.length == board.nImages);
			board = null;
			fail();
		} catch (Exception e) {

		}
	}

	@Test
	public void testNewGame() {
		try {
			Board board = new Board(null, null, 0);

			board.newGame();
			assertTrue(board.rows == 10);
			assertTrue(board.rows == board.cols);
			assertTrue(board.all_cells == board.rows * board.cols);
			assertTrue(board.all_cells == board.left_cells);
			fail();
		} catch (Exception e) {

		}
	}

	@Test
	public void testPaintGraphics() {
		try {
			Board board = new Board(null, null, 0);
			board.paint(null);
			fail();
		} catch (Exception e) {

		}
	}

}
