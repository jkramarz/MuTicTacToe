package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.table.TableRowSorter;

import org.uncommons.swing.SpringUtilities;

public class GameListTable extends JPanel {
	private static final long serialVersionUID = 1L;

	private GameListTableModel gameListTableModel;
	private TableRowSorter<GameListTableModel> sorter;
	private JTable jTable;
	private static MainMenuJFrame mm;
	public GameListTable(List<List<String>> list) {
		gameListTableModel = new GameListTableModel(list);
		sorter = new TableRowSorter<GameListTableModel>(gameListTableModel);
		jTable = new JTable(gameListTableModel);
		
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		jTable.setRowSorter(sorter);
		jTable.setPreferredScrollableViewportSize(new Dimension(70, 500));
		jTable.setFillsViewportHeight(true);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		add(new JScrollPane(jTable));

		JPanel addDeletePanel = new JPanel(new SpringLayout());
		JButton addButton = new JButton("Po³¹cz");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = jTable.getSelectedRow();
				if (row == -1)
					return;
				else{
					new BoardJFrame(mm.host, Integer.parseInt((String) gameListTableModel.getValueAt(jTable.convertRowIndexToModel(row), 0)), mm);
				}
			}
		});
	

		addDeletePanel.add(addButton);

		SpringUtilities.makeCompactGrid(addDeletePanel, 1, 1, 0, 0, 0, 0);
		add(addDeletePanel);
	}

	static void createAndShowGUI(List<List<String>> list, MainMenuJFrame mainMenuJFrame) {
		mm = mainMenuJFrame;
		JFrame frame = new JFrame("Lista gier");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new GameListTable(list));
		frame.pack();
		frame.setVisible(true);
	}
}