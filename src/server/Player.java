package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.naming.directory.InvalidAttributesException;

class Player {
	BufferedReader reader;
	BufferedWriter writer;
	Socket socket;

	Player(Socket s) throws IOException, InvalidAttributesException {
		if (s == null)
			throw new InvalidAttributesException("Player socket must exists!");
		if (s.isConnected() == false)
			throw new InvalidAttributesException(
					"Player socket must be connected!");
		socket = s;
		reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
	}

	public BufferedReader getReader() {
		return reader;
	}

	public BufferedWriter getWriter() {
		return writer;
	}

	boolean isConnected() {
		return socket.isConnected();
	}

}
