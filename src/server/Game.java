package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class Game extends Thread{
	private int port;
	private ServerSocket serversocket;
	private ArrayList<Player> players;
	private int turn;
	private String gameName;
	int[][] fields;
	
	@SuppressWarnings("unused")
	private Game(){}
	
	public Game(int i, String n){
		this(i);
		gameName = n;
	}
	
	public Game(int i) {
		port = i;
		fields = new int[10][10];
		for(int x = 0; x < 10; x++){
			for(int y = 0; y < 10; y++){
				fields[x][y]=-1;
			}
		}
	}

	public String getGameName(){
		return gameName;
	}
	
	public void run(){
		System.err.println("W¹tek Game na porcie " + port);
		players = new ArrayList<>();
		try {
			serversocket = new ServerSocket(port);
		}catch(Exception e){
			System.err.println("Nie uda³o siê zaj¹c portu " + port);
		}
		
		try{
			for(int i = 0; i < 2; i++){
				Player player = new Player(serversocket.accept());
				if(i == 0){
					player.writer.write(Message.getChooseSideMessage());
					player.writer.flush();
					String action = ((JSONObject) JSONSerializer.toJSON(
													player.reader.readLine()
												  )
							        ).getString("action");
					if(action == "ME"){
						turn = 0;
					}else{
						turn = 1;
					}
				}else{
					players.get(0).writer.write(Message.getConnectedMessage());
					players.get(0).writer.flush();
				}
				players.add(player);
			}
		}catch(Exception e){
			System.err.println("Nie uda³o siê uzyskac po³¹czenia z graczami.");
		}
		
		try{
			Boolean end_of_game = false;
			do{
				sendTurns();
				end_of_game = !game();
				turn = getOponentIndex();
			}while(
					!end_of_game &&
					players.get(0).isConnected() && 
					players.get(1).isConnected()
				  );
		}catch (Exception e) {
			for(int i = 0; i <= 1; i++){
				if(!players.get(i).isConnected())
					continue;
				try{
					System.err.println("Problem z kontynuowaniem gry, roz³¹czam graczy");
					players.get(i).writer.write(
						Message.getDisconnectedMessage()
					);
					players.get(i).writer.flush();
					players.get(i).socket.close();
				}catch(Exception ex){
					System.err.println("Nie uda³o siê roz³¹czyc graczy");
					return;
				}
			}
			e.printStackTrace();
		}
		
		try{
			serversocket.close();
		}catch(IOException e){
			System.err.println("Nie uda³o siê zamkn¹c socketu.");
		}
	}

	private Boolean game() throws IOException{
		
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(
									players.get(turn).reader.readLine()
								);
		switch(jsonObject.getString("action").toUpperCase()){
		
			case "PLACE":
				int x = jsonObject.getInt("x");
				int y = jsonObject.getInt("y");
				if(x < 0 || x >= 10 || y < 0 || y >= 10){
					players.get(getOponentIndex()).writer.write(
								Message.getErrorMessage(404)
							);
					players.get(getOponentIndex()).writer.flush();
					game();
				}
				if(fields[x][y] != -1){
					players.get(getOponentIndex()).writer.write(
								Message.getErrorMessage(409)
							);
					players.get(getOponentIndex()).writer.flush();
					game();
				}
				fields[x][y]=turn;
				players.get(getOponentIndex()).writer.write(
						Message.getOponentPlaceMessage(
								x,
								y
						)
				);
				return true;
			
			case "DISCONNECT":
				players.get(turn).socket.close();
				players.get(getOponentIndex()).writer.write(
							Message.getDisconnectedMessage()
						);
				players.get(getOponentIndex()).writer.flush();
				players.get(getOponentIndex()).socket.close();
				return false;
				
			case "CHAT":
			default:
				players.get(turn).writer.write(
							Message.getErrorMessage(401)
						);
				players.get(turn).writer.flush();
				game();
		}
		return true;
		
	}
	
	private void sendTurns() {
		try{
			players.get(turn).writer.write(Message.getPlayerTurnMessage());
			players.get(turn).writer.flush();
			players.get(getOponentIndex()).writer.write(
						Message.getOponentTurnMessage()
					);
			players.get(getOponentIndex()).writer.flush();
		}catch(IOException e){
			System.err.println("Nie mo¿na wys³ac tur.");
		}
	}
	private int getOponentIndex(){
		return (turn+1) % 2;
	}
}
