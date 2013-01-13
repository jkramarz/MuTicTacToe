package client;

import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import messages.Message;
import messages.PongMessage;

public class MainMenuJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3059271023307939391L;
	// parametry rodzaju gry
	final static int local = 0;
	final static int network = 1;

	// wymiary okienka
	int WIDTH = 170;
	int HEIGHT = 210;

	// TODO
	String host = "localhost";
	private ManagementConnectionWrapper mc;

	public MainMenuJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setTitle("5-in-a-row");

		// wyswietlenie okna
		setResizable(false);
		setVisible(true);
	}

	/**
	 * @param args
	 */
	/*
	 * public static void main(String[] args) { MainMenu m = new MainMenu();
	 * 
	 * }
	 */

	private javax.swing.JButton pvpButton = new javax.swing.JButton(
			"Nowa gra PvP");
	private javax.swing.JButton pvcButton = new javax.swing.JButton(
			"Nowa gra PvC");
	private javax.swing.JButton joinButton = new javax.swing.JButton(
			"Do³¹cz do gry");
	private javax.swing.JButton exitButton = new javax.swing.JButton("Wyjœcie");
	private javax.swing.JLabel jLabel1 = new javax.swing.JLabel("Gomoku");
	private javax.swing.JPanel jPanel1 = new javax.swing.JPanel();

	void initComponents() throws InterruptedException {

		try {
			mc = new ManagementConnectionWrapper(host, 10001);
			if(mc.sendCommand(Message.getPingMessage()) instanceof PongMessage){
				//JOptionPane.showMessageDialog(null, "Connection successfull.");
			}else{
				throw new Exception();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Connection failed.");
		}

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		// napis g³ówny
		jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		// BUTTON — nowa gra PvP
		pvpButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				pvpButtonMouseClicked(evt);
			}
		});

		// BUTTON — nowa gra PvC
		pvcButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				pvcMouseClicked(evt);
			}
		});

		// BUTTON — do³¹cz do gry
		joinButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				joinMouseClicked(evt);
			}
		});

		// button — wyjœcie
		exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				exitMouseClicked(evt);
			}
		});

		// layout
		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				jLabel1,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				449,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addGap(191,
																				191,
																				191)
																		.addGroup(
																				jPanel1Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								false)
																						.addComponent(
																								pvcButton,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								pvpButton,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								joinButton,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								exitButton,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel1Layout.createSequentialGroup()
						.addContainerGap(22, Short.MAX_VALUE)
						.addComponent(jLabel1).addGap(30, 30, 30)
						.addComponent(pvpButton).addGap(26, 26, 26)
						.addComponent(pvcButton).addGap(26, 26, 26)
						.addComponent(joinButton).addGap(26, 26, 26)
						.addComponent(exitButton)
						.addContainerGap(76, Short.MAX_VALUE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jPanel1,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jPanel1,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addContainerGap()));

		pack();
	}

	private void pvpButtonMouseClicked(java.awt.event.MouseEvent evt) {
		Integer port;
		try {
			port = mc.createNewPvpGame();
			this.setVisible(false);
			new BoardJFrame(host, port, this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pvcMouseClicked(java.awt.event.MouseEvent evt) {
		Integer port;
		try {
			port = mc.createNewPvcGame();
			this.setVisible(false);
			new BoardJFrame(host, port, this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void joinMouseClicked(java.awt.event.MouseEvent evt) {
		this.setVisible(false);
		try {
			List<List<String>> list = mc.getGamesList();
			GameListTable.createAndShowGUI(list, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//new BoardFrame(host, lastport, this);
	}

	private void exitMouseClicked(java.awt.event.MouseEvent evt) {
		dispose();
	}

}
