package guiFull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

/**
 * @author Leon Reusch
 */
public class Client {

	/**
	 * Client main to start the whole process of connection to a Server and chatting.
	 * @param args
	 */
	public static void main(String[] args) {
		
		Socket socket = null;
		
		while(socket == null) {
			socket = connect("127.0.0.1", 65432);
		}
		
		ClientWriterHandler cwh = new ClientWriterHandler(socket);
		ClientFrame frame = new ClientFrame(cwh);
		Thread crh = new Thread(new ClientReaderHandler(socket, frame));
		
		crh.setDaemon(true);
		crh.start();
	}
	
	/**
	 * This method tries to connect to a Server at the given Host address and port.
	 * @param host Host IP-Address of the Server to connect to
	 * @param port Port number of the Server
	 * @return Socket-Object
	 * @exception ConnectException returns Null
	 * @exception IOException printStackTrace + return Null
	 */
	private static Socket connect(String host, int port) {
		try {
			return new Socket(host, port);
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
