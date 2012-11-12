package server;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONSerializer;

class Message {
	static String getErrorMessage(Integer i) {
		Map<String, String> error = new HashMap<>();
		error.put("status", "ERROR");
		error.put("error_code", i.toString());
		error.put("error_message", getErrorText(i));
		return JSONSerializer.toJSON(error).toString() + "\n";
	}
	static String getErrorText(int i) {
		switch(i){
			case 503:
				return "brak mo�liwo�ci stworzenia gry ze wzgledu na brak dostepnych wolnych portow";
			case 501:
				return "dana metoda jest niezaimplementowana";
			case 409:
				return "przes�any komunikat nieadekwatny do obecnego stanu";
			case 404:
				return "przes�any komunikat niezgodny z protoko�em";
			case 403:
				return "pr�ba po�o�enia pionka na ju� zajetym miejscu";
			default:
				return null;
		}
	}
	
	public static String getNewGameMessage(Integer i) {
		Map<String, String> message = new HashMap<>();
		message.put("status", "OK");
		message.put("port", i.toString());
		return JSONSerializer.toJSON(message).toString() + "\n";
	}
	
	public static String getChooseSideMessage(){
		Map<String, String> message = new HashMap<>();
		message.put("status", "WHO STARTS");
		return JSONSerializer.toJSON(message).toString() + "\n";
	}
	
	public static String getPlayerTurnMessage() {
		Map<String, String> message = new HashMap<>();
		message.put("status", "TURN");
		message.put("attibute", "YOUR");
		return JSONSerializer.toJSON(message).toString() + "\n";
	}
	public static String getOponentTurnMessage() {
		Map<String, String> message = new HashMap<>();
		message.put("status", "TURN");
		message.put("attibute", "OPONENT");
		return JSONSerializer.toJSON(message).toString() + "\n";
	}
	public static String getDisconnectedMessage() {
		Map<String, String> message = new HashMap<>();
		message.put("status", "DISCONNECTED");
		return JSONSerializer.toJSON(message).toString() + "\n";
	}
	public static String getOponentPlaceMessage(Integer x, Integer y){
		Map<String, String> message = new HashMap<>();
		message.put("status", "PLACE");
		message.put("x", x.toString());
		message.put("y", y.toString());
		return JSONSerializer.toJSON(message).toString() + "\n";
	}
}
