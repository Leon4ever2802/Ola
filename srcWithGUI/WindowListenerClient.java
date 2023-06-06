package othersInNetworkWithGUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowListenerClient extends WindowAdapter{

	private ClientFrame frame;
	
	public WindowListenerClient(ClientFrame frame) {
		super();
		this.frame = frame;
	}
	
	public void windowClosing(WindowEvent e) {
        this.frame.onExit();
    }
}
