package guiLess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		Socket socket = null;
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(65432);
			serverSocket.setReuseAddress(true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("SERVER STARTED SUCCESSFULLY AT " + serverSocket.getLocalSocketAddress() + "\n");
		
		while(true) {
			
			 try {
				 
				socket = serverSocket.accept();
				
				System.err.println("New Client connected: " + serverSocket.getInetAddress() + serverSocket.getLocalSocketAddress());

				new Thread(new ClientHandler(socket)).start();

			}catch (Exception e) {
			e.printStackTrace();
			}
			 
		}
		
	}

}
