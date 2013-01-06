package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

class Connection implements Runnable {

	Socket socket;

	Connection(Socket s) {
		socket = s;
	}

	@Override
	public void run() {
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			BufferedWriter bufferedWritter = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));
			while (!socket.isClosed()) {
				String command = bufferedReader.readLine();
				System.err.println(command);
				String result = routeCommand(command);
				System.err.println(result);
				bufferedWritter.write(result + "\n");
				bufferedWritter.flush();
			}
			socket.close();
		} catch (Exception e) {
			try {
				socket.close();
			} catch (IOException e1) {}
			System.err.println(e.getMessage());
		}
	}

	private static String routeCommand(String command) {
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(command);
		switch (jsonObject.getString("action").toUpperCase()) {
		case "NEW GAME":
			if (jsonObject.containsKey("name")) {
				return Server.newGame(jsonObject.getString("name"));
			} else {
				return Server.newGame(null);
			}
		case "LIST GAMES":
			return Server.listGames();
		case "PING":
			return Server.pong();
		default:
			return Message.getErrorMessage(404);
		}
	}
	

}
