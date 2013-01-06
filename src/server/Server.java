/**
 * 
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
			Socket socket = serversocket.accept();
			Thread t = new Thread(new Connection(socket));
			t.start();
		}
		serversocket.close();

		/*
		 * }catch(Exception e){ System.err.println("Whoops! ;-("); }
		 */
	}

	static String newGame(String name) {
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
				if (name != null) {
					game = new Game(i, name);
				} else {
					game = new Game(i);
				}
				games.put(i, game);
				game.start();

				return Message.getNewGameMessage(i);
			}
		}
		return Message.getErrorMessage(503);
	}

	static String listGames() {
		/*
		 * ArrayList<Map<String, String>> gamesList = new ArrayList<>(); for(int
		 * port : games.keySet()){ if(games.get(port) != null){ Map<String,
		 * String> game = new HashMap<>(); game.put("port", new
		 * Integer(port).toString()); game.put("name",
		 * games.get(port).getGameName()); gamesList.add(game); } } Map<String,
		 * Object> message = new HashMap<>(); message.add("status", "OK");
		 * message.add("games", gamesList);
		 */

		return Message.getErrorMessage(401);
	}

	private static void setupSignalHandler() {
		Signal.handle(new Signal("INT"), new SignalHandler() {
			public void handle(Signal sig) {
				interupted = true;
			}
		});
	}

	static String pong() {
		System.err.println("PING -> PONG");
		return Message.getPongMessage();
	}
}
