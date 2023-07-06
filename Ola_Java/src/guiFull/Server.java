package guiFull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Leon Reusch
 */
public class Server {

	public static void main(String[] args) {
		
		Socket socket = null;
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(65432);
			serverSocket.setReuseAddress(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ServerFrame frame = new ServerFrame(serverSocket.getLocalSocketAddress().toString());
		
		acceptClient(serverSocket, frame);
		
	}
	
	/**
	 * @param serverSocket
	 * @param frame
	 */
	private static void acceptClient(ServerSocket serverSocket, ServerFrame frame) {
		
		Socket socket = null;
		
		while(true) {
			
			 try {
				 
				socket = serverSocket.accept();
				
				frame.messageRecieved("New Client connected at: " + serverSocket.getLocalSocketAddress());

				new Thread(new ClientHandler(socket, frame)).start();

			}catch (Exception e) {
			e.printStackTrace();
			}
			 
		}
	}
	
	/**
	 * 
	 */
	public static void endServer() {
		System.exit(0);
	}

}
