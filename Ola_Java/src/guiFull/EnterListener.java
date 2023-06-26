package guiFull;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * @author Leon Reusch
 */
public class EnterListener extends AbstractAction{
	
	private ClientFrame frame;

	public EnterListener(ClientFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.frame.enterPressed();
	}

}
