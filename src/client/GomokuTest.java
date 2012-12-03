package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class GomokuTest {

	@Test
	public void testGomoku() {
		Gomoku gomoku = new Gomoku(0);
		assertNotNull("Gomoku can be created", gomoku);
	}

	@Test
	public void testMain() {
		Gomoku.main(null);
	}

}
