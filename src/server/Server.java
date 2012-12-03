/**
 * 
 */
package server;
import sun.misc.Signal;
import sun.misc.SignalHandler;
import java.io.*;
import java.net.*;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.*;

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
		//try{
			serversocket = new ServerSocket(port);
			while(!interupted){
				Socket socket = serversocket.accept();
				try{
					BufferedReader bufferedReader = 
				            new BufferedReader(
				                new InputStreamReader(
				                    socket.getInputStream()
				                )
				            );
					BufferedWriter bufferedWritter =
							new BufferedWriter(
								new OutputStreamWriter(
									socket.getOutputStream()
								)
							);
					while(!socket.isClosed() && !interupted){
						String command = bufferedReader.readLine();
						System.err.println(command);
						String result = routeCommand(command);
						System.err.println(result);
						bufferedWritter.write(result + "\n");
						bufferedWritter.flush();
					}
					socket.close();
				}catch(Exception e){
					socket.close();
					System.err.println(e.getMessage());
				}
			}
			serversocket.close();
			
			
		/*}catch(Exception e){
			System.err.println("Whoops! ;-(");
		}*/
	}
	private static String routeCommand(String command) {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(command);
		switch(jsonObject.getString("action").toUpperCase()){
			case "NEW GAME":
				if(jsonObject.containsKey("name")){
					return newGame(jsonObject.getString("name"));
				}else{
					return newGame(null);
				}
			case "LIST GAMES":
				return listGames();
			case "PING":
				return pong();
			default:
				return Message.getErrorMessage(404);
		}
	}
	
	
	private static String listGames() {
		/*ArrayList<Map<String, String>> gamesList = new ArrayList<>();
		for(int port : games.keySet()){
			if(games.get(port) != null){
				Map<String, String> game = new HashMap<>();
				game.put("port", new Integer(port).toString());
				game.put("name", games.get(port).getGameName());
				gamesList.add(game);
			}
		}
		Map<String, Object> message = new HashMap<>();
		message.add("status", "OK");
		message.add("games", gamesList);*/
		
		return Message.getErrorMessage(401);
	}
	private static String newGame(String name) {
		System.err.println("Szukanie wolnego portu");
		for(int i = port_start; i <= port_end; i++){
			System.err.println("Port " + i);
			if(games.containsKey(i) && !games.get(i).isAlive()){
				System.err.println(" nieaktywana gra, zwalniam port");
				games.remove(i);
			}
			if(!games.containsKey(i)){
				System.err.println(" wolny port, rozpoczynam grê");
				Game game;
				if(name != null){
					game = new Game(i, name);
				}else{
					game = new Game(i);
				}
				games.put(i, game);
				game.start();
			
				return Message.getNewGameMessage(i);
			}
		}
		return Message.getErrorMessage(503);
	}
	
	private static String pong() {
		System.err.println("PING -> PONG");
		return Message.getPongMessage();
	}
	
	private static void setupSignalHandler(){
		Signal.handle(
			new Signal("INT"),
			new SignalHandler () {
			    public void handle(Signal sig) {
			      interupted=true;
			    }
			}
		);
	}

}
