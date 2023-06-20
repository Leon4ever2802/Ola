package guiFull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		
		Socket socket = null;
		
		while(socket == null) {
			socket = connect();
		}
		
		ClientWriterHandler cwh = new ClientWriterHandler(socket);
		ClientFrame frame = new ClientFrame(cwh);
		Thread crh = new Thread(new ClientReaderHandler(socket, frame));
		
		crh.setDaemon(true);
		crh.start();
	}
	
	private static Socket connect() {
		try {
			return new Socket("127.0.0.1", 65432);
		} 
		catch(ConnectException e) {
			return null;
		}
		catch (IOException e2) {
			e2.printStackTrace();
			return null;
		}
	}

}
