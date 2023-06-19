package guiFull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		ServerFrame frame = new ServerFrame();
		Socket socket = null;
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(65432);
			serverSocket.setReuseAddress(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		acceptClient(serverSocket, frame);
		
	}
	
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
	
	public static void endServer() {
		System.exit(0);
	}

}
