package messages;

public class NewGameMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7717084289176255917L;
	private int port;
	NewGameMessage(int port){
		this.port = port;
	}
	public int getPort(){
		return port;
	}
}
