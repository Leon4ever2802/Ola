package guiFull;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public class ServerFrame extends JFrame{

	private JLayeredPane layeredPane;
	private JScrollPane anzeige;
	private JTextPane texte;
	private JPanel filler;
	
	public ServerFrame() {
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
		this.anzeige.setBounds(20, 10, 320, 500);
		
		this.layeredPane = new JLayeredPane();
		this.layeredPane.setOpaque(true);
		this.layeredPane.setBounds(0, 0, 375, 560);
		this.layeredPane.add(anzeige);
		
		this.setTitle("Ola-Server");
		this.addWindowListener(new WindowListenerServer(this));
		this.add(layeredPane);
		this.setSize(new Dimension(375, 560));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setVisible(true);
		
		this.messageRecieved("SERVER STARTED SUCCESSFULLY!\n");
	}
	
	public void messageRecieved(String msg) {
		this.texte.setText(this.texte.getText() + "\n" + msg);
	}
	
	public void onClose() {
		Server.endServer();
		this.dispose();
	}
}
