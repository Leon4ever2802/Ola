package guiLess;

import java.io.IOException;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		
		Socket socket = null;
		
		try {
			socket = new Socket("192.168.0.162", 65432);
			
			Thread cwh = new Thread(new ClientWriterHandler(socket));
			Thread crh = new Thread(new ClientReaderHandler(socket));
			
			crh.setPriority(8);
			cwh.setPriority(4);
			crh.setDaemon(true);
			
			cwh.start();
			crh.start();
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
