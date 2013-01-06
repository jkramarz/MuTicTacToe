package messages;

public class NewGameRequestMessage extends Message {
	String name;
	private String type;
	NewGameRequestMessage(String type){
		this(type, "");
	}
	NewGameRequestMessage(String type, String n){
		name = n;
		this.type = type;
	}
	public String getName(){
		return name;
	}
	public String getType(){
		return type;
	}
}
