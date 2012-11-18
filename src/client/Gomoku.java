package client;

import javax.swing.*;

import client.Board;

import java.awt.*;

public class Gomoku extends JFrame {
	
	// wymiary okienka
	private final int WIDTH = 170;
	private final int HEIGHT = 210;

	private JLabel statusbar;
	
	public Gomoku()
	{
		// parametry okna
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("5-in-a-row");
        
        // pasek stanu
        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);

        add(new Board(statusbar));

        // wyswietlenie okna
        setResizable(false);
        setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Gomoku();
	}
	
}
