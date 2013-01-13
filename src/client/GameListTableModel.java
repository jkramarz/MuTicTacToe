package client;
import java.io.Serializable;
import java.util.List;

import javax.swing.table.AbstractTableModel;

class GameListTableModel extends AbstractTableModel implements Serializable {

	private static final long serialVersionUID = -1248237209971702523L;
	List<List<String>> data;

	public GameListTableModel(List<List<String>> data) {
		this.data = data;
	}


	public int getColumnCount() {
		return 1;
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return "Port";
	}

	public Object getValueAt(int row, int col) {
		return data.get(row).get(col);
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}
}
