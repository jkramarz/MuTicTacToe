package client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7710401119244316016L;
	// parametry rodzaju gry
	private static int local = 0;
	private static int network = 1;

	private int gameType;

	// zmienne dotyczace grafiki
	int nImages = 3; // liczba obrazkow w grze (0=blank,1=black,2=blue)
	int cSize = 16; // rozmiar komorki w pikselach
	Image[] img; // tablica obrazkow poszczegolnych pol

	// kody stanow komorek
	public static final int cBlank = 0;
	public static final int cBlack = 1;
	public static final int cBlue = 2;

	// pionek gracza
	int cMine = cBlack;
	int cHis = cBlue;

	// gameplay variables
	int[] field; // tablica zawartosci komorki
	int rows = 10; // ilosc wierszy
	int cols = 10; // ilosc kolumn
	int all_cells = rows*cols; // laczna ilosc komorek
	int left_cells = all_cells; // komorki niezapelnione
	boolean isMyTurn; // id gracza wykonujacego ruch (true=local, false=socket)
	// private static int ME = 1; private static int HIM = 2;

	JLabel statusbar; // pasek stanu

	// ladujemy obrazki do tablicy
	public Board(JLabel statusbar, int gameType) {
		this.statusbar = statusbar;
		this.gameType = gameType;

		img = new Image[nImages];

		// pobieramy obrazki z paczki o nazwach 0.png, 1.png...nImages.png
		for (int i = 0; i < nImages; i++) {
			img[i] = (new ImageIcon(this.getClass().getResource((i) + ".png")))
					.getImage();
		}
		setDoubleBuffered(true); // rysowanie odbywa sie w buforze poza ekranem

		// podpinamy obserwatora i inicjujemy gre
		addMouseListener(new BoardAdapter());
		newGame();
	}

	// inicjalizacja nowej gry
	public void newGame() {
		// inicjalizacja tablicy zawartosci komorek
		all_cells = rows * cols;
		left_cells = all_cells;
		field = new int[all_cells];

		isMyTurn = true;

		for (int i = 0; i < all_cells; i++)
			field[i] = cBlank;

		// pasek stanu
		statusbar.setText(isMyTurn ? "Your Turn." : "Waiting for opponent...");

	}

	// rysowanie planszy
	public void paint(Graphics g) {
		int cell = 0; // id aktualnie rysowanej komorki

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				cell = field[(i * cols) + j];
				g.drawImage(img[cell], (j * cSize), (i * cSize), this);
			}
		}
	}

	// obserwator klikniec myszki
	class BoardAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {

			// nie rob nic jezeli ruch nalezy do przeciwnika
			if (!isMyTurn && (gameType == network))
				return;

			// zamiana wspolrzednych na konkretne pole
			int x = e.getX();
			int y = e.getY();

			int cCol = x / cSize;
			int cRow = y / cSize;

			int field_id = (cRow * cols) + cCol; // id pola, uzywany w tablicy
													// fields[]

			// flaga - koniecznosc aktualizacji planszy
			boolean rep = false;

			// przechwytujemy klikniecie myszki jezeli jest w zakresie planszy
			if ((x < (cols * cSize)) && (y < (rows * cSize))) {
				// chwytamy tylko lewy przycisk
				if (e.getButton() == MouseEvent.BUTTON1) {
					// poloz pionek tylko gdy pole jest puste
					if (field[field_id] == cBlank) {
						field[field_id] = cMine;
						left_cells--;
						rep = true; // zmien flage - trzeba zaktualizowac
									// plansze
						isMyTurn = !isMyTurn;
						statusbar.setText(isMyTurn ? "Your Turn."
								: "Waiting for opponent...");
					} else
						// TODO: negative feedback
						return;
				}

				if (e.getButton() == MouseEvent.BUTTON3) {
					if (field[field_id] == cBlank) {
						field[field_id] = 3 - cMine;
						left_cells--;
						rep = true;
						isMyTurn = !isMyTurn;
						statusbar.setText(isMyTurn ? "Your Turn."
								: "Waiting for opponent...");
					}
				}

				if (rep) {
					repaint();
				}

			}
		}
	}

}
