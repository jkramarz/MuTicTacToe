package messages;

public class NewGameRequestMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5550178127013249052L;
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
