package client;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Client extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9216930946594064363L;
	// wymiary okienka
	private final int WIDTH = 170;
	private final int HEIGHT = 210;

	private JLabel statusbar;

	public Client(String host, int port) {
		try {
			// parametry okna
			// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(WIDTH, HEIGHT);
			setLocationRelativeTo(null);
			setTitle("5-in-a-row");
			// pasek stanu
			statusbar = new JLabel("");
			add(statusbar, BorderLayout.SOUTH);
			//nowa gra
			add(new Board(statusbar, host, port));
			// wyswietlenie okna
			setResizable(false);
			setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		MainMenu m = new MainMenu();
		try {
			m.initComponents();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// new Gomoku();

	}

}
