package messages;

import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -161015076099664818L;

	protected Message(){}
	
	public static ErrorMessage getErrorMessage(Integer i) {
		return new ErrorMessage(i);
	}

	public static NewGameMessage getNewGameMessage(Integer i) {
		return new NewGameMessage(i);
	}

	public static PingMessage getPingMessage() {
		return new PingMessage();
	}

	public static PongMessage getPongMessage() {
		return new PongMessage();
	}

	public static WinMessage getWinMessage() {
		return new WinMessage();
	}

	public static LoseMessage getLoseMessage() {
		return new LoseMessage();
	}

	public static YourTurnMessage getYourTurnMessage() {
		return new YourTurnMessage();
	}

	public static ChooseSidesMessage getChooseSidesMessage() {
		return new ChooseSidesMessage();
	}

	public static DisconnectMessage getDisconnectMessage() {
		return new DisconnectMessage();
	}

	public static Message getNewGameRequestMessage(String string) {
		return new NewGameRequestMessage(string);
	}

	public static PlaceMessage getPlaceMessage(int x, int y) {
		return new PlaceMessage(x, y);
	}

	public static SidesMessage getSidesMessage(int i) {
		return new SidesMessage(i);
	}
	
	public static GameListMessage getGameListMessage(HashMap<Integer,String> list){
		return new GameListMessage(list);
	}
	
	public static GameListRequestMessage getGameListRequestMessage(){
		return new GameListRequestMessage();
	}
}
