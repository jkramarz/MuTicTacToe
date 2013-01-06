package server;

import java.util.Queue;

import messages.Message;

abstract class ConnectionThread extends Thread {

	abstract public void disconnect();

	abstract Queue<Message> getToClient();

	abstract Queue<Message> getToServer();

}
