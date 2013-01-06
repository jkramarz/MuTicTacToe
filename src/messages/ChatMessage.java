package messages;

public class ChatMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8724834161666826598L;
	String chat;
	public ChatMessage(String string) {
		chat = string;
	}
	public String getChat(){
		return chat;
	}
}
