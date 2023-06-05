package othersInNetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

	private static int cnt = 0;
	private int id;
	private String name;
	private final Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private static ArrayList<ClientHandler> clients = new ArrayList<>();
	
	public ClientHandler(Socket socket) {
		this.id = cnt++;
		this.socket = socket;
		try {
			this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			ClientHandler.clients.add(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String checkUsers() {
		String names = "";
		if(this.id != 0) {
			for(ClientHandler ch : clients) {
				if(ch == this) continue;
				else if(ch.getId() == clients.size()-2) names += ch.getName();
				else names += ch.getName() + ", ";
			}
		}
		else names = "Noone";
		names += " already connected!";
		return names;
	}
	
	@Override
	public void run() {
		try {

			String msgFromClient;
			
			while((msgFromClient = this.bufferedReader.readLine()) != null) {
				
				System.out.println("Client" + this.id + ": " + msgFromClient);
				
				synchronized (this.getClass()){
					if(msgFromClient.contains("%NAME%")) {
						this.name = msgFromClient.split("%")[2];
						
						for (ClientHandler ch : ClientHandler.clients) {
							if(ch == this) {
								ch.writeToClient("%CHECK%" + checkUsers());
							}
							else {
								ch.writeToClient("%CONN%" + this.name + " connected!");
							}
						}
					}
					
					else if (msgFromClient.equalsIgnoreCase("%0%")) {
						
						for (ClientHandler ch : ClientHandler.clients) {
							if(ch == this) {
								continue;
							}
							else if(ch.getId() > this.id) {
								ch.writeToClient("%D%" + this.name + " disconnected!");
								ch.setId(ch.getId()-1);
							}
							else {
								ch.writeToClient("%D%" + this.name + " disconnected!");
							}
						}
						ClientHandler.clients.remove(this);
						ClientHandler.cnt--;
						System.err.println("Client" + this.id + " disconnected!");
						break;
					}
					else {
						for (ClientHandler ch : ClientHandler.clients) {
							if(ch == this) {
								continue;
							}
							else {
								ch.writeToClient(this.name + ": " + msgFromClient);
							}
						}
					}
				}
				
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void writeToClient(String msg) {
		
		try {
			this.bufferedWriter.write(msg);
			this.bufferedWriter.newLine();
			this.bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Socket getSocket() {
		return socket;
	}

	public String getName() {
		return name;
	}

}
