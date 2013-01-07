package server;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import messages.DisconnectMessage;
import messages.LoseMessage;
import messages.Message;
import messages.PlaceMessage;
import messages.WinMessage;
import messages.YourTurnMessage;
import server.Game.GameState;

class AiPlayerThread extends PlayerThread {
	Queue<Message> toClient;
	Queue<Message> toServer;

	Marker[][] board;
	int[][] gradient = {
			{0,0,0,0,0,0,0,0,0,0},
			{0,1,1,1,1,1,1,1,1,0},
			{0,1,1,1,1,1,1,1,1,0},
			{0,1,1,2,2,2,2,1,1,0},
			{0,1,1,2,3,3,2,1,1,0},
			{0,1,1,2,3,3,2,1,1,0},
			{0,1,1,2,2,2,2,1,1,0},
			{0,1,1,1,1,1,1,1,1,0},
			{0,1,1,1,1,1,1,1,1,0},
			{0,0,0,0,0,0,0,0,0,0}
	};
	
	GameState state = GameState.NEW;
	PlaceMessage lastmove;

	AiPlayerThread(Marker marker, Marker[][] board) throws IOException {
		this.toClient = new ConcurrentLinkedQueue<Message>();
		this.toServer = new ConcurrentLinkedQueue<Message>();
		this.marker = marker;
		this.board = board;
	}

	Queue<Message> toClient() {
		return toClient;
	}

	Queue<Message> toServer() {
		return toServer;
	}

	public void run() {
		while (true) {
			if (toClient.isEmpty()) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				Message m = toClient.poll();
				if (m instanceof DisconnectMessage || m instanceof WinMessage
						|| m instanceof LoseMessage) {
					break;
				} else if (m instanceof PlaceMessage) {
					lastmove = (PlaceMessage) m;
					decreaseGradient(lastmove.getX(), lastmove.getY());
				} else if (m instanceof YourTurnMessage) {
					int x = lastmove.getX();
					int y = lastmove.getY();
					int b1, b2;
					int count;

					// check in column
					count = 1;
					for (b1 = x + 1; b1 < 10 && board[b1][y] == board[x][y]; b1++, count++)
						;
					for (b2 = x - 1; b2 >= 0 && board[b2][y] == board[x][y]; b2--, count++)
						;
					if (count == 4) {
						if (b1 < 10 && board[b1][y] != Marker.SECOND) {
							place(b1, y);
							continue;
						} else if (b2 >= 0 && board[b2][y] != Marker.SECOND) {
							place(b2, y);
							continue;
						} else {
							panic();
							continue;
						}
					}

					// check in row
					count = 1;
					for (b1 = y + 1; b1 < 10 && board[x][b1] == board[x][y]; b1++, count++)
						;
					for (b2 = y - 1; b2 >= 0 && board[x][b2] == board[x][y]; b2--, count++)
						;
					if (count == 4) {
						if (b1 < 10 && board[x][b1] != Marker.SECOND) {
							place(x, b1);
							continue;
						} else if (b2 >= 0 && board[x][b2] != Marker.SECOND) {
							place(x, b2);
							continue;
						} else {
							panic();
							continue;
						}
					}

					// in NE-SW
					count = 1;
					for (b1 = 1; (x + b1) < 10 && (y + b1) < 10
							&& board[x + b1][y + b1] == board[x][y]; b1++, count++)
						;
					for (b2 = 1; (x - b2) >= 0 && (y - b2) >= 0
							&& board[x - b2][y - b2] == board[x][y]; b2++, count++)
						;
					if (count == 4) {
						if ((x + b1) < 10 && (y + b1) < 10
								&& board[x + b1][y + b1] != Marker.SECOND) {
							place(x + b1, y + b1);
							continue;
						} else if ((x - b2) >= 0 && (y - b2) >= 0
								&& board[x - b2][y - b2] != Marker.SECOND) {
							place(x - b2, y - b2);
							continue;
						} else {
							panic();
							continue;
						}
					}

					// in NW-SE
					count = 1;
					for (b1 = 1; (x + b1) < 10 && (y - b1) >= 0
							&& board[x + b1][y - b1] == board[x][y]; b1++, count++)
						;
					for (b2 = 1; (x - b2) >= 0 && (y + b2) < 10
							&& board[x - b2][y + b2] == board[x][y]; b2++, count++)
						;
					if (count == 4) {
						if ((x + b1) < 10 && (y - b1) >= 0
								&& board[x + b1][y - b1] != Marker.SECOND) {
							place(x, b1);
							continue;
						} else if ((x - b2) >= 0 && (y + b2) < 10
								&& board[x - b2][y + b2] != Marker.SECOND) {
							place(x, b2);
							continue;
						} else {
							panic();
							continue;
						}
					}

					panic();
				}
			}
		}
	}

	private void panic() {
		int maxx = 0;
		int maxy = 0;
		int maxg = -1000;
		for(int x = 0; x < 10; x++){
			for(int y = 0; y < 10; y++){
				if(gradient[x][y] > maxg && board[x][y] == Marker.BLANK){
					maxg = gradient[x][y];
					maxx = x;
					maxy = y;
				}
			}
		}
		place(maxx, maxy);
		return;
	}

	private void place(int x, int y) {
		toServer.add(Message.getPlaceMessage(x, y));
		increaseGradient(x, y);
		return;
	}

	private void increaseGradient(int x, int y) {
		for(int i = -1; i <= 1; i++){
			for(int j = -1; j <= 1; j++){
				if(x+i >= 0 && x+i < 10 && y+j >= 0 && y+j < 10){
						gradient[x+i][y+j]=gradient[x][y]+1;
				}
			}
		};
	}
	private void decreaseGradient(int x, int y) {
		for(int i = -1; i <= 1; i++){
			for(int j = -1; j <= 1; j++){
				if(x+i >= 0 && x+i < 10 && y+j >= 0 && y+j < 10){
						gradient[x+i][y+j]=gradient[x][y]-1;
				}
			}
		}
	}
	@Override
	public void disconnect() {
		return;
	}

}
