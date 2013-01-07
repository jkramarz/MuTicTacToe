package client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.Message;
import messages.NewGameMessage;

public class ManagementConnection {

	private String gamehost;
	private Integer gameport;
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private BufferedInputStream bis;

	ManagementConnection(String host, Integer port) throws IOException {
		gamehost = host;
		gameport = port;
		socket = new Socket(gamehost, gameport);
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		bis = new BufferedInputStream(socket.getInputStream());
		inputStream = new ObjectInputStream(bis);
		
	}
	
	public String getGamehost() {
		return gamehost;
	}

	public Integer getGameport() {
		return gameport;
	}

	public Message sendCommand(Message command) throws IOException {
		outputStream.writeObject(command);
		Object o;
		try {
			o = inputStream.readObject();
			System.err.println(o.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		if(o instanceof Message){
			return (Message) o;
		}else{
			return null;
		}
	}

	public int createNewPvpGame() throws Exception {
		Message result = sendCommand(Message.getNewGameRequestMessage("PVP"));
		if(result instanceof NewGameMessage){
			result = (NewGameMessage) result;
			return ((NewGameMessage) result).getPort();
		}
		throw new Exception("Wrong reply");
	}

	public Integer createNewPvcGame() throws Exception {
		Message result = sendCommand(Message.getNewGameRequestMessage("PVC"));
		if(result instanceof NewGameMessage){
			result = (NewGameMessage) result;
			return ((NewGameMessage) result).getPort();
		}
		throw new Exception("Wrong reply");
	}

}
