package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;

import messages.Message;

class PlayerConnectionThread extends ConnectionThread {

	Queue<Message> fromServer;
	Queue<Message> toServer;
	Socket playerConnection;
	ObjectInputStream inputStream;
	ObjectOutputStream outputStream;

	PlayerConnectionThread(Socket playerConnection, Queue<Message> fromServer, Queue<Message> toServer) throws IOException {
		this.playerConnection = playerConnection;
		this.fromServer = fromServer;
		this.toServer = toServer;
		inputStream = new ObjectInputStream(playerConnection.getInputStream());
		outputStream = new ObjectOutputStream(playerConnection.getOutputStream());
	}

	public void run() {
		try {
			while (playerConnection.isConnected()) {
				if (!fromServer.isEmpty()) {
					System.err.println("=>" + fromServer.peek().toString());
					outputStream.writeObject(fromServer.poll());
				}
				if (inputStream.available() > 0) {
					Object o = inputStream.readObject();
					System.err.println("<=" + o.toString());
					if (o instanceof Message) {
						toServer.add((Message) o);
					}
				}
				Thread.sleep(50);
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
		} catch (IOException e) {}
	}

}
