package messages;

public class SidesMessage extends Message {
	private int side;
	SidesMessage(int s){
		side = s;
	}
	public int getSide() {
		return side;
	}
}
