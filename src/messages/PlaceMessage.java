package messages;

public class PlaceMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5427032342931405167L;
	private int x;
	private int y;
	PlaceMessage(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY(){
		return y;
	}
}
