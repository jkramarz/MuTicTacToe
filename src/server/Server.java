/**
 * 
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import messages.Message;
import messages.NewGameRequestMessage;
import sun.misc.Signal;
import sun.misc.SignalHandler;

//import java.util.ArrayList;

/**
 * @author lenwe
 * 
 */
public class Server {
	static int port = 10001;
	static int port_start = 10010;
	static int port_end = 10020;

	static Map<Integer, Game> games = new HashMap<>();
	private static Boolean interupted = false;
	static ServerSocket serversocket;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		System.err.println("Server is starting up...");
		setupSignalHandler();
		// try{
		serversocket = new ServerSocket(port);
		while (!interupted) {
			try {
				Socket socket = serversocket.accept();
				Thread t = new Thread(new ManagementConnection(socket));
				t.start();
			} catch (Exception e) {
			}
		}
		serversocket.close();

		/*
		 * }catch(Exception e){ System.err.println("Whoops! ;-("); }
		 */
	}

	static Message listGames() {
		HashMap<Integer, String> list = new HashMap<>();	
		 for(int port : games.keySet()){
			 if(games.get(port) != null && games.get(port).isAlive()){
				 list.put(port, String.valueOf(port));
			 }
		 }
		return Message.getGameListMessage(list);
	}

	private static void setupSignalHandler() {
		Signal.handle(new Signal("INT"), new SignalHandler() {
			public void handle(Signal sig) {
				interupted = true;
			}
		});
		
		Signal.handle(new Signal("TERM"), new SignalHandler() {
			public void handle(Signal sig) {
				try
                {
                    serversocket.close();
                }
                catch(IOException e)
                {
                    System.err.println("Nie uda³o siê odbindowaæ, smutne.");
                }
			}
		});
	}

	static Message pong() {
		System.err.println("PING -> PONG");
		return Message.getPongMessage();
	}

	static Message newGame(NewGameRequestMessage m) {
		return newGame(m.getType(), m.getName());
	}

	static Message newGame(String type, String name) {
		System.err.println("Szukanie wolnego portu");
		for (int i = port_start; i <= port_end; i++) {
			System.err.println("Port " + i);
			if (games.containsKey(i) && !games.get(i).isAlive()) {
				System.err.println(" nieaktywana gra, zwalniam port");
				games.remove(i);
			}
			if (!games.containsKey(i)) {
				System.err.println(" wolny port, rozpoczynam grê");
				Game game;
				try {
					game = new Game(i, type, name);
					games.put(i, game);
					game.start();
					return Message.getNewGameMessage(i);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Message.getErrorMessage(503);
	}
}
