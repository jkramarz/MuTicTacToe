package server;

import java.util.Queue;

import messages.Message;

abstract class PlayerThread extends Thread {

	private PlayerThread opponent = null;
	protected Marker marker = null;
	
	abstract public void disconnect();

	abstract Queue<Message> toClient();

	abstract Queue<Message> toServer();

	
	void setOpponent(PlayerThread opponent) {
		if (this.opponent == null) {
			this.opponent = opponent;
		}
	}

	PlayerThread opponent() {
		return opponent;
	}
	
	Marker marker(){
		return marker;
	}
}
