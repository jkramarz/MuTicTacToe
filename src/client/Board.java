package client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import messages.ChatMessage;
import messages.ChooseSidesMessage;
import messages.DisconnectMessage;
import messages.LoseMessage;
import messages.Message;
import messages.PlaceMessage;
import messages.WinMessage;
import messages.YourTurnMessage;

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
	volatile boolean isMyTurn = false; // id gracza wykonujacego ruch
										// (true=local, false=socket)
	// private static int ME = 1; private static int HIM = 2;

	JLabel statusbar; // pasek stanu

	Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	// ladujemy obrazki do tablicy
	public Board(JLabel statusbar, String host, int port)
			throws UnknownHostException, IOException {
		this.statusbar = statusbar;

		socket = new Socket(host, port);
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		inputStream = new ObjectInputStream(socket.getInputStream());

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

		class Worker extends Thread {
			@Override
			public void run() {
				while (socket.isConnected()) {
					try {
						if (inputStream.available() > 0) {
							Object o = inputStream.readObject();
							System.err.println("<=" + o.toString());
							if (o instanceof Message) {
								if (o instanceof YourTurnMessage) {
									isMyTurn = true;
								} else if (o instanceof ChooseSidesMessage) {
									outputStream.writeObject(Message
											.getSidesMessage(0));
								} else if (o instanceof ChatMessage) {
									// TODO
								} else if (o instanceof PlaceMessage) {
									PlaceMessage m = (PlaceMessage) o;
									field[getFieldId(m.getX(), m.getY())] = cHis;
									repaint();
								} else if (o instanceof WinMessage) {
									// TODO
								} else if (o instanceof LoseMessage) {
									// TODO
								} else if (o instanceof DisconnectMessage) {
									// TODO
								} else {

								}
							}
						} else {
							Thread.sleep(50);
						}
					} catch (ClassNotFoundException | IOException
							| InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return;
			}

		}

		Worker worker = new Worker();
		worker.start();

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

	Map<String, String> getCoordsFromFieldId(int id) {
		Map<String, String> result = new HashMap<>();
		result.put("x", new Integer(id % cols).toString());
		result.put("y", new Integer(id / cols).toString());
		return result;
	}

	// obserwator klikniec myszki
	class BoardAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {

			// nie rob nic jezeli ruch nalezy do przeciwnika
			/*
			 * if (!isMyTurn) return;
			 */

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
						try {
							Map<String, String> fieldId = getCoordsFromFieldId(field_id);
							PlaceMessage m = Message.getPlaceMessage(
									fieldId.get("x"), fieldId.get("y"));
							System.err.println("=>" + m.toString());
							outputStream.writeObject(m);

							field[field_id] = cMine;
							left_cells--;
							rep = true; // zmien flage - trzeba zaktualizowac
										// plansze
							isMyTurn = !isMyTurn;
							statusbar.setText(isMyTurn ? "Your Turn."
									: "Waiting for opponent...");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					} else
						// TODO: negative feedback
						return;
				}

				if (rep) {
					repaint();
				}

			}
		}
	}

}
