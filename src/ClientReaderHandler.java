package othersInNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReaderHandler implements Runnable{

	private String msgFromServer;
	private BufferedReader bufferedReader;
	
	public ClientReaderHandler(Socket socket) {
		try {
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while((this.msgFromServer = this.bufferedReader.readLine()) != null) {
				if(this.msgFromServer.contains("%D%")) System.err.println(this.msgFromServer.split("%")[2]);
				else if(this.msgFromServer.contains("%CONN%")) System.err.println(this.msgFromServer.split("%")[2]);
				else if(this.msgFromServer.contains("%LIST%")) System.err.println(this.msgFromServer.split("%")[2]);
				else System.out.println(this.msgFromServer);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				if(this.bufferedReader != null) {
					this.bufferedReader.close();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
