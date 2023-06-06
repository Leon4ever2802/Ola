package othersInNetworkWithGUI;

import java.io.IOException;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		
		Socket socket = null;
		
		try {
			socket = new Socket("10.118.10.14", 65432);
			
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
