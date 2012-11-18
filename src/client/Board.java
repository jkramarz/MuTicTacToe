package client;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel
{
	// zmienne dotyczace grafiki
	private final int nImages = 3; // liczba obrazkow w grze (0=blank,1=black,2=blue)
	private final int cSize = 16; // rozmiar komorki w pikselach
	private Image[] img; // tablica obrazkow poszczegolnych pol
	
	// kody stanow komorek
	private final int cBlank = 0;
	private final int cBlack = 1;
	private final int cBlue = 2;
	
	// pionek gracza
	private final int cMine=cBlack;
	
	// gameplay variables
	private int[] field; // tablica zawartosci komorki
	private int rows = 10; // ilosc wierszy
	private int cols = 10; // ilosc kolumn
	private int all_cells; // laczna ilosc komorek
	private int left_cells; // komorki niezapelnione
	
	private JLabel statusbar; // pasek stanu
	
	// ladujemy obrazki do tablicy
	public Board(JLabel statusbar) 
	{
        this.statusbar = statusbar;

        img = new Image[nImages];
        
        // pobieramy obrazki z paczki o nazwach 0.png, 1.png...nImages.png
        for (int i=0;i < nImages;i++) 
        {
            img[i] =
                    (new ImageIcon(this.getClass().getResource((i)
                        + ".png"))).getImage();
        }
        setDoubleBuffered(true); // rysowanie odbywa sie w buforze poza ekranem
        
        // podpinamy obserwatora i inicjujemy gre
        addMouseListener(new BoardAdapter());
        newGame();
    }
	
	// inicjalizacja nowej gry
	public void newGame()
	{
		// inicjalizacja tablicy zawartosci komorek
		all_cells = rows * cols;
		left_cells = all_cells;
        field = new int[all_cells];
        
        for (int i=0;i<all_cells;i++) field[i] = cBlank;
        
        // pasek stanu (mozna potem wywalic albo dodac cos innego tu)
        statusbar.setText(Integer.toString(left_cells));
        
	}
	
	// rysowanie planszy
	public void paint(Graphics g) 
	{
		int cell = 0; // id aktualnie rysowanej komorki

        for (int i = 0; i < rows; i++) 
        { 
        	for (int j = 0; j < cols; j++) 
        	{
        		cell = field[(i * cols) + j];
                g.drawImage(img[cell], (j * cSize), (i * cSize), this);
            }
        }
	}
	
	// obserwator klikniec myszki
	class BoardAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
        	
        	// zamiana wspolrzednych na konkretne pole
            int x = e.getX();
            int y = e.getY();

            int cCol = x / cSize;
            int cRow = y / cSize;
            
            int field_id = (cRow*cols) + cCol; // id pola, uzywany w tablicy fields[]
            
            // flaga - koniecznosc aktualizacji planszy
            boolean rep = false;

            if ((x < (cols * cSize))&&(y < (rows * cSize))) 
            {
            	// chwytamy tylko lewy przycisk
            	if(e.getButton() == MouseEvent.BUTTON1)
            	{
            		// poloz pionek tylko gdy pole jest puste
            		if(field[field_id] == cBlank)
            		{
            			field[field_id] = cMine;
            			left_cells--;
            			statusbar.setText(Integer.toString(left_cells));
            			rep = true; // zmien flage - trzeba zaktualizowac plansze
            		} else
            			//TODO: negative feedback
            			return;            	
            	}

                if (rep) repaint();

            }
        }
    }
}


