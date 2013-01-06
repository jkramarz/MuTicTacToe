package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.ListGamesRequestMessage;
import messages.Message;
import messages.NewGameRequestMessage;
import messages.PingMessage;

class Connection implements Runnable {

	Socket socket;
	ObjectInputStream inputStream;
	ObjectOutputStream outputStream;
	Connection(Socket s) throws IOException {
		socket = s;
		inputStream = new ObjectInputStream(socket.getInputStream());
		outputStream = new ObjectOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		try {
			while (!socket.isClosed()) {
				if(inputStream.available() > 0){
					Object o = inputStream.readObject();
					if(o instanceof Message){
						outputStream.writeObject(routeCommand((Message) o));
					}
				}
			}
			socket.close();
		} catch (Exception e) {
			try {
				socket.close();
			} catch (IOException e1) {}
			System.err.println(e.getMessage());
		}
	}

	private Message routeCommand(Message m) {
		if(m instanceof NewGameRequestMessage){
			return Server.newGame((NewGameRequestMessage) m);
		}else if(m instanceof ListGamesRequestMessage){
			return Server.listGames();
		}else if(m instanceof PingMessage){
			return Message.getPongMessage();
		}else{
			return Message.getErrorMessage(404);
		}
	}
	

}