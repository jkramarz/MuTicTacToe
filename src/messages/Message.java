package messages;

public class Message {

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
}
