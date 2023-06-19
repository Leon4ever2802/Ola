package guiFull;

import java.io.IOException;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		
		Socket socket = null;
		
		try {
			socket = new Socket("127.0.0.1", 65432);
			
			ClientWriterHandler cwh = new ClientWriterHandler(socket);
			ClientFrame frame = new ClientFrame(cwh);
			Thread crh = new Thread(new ClientReaderHandler(socket, frame));
			
			crh.setDaemon(true);
			crh.start();
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
