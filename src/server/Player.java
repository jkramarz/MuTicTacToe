package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class Player {
	BufferedReader reader;
	BufferedWriter writer;
	Socket socket;
	
	Player(Socket s) throws IOException{
	   socket = s;
       reader = new BufferedReader(
    		   		new InputStreamReader(
    		   			socket.getInputStream()
    		   		)
    		   );
       writer = new BufferedWriter(
    		    	new OutputStreamWriter(
						socket.getOutputStream()
					)
				);
	}
	Boolean isConnected(){
		return socket.isConnected();
	}
	
}
