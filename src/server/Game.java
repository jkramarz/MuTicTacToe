package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import messages.ChatMessage;
import messages.DisconnectMessage;
import messages.Message;
import messages.PlaceMessage;
import messages.SidesMessage;

public class Game extends Thread {
	int port;
	ServerSocket socket;
	state turn = state.FIRST;
	String gameName = null;
	String gameType = null;
	state[][] fields;

	Queue<Message> toServer[];
	Queue<Message> toPlayer[];
	ConnectionThread[] player = new ConnectionThread[2];
	private gamestate gameState;

	static enum gamestate {
		SIDES, FIRST, FIRSTWON, SECOND, SECONDWON, END, NEW
	}

	public static enum state {
		BLANK, FIRST, SECOND
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

	public String getGameType() {
		return gameType;
	}

	void estabilishConnections() throws IOException {
		for (int i = 0; i < 2; i++) {
			toPlayer[i] = new ConcurrentLinkedQueue<Message>();
			toServer[i] = new ConcurrentLinkedQueue<Message>();
			if (i == 1 && gameType == "PVC") {
				player[i] = new AIConnectionThread(
						toPlayer[i], toServer[i]);
			} else {
				player[i] = new PlayerConnectionThread(socket.accept(),
						toPlayer[i], toServer[i]);
			}
			player[i].start();
		}
	}

	boolean playersConnected() {
		return player[0].isAlive() && player[1].isAlive();
	}

	boolean chatAndDisconnect() {
		for (int i = 0; i < 2; i++) {
			if (!toServer[i].isEmpty()) {
				if (toServer[i].peek() instanceof ChatMessage) {
					toPlayer[opposite(i)].add(toServer[i].poll());
				} else if (toServer[i].peek() instanceof DisconnectMessage) {
					toPlayer[opposite(i)].add(toServer[i].poll());
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
			turn(0);
			break;
		case FIRSTWON:
			winlose(0);
			break;
		case SECOND:
			turn(1);
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
		toPlayer[0].add(Message.getChooseSidesMessage());
		gameState = gamestate.SIDES;
	}

	private void sides() {
		if (!toServer[0].isEmpty()
				&& toServer[0].peek() instanceof SidesMessage) {
			SidesMessage m = (SidesMessage) toServer[0].poll();
			if (m.getSide() == 0) {
				gameState = gamestate.FIRST;
				toPlayer[0].add(Message.getYourTurnMessage());
			} else {
				gameState = gamestate.SECOND;
				toPlayer[1].add(Message.getYourTurnMessage());
			}
		}
	}

	private void winlose(int i) {
		toPlayer[i].add(Message.getWinMessage());
		toPlayer[opposite(i)].add(Message.getLoseMessage());
	}

	private void turn(int i) {
		if (!toServer[i].isEmpty()
				&& toServer[i].peek() instanceof PlaceMessage) {
			PlaceMessage m = (PlaceMessage) toServer[i].poll();
			if (fields[m.getX()][m.getY()] == state.BLANK) {
				fields[m.getX()][m.getY()] = (i == 0 ? state.FIRST
						: state.SECOND);
				toPlayer[opposite(i)].add(m);
				if (checkWon(m.getX(), m.getY())) {
					gameState = (i == 0 ? gamestate.FIRSTWON
							: gamestate.SECONDWON);
				} else {
					gameState = (i == 0 ? gamestate.SECOND : gamestate.FIRST);
					toPlayer[opposite(i)].add(Message.getYourTurnMessage());
				}
			} else {
				toPlayer[i].add(Message.getErrorMessage(403));
			}
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
		for (int i = 0; i < 2; i++) {
			player[i].disconnect();
		}
	}

	public void run() {
		System.err.println("W¹tek Game na porcie " + port);
		try {
			estabilishConnections();
			while (playersConnected()) {
				if (chatAndDisconnect()) {
					break;
				}
				if (stateMachine()) {
					break;
				}
			}
		} catch (Exception e) {
		} finally {
			disconnect();
		}
	}

	private int opposite(int i) {
		if (i == 0)
			return 1;
		return 0;
	}
}
