package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Game extends Thread{
	private int port;
	private ServerSocket serversocket;
	private ArrayList<Player> players;
	
	@SuppressWarnings("unused")
	private Game(){}
	
	public Game(int i) {
		port = i;
	}

	public void run(){
		try {
			serversocket = new ServerSocket(port);
			for(int i = 0; i < 2; i++){
				Player player = new Player(serversocket.accept());
				players.add(player);

			}
			
			do{
				
			}while(players.get(0).isConnected() && players.get(1).isConnected());
			serversocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
