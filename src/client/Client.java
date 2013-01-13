package client;

public class Client {
	public static void main(String[] args) {
		MainMenuJFrame m = new MainMenuJFrame();
		try {
			m.initComponents();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
