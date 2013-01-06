package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Queue;

import messages.ChatMessage;
import messages.DisconnectMessage;
import messages.Message;
import messages.PlaceMessage;
import messages.SidesMessage;

public class Game extends Thread {
	int port;
	ServerSocket socket;
	String gameName = null;
	String gameType = null;
	Marker[][] fields;

	@SuppressWarnings("rawtypes")
	Queue toServer[] = new Queue[2];
	@SuppressWarnings("rawtypes")
	Queue toPlayer[] = new Queue[2];
	PlayerThread[] players = new PlayerThread[2];
	private GameState gameState;

	static enum GameState {
		SIDES, FIRST, FIRSTWON, SECOND, SECONDWON, END, NEW
	}

	protected Game() {
	}

	public Game(int i, String type, String name) throws IOException {
		this(type, i);
		gameName = name;
		gameType = type;
	}

	public Game(String type, int i) throws IOException {
		port = i;
		socket = new ServerSocket(port);
		fields = new Marker[10][10];
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				fields[x][y] = Marker.BLANK;
			}
		}
	}

	public String getGameName() {
		return gameName;
	}

	public String getGameType() {
		return gameType;
	}

	void estabilishConnections() throws IOException {
		for (int i = 0; i < 2; i++) {
			if (i == 1 && gameType == "PVC") {
				players[i] = new AiPlayerThread(i == 0 ? Marker.FIRST
						: Marker.SECOND);
			} else {
				players[i] = new HumanPlayerThread(i == 0 ? Marker.FIRST
						: Marker.SECOND, socket.accept());
			}
			toPlayer[i] = players[i].toClient();
			toServer[i] = players[i].toServer();
			players[i].start();
		}
		
		players[0].setOpponent(players[1]);
		players[0].opponent().setOpponent(players[0]);
	}

	boolean playersConnected() {
		for(PlayerThread player: players){
			if(!player.isAlive()){
				return false;
			}
		}
		return true;
	}

	boolean chatAndDisconnect() {
		for(PlayerThread player: players){
			if (player.toServer().isEmpty()) {
				if (player.toServer().peek() instanceof ChatMessage) {
					player.opponent().toClient()
							.add(player.toServer().poll());
				} else if (player.toServer().peek() instanceof DisconnectMessage) {
					player.opponent().toClient()
							.add(player.toServer().poll());
					return true;
				}
			}
		}
		return false;
	}

	boolean stateMachine() {
		switch (gameState) {
		case NEW:
			newgame();
			break;
		case SIDES:
			sides();
			break;
		case FIRST:
			turn(players[0]);
			break;
		case FIRSTWON:
			winlose(0);
			break;
		case SECOND:
			turn(players[0].opponent());
			break;
		case SECONDWON:
			winlose(1);
			break;
		case END:
			return true;
		default:
			break;
		}
		return false;
	}

	private void newgame() {
		players[0].toClient().add(Message.getChooseSidesMessage());
		gameState = GameState.SIDES;
	}

	private void sides() {
		if (!players[0].toServer().isEmpty()
				&& players[0].toServer().peek() instanceof SidesMessage) {
			SidesMessage m = (SidesMessage) players[0].toServer().poll();
			if (m.getSide() == 0 || m.getSide() == 1) {
				gameState = m.getSide() == 0 ? GameState.FIRST
						: GameState.SECOND;
				players[m.getSide()].toClient()
						.add(Message.getYourTurnMessage());
			}
		} else if (!players[0].toServer().isEmpty()) {
			players[0].toServer().remove();
			players[0].toClient().add(Message.getErrorMessage(409));
		}
	}

	private void winlose(int i) {
		players[0].toClient().add(Message.getWinMessage());
		players[0].opponent().toClient().add(Message.getLoseMessage());
	}

	private void turn(PlayerThread player) {
		if (player.toServer().isEmpty()
				&& player.toServer().peek() instanceof PlaceMessage) {
			PlaceMessage m = (PlaceMessage) player.toServer().poll();
			if (fields[m.getX()][m.getY()] == Marker.BLANK) {
				fields[m.getX()][m.getY()] = player.marker();
				player.opponent().toClient().add(m);
				if (checkWon(m.getX(), m.getY())) {
					gameState = (gameState == GameState.FIRST ? GameState.FIRSTWON
							: GameState.SECONDWON);
				} else {
					gameState = (gameState == GameState.FIRST ? GameState.SECOND
							: GameState.FIRST);
					player.opponent().toClient()
							.add(Message.getYourTurnMessage());
				}
			} else if (!toServer[0].isEmpty()) {
				player.toClient().add(Message.getErrorMessage(403));
			}
		} else {
			player.toServer().remove();
			player.toClient().add(Message.getErrorMessage(409));
		}
	}

	boolean checkWon(int x, int y) {
		int count;

		// in column
		count = 1;
		for (int i = x + 1; i < 10 && fields[i][y] == fields[x][y]; i++, count++)
			;
		for (int i = x - 1; i >= 0 && fields[i][y] == fields[x][y]; i--, count++)
			;
		if (count >= 5)
			return true;

		// in row
		count = 1;
		for (int i = y + 1; i < 10 && fields[x][i] == fields[x][y]; i++, count++)
			;
		for (int i = y - 1; i >= 0 && fields[x][i] == fields[x][y]; i--, count++)
			;
		if (count >= 5)
			return true;

		// in NE-SW
		count = 1;
		for (int i = 1; (x + i) < 10 && (y + i) < 10
				&& fields[x + i][y + i] == fields[x][y]; i++, count++)
			;
		for (int i = 1; (x - i) >= 0 && (y - i) >= 0
				&& fields[x - i][y - i] == fields[x][y]; i++, count++)
			;
		if (count >= 5)
			return true;

		// in NW-SE
		count = 1;
		for (int i = 1; (x + i) < 10 && (y - i) >= 0
				&& fields[x + i][y + i] == fields[x][y]; i++, count++)
			;
		for (int i = 1; (x - i) >= 0 && (y + i) < 10
				&& fields[x - i][y - i] == fields[x][y]; i++, count++)
			;
		if (count >= 5)
			return true;

		return false;
	}

	void disconnect() {
		for (PlayerThread player: players){
			player.disconnect();
		}
	}

	public void run() {
		System.err.println("W¹tek Game na porcie " + port);
		try {
			estabilishConnections();
			gameState = GameState.NEW;
			while (playersConnected()) {
				if (chatAndDisconnect()) {
					break;
				}
				if (stateMachine()) {
					break;
				}
				Thread.sleep(50);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

}
