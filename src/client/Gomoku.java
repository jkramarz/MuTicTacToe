package client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Gomoku extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9216930946594064363L;
	// wymiary okienka
	private final int WIDTH = 170;
	private final int HEIGHT = 210;

	private JLabel statusbar;
	
	public Gomoku(int gameType)
	{
		// parametry okna
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("5-in-a-row");
        
        // pasek stanu
        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);

        add(new Board(statusbar, gameType));

        // wyswietlenie okna
        setResizable(false);
        setVisible(true);
        
        
	}
	
	public static void main(String[] args)
	{
		MainMenu m = new MainMenu();
		
		//new Gomoku();
		
	}
	
}
