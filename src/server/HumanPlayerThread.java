package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import messages.Message;

class HumanPlayerThread extends PlayerThread {

	Queue<Message> toClient;
	Queue<Message> toServer;
	Socket socket;
	ObjectInputStream inputStream;
	ObjectOutputStream outputStream;
	private BufferedInputStream bis;

	HumanPlayerThread(Marker marker, Socket playerConnection) {
		this.socket = playerConnection;
		this.toClient = new ConcurrentLinkedQueue<Message>();
		this.toServer = new ConcurrentLinkedQueue<Message>();
		this.marker = marker;
		try {
			bis = new BufferedInputStream(playerConnection.getInputStream());
			inputStream = new ObjectInputStream(bis);
			outputStream = new ObjectOutputStream(
					playerConnection.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Queue<Message> toClient() {
		return toClient;
	}

	Queue<Message> toServer() {
		return toServer;
	}

	public void run() {
		try {
			while (!socket.isClosed()) {
				if (!toClient.isEmpty()) {
					System.err.println("=>" + toClient.peek().toString());
					outputStream.writeObject(toClient.poll());
					outputStream.flush();
				} else if (bis.available()>0) {
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
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
