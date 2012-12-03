package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest {

	@Test
	public void testBoard() {
		Board board = new Board(null, 0);
		assertTrue(board.img.length == board.nImages);
		board = null;
	}

	@Test
	public void testNewGame() {
		Board board = new Board(null, 0);
		board.newGame();
		assertTrue(board.rows == 10);
		assertTrue(board.rows == board.cols);
		assertTrue(board.all_cells == board.rows * board.cols);
		assertTrue(board.all_cells == board.left_cells);
	}

	@Test
	public void testPaintGraphics() {
		Board board = new Board(null, 0);
		board.paint(null);
	}

}
