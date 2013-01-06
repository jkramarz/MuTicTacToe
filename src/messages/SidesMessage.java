package messages;

public class SidesMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8633551622353943465L;
	private int side;
	SidesMessage(int s){
		side = s;
	}
	public int getSide() {
		return side;
	}
}
