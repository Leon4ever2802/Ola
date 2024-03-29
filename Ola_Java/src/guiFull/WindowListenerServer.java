package guiFull;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Leon Reusch
 */
public class WindowListenerServer extends WindowAdapter{

	private ServerFrame frame;
	
	/**
	 * @param frame
	 */
	public WindowListenerServer(ServerFrame frame) {
		super();
		this.frame = frame;
	}
	
	/**
	 *
	 */
	public void windowClosing(WindowEvent e) {
        for(ClientHandler client : ClientHandler.getClients()) {
        	client.writeToClient("%CLOSED%");
        }
        this.frame.onClose();
    }
}
