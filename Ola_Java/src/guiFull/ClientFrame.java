package guiFull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

//https://stackoverflow.com/questions/22623515/writing-at-the-bottom-of-jtextarea
/**
 * @author Leon Reusch
 */
public class ClientFrame extends JFrame implements ActionListener {

	private JLayeredPane layeredPane;
	private JScrollPane anzeige;
	private JTextField userInput;
	private JButton send;
	private JTextPane texte;
	private JPanel filler;
	private ClientWriterHandler cwh;
	
	public ClientFrame(ClientWriterHandler cwh) {
		this.cwh = cwh;
		
		this.texte = new JTextPane();
		this.texte.setFont(new Font("Arial", Font.PLAIN, 12));
		this.texte.setEditable(false);
		this.texte.setBackground(null);
		this.texte.setOpaque(true);
		//this.texte.setMargin(new Insets(415,0,0,0));
		
		this.filler = new JPanel();
		this.filler.setBackground(null);
		this.filler.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
        	gbc.gridy = 0;
        	gbc.weightx = 1;
        	gbc.weighty = 1;
        	gbc.fill = GridBagConstraints.BOTH;
        	JPanel filler2 = new JPanel();
        	filler2.setBackground(null);
        	this.filler.add(filler2, gbc);
        	gbc.gridy = 1;
        	gbc.weighty = 0;
        	this.filler.add(this.texte, gbc);
        
		this.anzeige = new JScrollPane(filler);
		this.anzeige.setOpaque(true);
		this.anzeige.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.anzeige.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.anzeige.setBounds(20, 10, 320, 450);
		
		this.userInput = new JTextField();
		this.userInput.setOpaque(true);
		this.userInput.setBackground(null);
		this.userInput.setBounds(20, 475, 252, 30);
		this.userInput.setFont(new Font("Arial", Font.PLAIN, 12));
		
		this.send = new JButton();
		this.send.setOpaque(true);
		this.send.setFont(new Font("Arial", Font.BOLD, 12));
		this.send.setText("Send");
		this.send.setBackground(Color.LIGHT_GRAY);
		this.send.setBounds(277, 475, 63, 29);
		this.send.addActionListener(this);
		this.send.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, 0), "Enter");
		this.send.getActionMap().put("Enter", new EnterListener(this));
		
		this.layeredPane = new JLayeredPane();
		this.layeredPane.setOpaque(true);
		this.layeredPane.setBounds(0, 0, 375, 560);
		this.layeredPane.add(anzeige);
		this.layeredPane.add(send);
		this.layeredPane.add(userInput);
		
		this.setTitle("Ola - ");
		this.add(layeredPane);
		this.addWindowListener(new WindowListenerClient(this));
		this.setSize(new Dimension(375, 560));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String msg = this.userInput.getText();
		this.userInput.setText("");
		
		if(msg.replaceAll("\\s+", "").equals("")) return;
		
		this.texte.setText(this.texte.getText() + "\n" + msg);
		this.cwh.sendMessage(msg);
	}
	
	public void enterPressed() {
		String msg = this.userInput.getText();
		this.userInput.setText("");
		
		if(msg.equals("%0%")) this.onExit();
		else {
			if(msg.replaceAll("\\s+", "").equals("")) return;
			
			this.texte.setText(this.texte.getText() + "\n" + msg);
			this.cwh.sendMessage(msg);
		}
		
	}
	
	public void messageRecieved(String msg) {
		this.texte.setText(this.texte.getText() + "\n" + msg);
	}
	
	public void onExit() {
		this.cwh.sendMessage("%0%");
		this.dispose();
	}
	
	public void onServerClose() {
		this.texte.setText(this.texte.getText() + "\nServer closed, connection lost!\n...");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dispose();
	}
	
	public void addUsername(String name) {
		this.setTitle("Ola - " + name);
	}

}
