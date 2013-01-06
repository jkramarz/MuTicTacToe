package messages;

public class NewGameRequestMessage extends Message {
	String name;
	NewGameRequestMessage(){
		this("");
	}
	NewGameRequestMessage(String n){
		name = n;
	}
	public String getName(){
		return name;
	}
}
