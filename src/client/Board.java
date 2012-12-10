package client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class Board extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7710401119244316016L;

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
	volatile int[] field; // tablica zawartosci komorki
	int rows = 10; // ilosc wierszy
	int cols = 10; // ilosc kolumn
	int all_cells = rows * cols; // laczna ilosc komorek
	int left_cells = all_cells; // komorki niezapelnione
	volatile boolean isMyTurn = false; // id gracza wykonujacego ruch (true=local, false=socket)
	// private static int ME = 1; private static int HIM = 2;

	JLabel statusbar; // pasek stanu

	Socket socket;
	volatile BufferedReader bufferedReader;
	volatile BufferedWriter bufferedWritter;

	// ladujemy obrazki do tablicy
	public Board(JLabel statusbar, String host, int port)
			throws UnknownHostException, IOException {
		this.statusbar = statusbar;

		socket = new Socket(host, port);
		bufferedReader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		bufferedWritter = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		JOptionPane.showMessageDialog(null, host + port);

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
		
		for (int i = 0; i < all_cells; i++)
			field[i] = cBlank;

		// pasek stanu
		statusbar.setText(isMyTurn ? "Your Turn." : "Waiting for opponent...");

		@SuppressWarnings("rawtypes")
		class Worker extends SwingWorker {
			boolean end = false;

			@Override
			protected Object doInBackground() throws Exception {
				while (!end) {
					JSONObject jsonObject = (JSONObject) JSONSerializer
							.toJSON(bufferedReader.readLine());
					switch (jsonObject.getString("status").toUpperCase()) {
					case "TURN":
						switch (jsonObject.getString("attribute")) {
						case "YOUR":
							isMyTurn = true;
							break;
						case "OPONENT":
							isMyTurn = false;
							break;
						}
						break;
					case "PLACE":
						Integer x = new Integer(jsonObject.getString("X"));
						Integer y = new Integer(jsonObject.getString("Y"));
						field[getFieldId(x, y)] = cHis;
						repaint();
						break;
					case "WIN":
						switch (jsonObject.getString("attribute")) {
						case "YOUR":
							//TODO
						case "OPONENT":
							//TODO
						}
						break;
					case "DISCONNECT":
						switch (jsonObject.getString("attribute")) {
						case "SERVER":
							//TODO
						case "OPONENT":
							//TODO
						}
						break;
					case "CONNECTED":
						//TODO
						break;
					default:
						throw new Exception();
					}

				}
				// TODO Auto-generated method stub
				return null;
			}

		}

		Worker worker = new Worker();
		worker.execute();

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

	int getFieldId(int cRow, int cCol) {
		return (cRow * cols) + cCol;
	}

	// obserwator klikniec myszki
	class BoardAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {

			// nie rob nic jezeli ruch nalezy do przeciwnika
			if (!isMyTurn)
				return;

			// zamiana wspolrzednych na konkretne pole
			int x = e.getX();
			int y = e.getY();

			int cCol = x / cSize;
			int cRow = y / cSize;

			int field_id = getFieldId(cRow, cCol); // id pola, uzywany w tablicy
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
