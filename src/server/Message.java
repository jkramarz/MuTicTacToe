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
		return JSONSerializer.toJSON(error).toString();
	}
	static String getErrorText(int i) {
		switch(i){
			case 503:
				return "brak mo¿liwoœci stworzenia gry ze wzgledu na brak dostepnych wolnych portow";
			case 501:
				return "dana metoda jest niezaimplementowana";
			case 409:
				return "przes³any komunikat nieadekwatny do obecnego stanu";
			case 404:
				return "przes³any komunikat niezgodny z protoko³em";
			case 403:
				return "próba po³o¿enia pionka na ju¿ zajetym miejscu";
			default:
				return null;
		}
	}
	
	public static String getNewGameMessage(Integer i) {
		Map<String, String> message = new HashMap<>();
		message.put("status", "OK");
		message.put("port", i.toString());
		return JSONSerializer.toJSON(message).toString();
	}
	
	public static String getChooseSideMessage(){
		Map<String, String> message = new HashMap<>();
		message.put("status", "WHO STARTS");
		return JSONSerializer.toJSON(message).toString();
	}
}
