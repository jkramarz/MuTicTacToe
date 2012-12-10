package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class ManagementConnection {

	private String gamehost;
	private Integer gameport;
	private CountDownLatch interupted;
	static Socket socket;
	volatile BufferedReader bufferedReader;
	volatile BufferedWriter bufferedWritter;
	String command;
	String result;

	ManagementConnection(String host, Integer port) {
		gamehost = host;
		gameport = port;
	}

	void close() throws Exception {
		socket.close();
	}

	void createConnection() throws UnknownHostException, IOException {
		socket = new Socket(gamehost, gameport);
		interupted = new CountDownLatch(1);
		bufferedReader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		bufferedWritter = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
	}

	public String getGamehost() {
		return gamehost;
	}

	public Integer getGameport() {
		return gameport;
	}

	public void interupt() {
		interupted.countDown();
	}

	public String sendCommand(String command) {
		try {
			bufferedWritter.write(command + "\n");
			bufferedWritter.flush();
			return bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Integer createNewPvpGame() throws Exception {
		Integer port = null;
		String result = sendCommand("{\"action\": \"NEW GAME\", \"type\": \"PVP\"}");
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(result);
		switch (jsonObject.getString("status").toUpperCase()) {
		case "OK":
			if (jsonObject.containsKey("port")) {
				port = new Integer((String) jsonObject.getString("port"));
				break;
			}
		default:
			throw new Exception();
		}
		return port;
	}

	public Integer createNewPvcGame() throws Exception {
		Integer port = null;
		String result = sendCommand("{\"action\": \"NEW GAME\", \"type\": \"PVC\"}");
		JOptionPane.showMessageDialog(null, (String)result);
		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(result);
		switch (jsonObject.getString("status").toUpperCase()) {
		case "OK":
			if (jsonObject.containsKey("port")) {
				port = new Integer((String) jsonObject.getString("port"));
				break;
			}
		default:
			throw new Exception();
		}
		return port;
	}

}
