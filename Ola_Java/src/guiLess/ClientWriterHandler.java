package guiLess;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientWriterHandler implements Runnable{

	private BufferedWriter bufferedWriter;
	private Scanner input;
	
	
	public ClientWriterHandler(Socket socket) {
		this.input = new Scanner(System.in);
		try {
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.print("Enter username:");
			this.bufferedWriter.write("%NAME%" + this.input.nextLine());
			this.bufferedWriter.newLine();
			this.bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				
				String msgToSend = this.input.nextLine();
				
				this.bufferedWriter.write(msgToSend);
				this.bufferedWriter.newLine();
				this.bufferedWriter.flush();
				
				if(msgToSend.equalsIgnoreCase("%0%")) {
					break;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
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
}
