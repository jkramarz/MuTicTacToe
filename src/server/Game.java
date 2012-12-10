package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class Game extends Thread {
	int port;
	ServerSocket serversocket;
	ArrayList<Player> players;
	state turn = state.FIRST;
	String gameName = null;
	state[][] fields;

	public static enum state {
		BLANK, FIRST, SECOND
	}

	protected Game() {
	}

	public Game(int i, String n) {
		this(i);
		gameName = n;
	}

	public Game(int i) {
		port = i;
		fields = new state[10][10];
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				fields[x][y] = state.BLANK;
			}
		}
	}

	public String getGameName() {
		return gameName;
	}

	public void run() {
		System.err.println("W¹tek Game na porcie " + port);
		players = new ArrayList<>();
		try {
			serversocket = new ServerSocket(port);
		} catch (Exception e) {
			System.err.println("Nie uda³o siê zaj¹c portu " + port);
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < 2; i++) {
				Player player = new Player(serversocket.accept());
				System.err.println("Gracz " + i + " po³¹czony.");
				// TODO
				if (i == 0 && false) {
					player.writer.write(Message.getChooseSideMessage());
					player.writer.flush();
					String action = ((JSONObject) JSONSerializer
							.toJSON(player.reader.readLine()))
							.getString("action");
					if (action == "ME") {
						turn = state.FIRST;
					} else {
						turn = state.SECOND;
					}
				} else if (i == 1) {
					players.get(0).writer.write(Message.getConnectedMessage());
					players.get(0).writer.flush();
				}
				players.add(player);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Boolean end_of_game = false;
			do {
				sendTurns();
				end_of_game = !game();
				turn = getOponent();
			} while (!end_of_game && players.get(0).isConnected()
					&& players.get(1).isConnected());
		} catch (Exception e) {
			for (int i = 0; i <= 1; i++) {
				if (!players.get(i).isConnected())
					continue;
				try {
					System.err
							.println("Problem z kontynuowaniem gry, roz³¹czam graczy");
					players.get(i).writer.write(Message
							.getDisconnectedMessage());
					players.get(i).writer.flush();
					players.get(i).socket.close();
				} catch (Exception ex) {
					System.err.println("Nie uda³o siê roz³¹czyc graczy");
					return;
				}
			}
			e.printStackTrace();
		}

		try {
			serversocket.close();
		} catch (IOException e) {
			System.err.println("Nie uda³o siê zamkn¹c socketu.");
		}
	}

	private Boolean game() throws IOException {

		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(players
				.get(turn == state.FIRST ? 0 : 1).reader.readLine());
		switch (jsonObject.getString("action").toUpperCase()) {

		case "PLACE":
			int x = jsonObject.getInt("x");
			int y = jsonObject.getInt("y");
			if (x < 0 || x >= 10 || y < 0 || y >= 10) {
				players.get(getPlayerIndex(getOponent())).writer.write(Message
						.getErrorMessage(404));
				players.get(getPlayerIndex(getOponent())).writer.flush();
				game();
			}
			if (fields[x][y] != state.BLANK) {
				players.get(getPlayerIndex(getOponent())).writer.write(Message
						.getErrorMessage(409));
				players.get(getPlayerIndex(getOponent())).writer.flush();
				game();
			}
			fields[x][y] = turn;
			players.get(getPlayerIndex(getOponent())).writer.write(Message
					.getOponentPlaceMessage(x, y));
			return true;

		case "DISCONNECT":
			players.get(getPlayerIndex(turn)).socket.close();
			players.get(getPlayerIndex(getOponent())).writer.write(Message
					.getDisconnectedMessage());
			players.get(getPlayerIndex(getOponent())).writer.flush();
			players.get(getPlayerIndex(getOponent())).socket.close();
			return false;

		case "CHAT":
		default:
			players.get(getPlayerIndex(turn)).writer.write(Message
					.getErrorMessage(401));
			players.get(getPlayerIndex(turn)).writer.flush();
			game();
		}
		return true;

	}

	private void sendTurns() {
		try {
			players.get(getPlayerIndex(turn)).writer.write(Message
					.getPlayerTurnMessage());
			players.get(getPlayerIndex(turn)).writer.flush();
			players.get(getPlayerIndex(getOponent())).writer.write(Message
					.getOponentTurnMessage());
			players.get(getPlayerIndex(getOponent())).writer.flush();
		} catch (IOException e) {
			System.err.println("Nie mo¿na wys³ac tur.");
		}
	}

	private state getOponent() {
		if (turn == state.FIRST)
			return state.SECOND;
		return state.FIRST;
	}

	private int getPlayerIndex(state t) {
		if (t == state.FIRST)
			return 0;
		return 1;
	}
}
