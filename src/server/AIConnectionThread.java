package server;

import java.io.IOException;
import java.util.Queue;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import messages.Message;

class AIConnectionThread extends ConnectionThread {
	Queue<Message> fromServer;
	Queue<Message> toServer;

	AIConnectionThread(Queue<Message> fromServer, Queue<Message> toServer)
			throws IOException {
		this.fromServer = fromServer;
		this.toServer = toServer;
	}

	public void run(){
		throw new NotImplementedException();
	}
	
	@Override
	public void disconnect() {
		return;
	}

}
