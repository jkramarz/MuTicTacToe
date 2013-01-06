package server;

import java.util.Queue;

import messages.Message;

abstract class ConnectionThread extends Thread {

	private ConnectionThread opponent = null;
	protected Marker marker = null;
	
	abstract public void disconnect();

	abstract Queue<Message> toClient();

	abstract Queue<Message> toServer();

	
	void setOpponent(ConnectionThread opponent) {
		if (this.opponent == null) {
			this.opponent = opponent;
		}
	}

	ConnectionThread opponent() {
		return opponent;
	}
	
	Marker marker(){
		return marker;
	}
}
