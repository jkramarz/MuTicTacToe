/**
 * 
 */
package server;

import java.io.*;
import java.net.*;

/**
 * @author lenwe
 *
 */
public class Server {
	private static int port = 10001; 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.err.println("Server is starting up...");
		try{
			ServerSocket serversocket = new ServerSocket(port);
			
			
			Socket socket = serversocket.accept();
			BufferedReader bufferedReader = 
		            new BufferedReader(
		                new InputStreamReader(
		                    socket.getInputStream()));
			System.out.println(bufferedReader.readLine());
			
			socket.close();
			serversocket.close();
			
			
		}catch(Exception e){
			System.err.println("Whoops! ;-(");
		}
	}

}
