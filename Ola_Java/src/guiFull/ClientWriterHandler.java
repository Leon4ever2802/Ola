package guiFull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Leon Reusch
 */
public class ClientWriterHandler{

	private BufferedWriter bufferedWriter;
	private Scanner input;
	
	public ClientWriterHandler(Socket socket) {
		this.input = new Scanner(System.in);
		try {
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.print("Enter username:");
			String name = this.input.nextLine();
			while(name.equals("") || name.length() > 15) {
				System.out.print("Username invalid or too long!\nEnter username:");
				name = this.input.nextLine();
			}
			this.bufferedWriter.write("%NAME%" + name);
			this.bufferedWriter.newLine();
			this.bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg) {
		try {
			
			this.bufferedWriter.write(msg);
			this.bufferedWriter.newLine();
			this.bufferedWriter.flush();
			
			if(msg.equalsIgnoreCase("%0%")) {
				try {	
					if(this.bufferedWriter != null) {
						this.bufferedWriter.close();
					}
					if(this.input != null) {
						this.input.close();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
