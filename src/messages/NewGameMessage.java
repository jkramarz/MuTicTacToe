package messages;

public class NewGameMessage extends Message {
	private int port;
	NewGameMessage(int port){
		this.port = port;
	}
	public int getPort(){
		return port;
	}
}
