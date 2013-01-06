package messages;

class ErrorMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3521258628682004272L;
	int code;

	public ErrorMessage(int i) {
		code = i;
	}

	int getErrorCode(){
		return code;
	}
	
	public String getErrorText() {
		switch (code) {
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
			return "";
		}
	}
}
