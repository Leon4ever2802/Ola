package guiFull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReaderHandler implements Runnable{

	private String msgFromServer;
	private BufferedReader bufferedReader;
	private ClientFrame frame;
	private Socket socket;
	
	public ClientReaderHandler(Socket socket, ClientFrame frame) {
		this.socket = socket;
		this.frame = frame;
		try {
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while((this.msgFromServer = this.bufferedReader.readLine()) != null) {
				if(this.msgFromServer.contains("%D%") || 
						this.msgFromServer.contains("%CHECK%") || 
						this.msgFromServer.contains("%CONN%")) {
					this.frame.messageRecieved(msgFromServer.split("%")[2]);
				}
				else if(this.msgFromServer.equals("%CLOSED%")) {
					frame.onServerClose();
					break;
				}
				else this.frame.messageRecieved(this.msgFromServer);
			}
		}
		catch(Exception e) {
			
		}finally {
			
			try {
				if(this.bufferedReader != null) {
					this.bufferedReader.close();
				}
				if(this.socket != null) {
					this.socket.close();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
