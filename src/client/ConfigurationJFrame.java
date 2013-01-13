package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.uncommons.swing.SpringUtilities;

public class ConfigurationJFrame extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField filterJText;

	public ConfigurationJFrame() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel form = new JPanel(new SpringLayout());
		JLabel l1 = new JLabel("Nazwa hosta:");
		form.add(l1);

		filterJText = new JTextField();
		l1.setLabelFor(filterJText);
		form.add(filterJText);

		SpringUtilities.makeCompactGrid(form, 1, 2, 0, 0, 0, 0);

		add(form);

		JPanel addDeletePanel = new JPanel(new SpringLayout());

		JButton saveButton = new JButton("Zapisz");
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveConfig();

			}
		});

		addDeletePanel.add(saveButton);

		SpringUtilities.makeCompactGrid(addDeletePanel, 1, 1, 0, 0, 0, 0);
		add(addDeletePanel);
	}

	private void saveConfig() {
		try {
			MainMenuJFrame.mc = new ManagementConnectionWrapper(
					filterJText.getText());
			MainMenuJFrame.ref.host = filterJText.getText(); 
			JOptionPane.showMessageDialog(null, "Po³¹czenie nawi¹zane z sukcesem.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "B³¹d po³¹czenia.");
		}
	}

	static void createAndShowGUI() {
		JFrame frame = new JFrame("Konfiguracja");		
		frame.setContentPane(new ConfigurationJFrame());
		frame.pack();
		frame.setVisible(true);
	}
}