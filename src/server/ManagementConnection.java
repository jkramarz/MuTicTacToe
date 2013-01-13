package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.GameListRequestMessage;
import messages.Message;
import messages.NewGameRequestMessage;
import messages.PingMessage;

class ManagementConnection implements Runnable {

	Socket socket;
	ObjectInputStream inputStream;
	ObjectOutputStream outputStream;

	ManagementConnection(Socket s) throws IOException {
		socket = s;
		inputStream = new ObjectInputStream(socket.getInputStream());
		outputStream = new ObjectOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		try {
			while (socket.isConnected()) {
				Object o = inputStream.readObject();
				if (o instanceof Message) {
					outputStream.writeObject(routeCommand((Message) o));
				}else{
					throw new Exception();
				}

			}
			socket.close();
		} catch (Exception e) {
			try {
				socket.close();
			} catch (IOException e1) {
			}
			System.err.println(e.getMessage());
		}
	}

	private Message routeCommand(Message m) {
		System.err.println(m.toString());
		if (m instanceof NewGameRequestMessage) {
			return Server.newGame((NewGameRequestMessage) m);
		} else if (m instanceof GameListRequestMessage) {
			return Server.listGames();
		} else if (m instanceof PingMessage) {
			return Message.getPongMessage();
		} else {
			return Message.getErrorMessage(404);
		}
	}

}
