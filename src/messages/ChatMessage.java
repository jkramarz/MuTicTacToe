package messages;

public class ChatMessage extends Message {
	String chat;
	public ChatMessage(String string) {
		chat = string;
	}
	public String getChat(){
		return chat;
	}
}
