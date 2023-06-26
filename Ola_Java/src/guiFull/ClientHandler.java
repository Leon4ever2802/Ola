package guiFull;

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
	private ServerFrame frame;
	private static ArrayList<ClientHandler> clients = new ArrayList<>();
	
	public ClientHandler(Socket socket, ServerFrame frame) {
		this.id = cnt++;
		this.socket = socket;
		this.frame = frame;
		try {
			this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String checkUsers() {
		String names = "";
		if(clients.size() > 1) {
			for(ClientHandler ch : clients) {
				if(ch == this) continue;
				else if(clients.indexOf(ch) == clients.size()-2) names += ch.getName();
				else names += ch.getName() + ", ";
			}
		}
		else names = "Noone else";
		names += " already connected!";
		return names;
	}
	
	@Override
	public void run() {
		try {

			String msgFromClient = this.bufferedReader.readLine();
			frame.messageRecieved("Client" + this.id + ": " + msgFromClient);
			this.name = msgFromClient.split("%")[2];
			ClientHandler.clients.add(this);
			
			synchronized (this.getClass()){
				for (ClientHandler ch : ClientHandler.clients) {
					if(ch == this) {
						ch.writeToClient("%CONN%Server: Connected");
						ch.writeToClient("%NAME%" + this.name);
						ch.writeToClient("%CHECK%Server: " + checkUsers());
					}
					else {
						ch.writeToClient("%CONN%Server: " + this.name + " connected!");
					}
				}
			}
			
			while((msgFromClient = this.bufferedReader.readLine()) != null) {
				
				frame.messageRecieved("Client" + this.id + ": " + msgFromClient);
				
				synchronized (this.getClass()){
					
					if (msgFromClient.equalsIgnoreCase("%0%")) {
						
						for (ClientHandler ch : ClientHandler.clients) {
							if(ch == this) {
								continue;
							}
							else {
								ch.writeToClient("%D%Server: " + this.name + " disconnected!");
							}
						}
						ClientHandler.clients.remove(this);
						frame.messageRecieved("Client" + this.id + " disconnected!");
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
	
	public void writeToClient(String msg) {
		
		try {
			this.bufferedWriter.write(msg);
			this.bufferedWriter.newLine();
			this.bufferedWriter.flush();
		} catch (IOException e) {
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
	
	public static ArrayList<ClientHandler> getClients() {
		return clients;
	}

}
