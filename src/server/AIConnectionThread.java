package server;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import messages.Message;

class AIConnectionThread extends ConnectionThread {
	Queue<Message> toClient;
	Queue<Message> toServer;

	AIConnectionThread(Marker marker) throws IOException {
		this.toClient = new ConcurrentLinkedQueue<Message>();
		this.toServer = new ConcurrentLinkedQueue<Message>();
		this.marker = marker;
	}

	Queue<Message> toClient() {
		return toClient;
	}

	Queue<Message> toServer() {
		return toServer;
	}

	public void run() {
		throw new NotImplementedException();
	}

	@Override
	public void disconnect() {
		return;
	}

}
