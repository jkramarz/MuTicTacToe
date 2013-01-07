package client;

public class Client {
	public static void main(String[] args) {
		MainMenu m = new MainMenu();
		try {
			m.initComponents();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
