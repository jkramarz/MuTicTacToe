package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import messages.Message;

class PlayerConnectionThread extends ConnectionThread {

	Queue<Message> toClient;
	Queue<Message> toServer;
	Socket playerConnection;
	ObjectInputStream inputStream;
	ObjectOutputStream outputStream;

	PlayerConnectionThread(Socket playerConnection) throws IOException {
		this.playerConnection = playerConnection;
		this.toClient = new ConcurrentLinkedQueue<Message>();
		this.toServer = new ConcurrentLinkedQueue<Message>();
		inputStream = new ObjectInputStream(playerConnection.getInputStream());
		outputStream = new ObjectOutputStream(
				playerConnection.getOutputStream());
	}
	
	Queue<Message> getToClient(){
		return toClient;
	}
	
	Queue<Message> getToServer(){
		return toServer;
	}

	public void run() {
		try {
			while (playerConnection.isConnected()) {
				if (!toClient.isEmpty()) {
					System.err.println("=>" + toClient.peek().toString());
					outputStream.writeObject(toClient.poll());
				} else if (inputStream.available() > 0) {
					Object o = inputStream.readObject();
					System.err.println("<=" + o.toString());
					if (o instanceof Message) {
						toServer.add((Message) o);
					}
				} else {
					Thread.sleep(50);
				}
			}
			toServer.add(Message.getDisconnectMessage());
		} catch (Exception e) {
			return;
		}
	}

	@Override
	public void disconnect() {
		try {
			playerConnection.close();
		} catch (IOException e) {
		}
	}

}
