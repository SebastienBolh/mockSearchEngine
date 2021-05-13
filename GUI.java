/* This is my first ever foray in GUIs.
 * I have put comments explaining almost 
 * everything to help me (and others if curious)
 * to learn and fully understand the code!
 * */


package finalproject;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class GUI implements ActionListener{
	
	//declare variables to be in method scope
	private int count = 0;
	private JFrame frame;
	private JPanel panel;
	private JButton searchButton;
	private JLabel label;
	
	public GUI() {
		//initialize frame + panel
		frame = new JFrame();
		panel = new JPanel();
		//set panel to the frame
		panel.setBorder(BorderFactory.createEmptyBorder(300,600,300,600));
		panel.setLayout(new GridLayout(0, 1));
		
		//BUTTON:
		searchButton = new JButton("Search");
		searchButton.addActionListener(this);
		//change size/position
		searchButton.setLocation(10,10);
		//add to panel
		panel.add(searchButton);
		
		//LABEL:
		label = new JLabel("Number of clicks: 0");
		//add to panel
		panel.add(label);

		//add panel to frame
		frame.add(panel, BorderLayout.CENTER);
		//set what happens when panel closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//set title of window
		frame.setTitle("Welcome to Sebastien's Internet!");
		//set window to match certain size
		frame.pack();
		//center JFrame
		frame.setLocationRelativeTo(null);
		//set window to be visible and in focus
		frame.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new GUI();
		try {
			SearchEngine searchEngine = new SearchEngine("test.xml");
			searchEngine.crawlAndIndex("www.cs.mcgill.ca");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		count++;
		label.setText("Number of clicks: " + count);
	}
}
