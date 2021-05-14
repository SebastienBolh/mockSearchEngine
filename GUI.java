/* This is my first serious foray in GUIs.
 * I have put comments explaining almost 
 * everything to help me (and others if curious)
 * to learn and fully understand the code!
 * */

package finalproject;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GUI implements ActionListener{
	
	//declare variables to be in method scope
	private int count = 0;
	private JFrame frame;
	private JPanel panel;
	private JButton searchButton;
	private JLabel label;
	private JTextField searchField;
	private static SearchEngine searchEngine;
	
	public GUI() {
		//initialize frame + panel
		frame = new JFrame();
		panel = new JPanel();
		//set panel to the frame
		panel.setBorder(BorderFactory.createEmptyBorder(300,600,300,600));
		panel.setLayout(new GridLayout(0, 1));
		
		//SEARCH BAR:
		searchField = new JTextField("Enter your search here");
		panel.add(searchField);
		
		//BUTTON:
		searchButton = new JButton("Search");
		searchButton.addActionListener(this);
		//change size/position
		searchButton.setLocation(10,10);
		//add to panel
		panel.add(searchButton);

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
			searchEngine = new SearchEngine("test.xml");
			searchEngine.crawlAndIndex("www.cs.mcgill.ca");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		String userSearchText = searchField.getText();
		ArrayList<String> results = searchEngine.getResults(userSearchText);
		//check results working
		System.out.println(results);
		//get rid of everything (in order to get rid of old results)
		panel.removeAll();
		//put basic elements back
		panel.add(searchField);
		panel.add(searchButton);
		
		//make ArrayList of buttons so i can iterate through them and operate upon them
		ArrayList<JButton> buttons = new ArrayList<>();
		
		//create button for each element
		for(String url: results) {
			buttons.add(new JButton(url));
			panel.add(new JButton(url));
		}
		
		if(buttons.isEmpty()) {
			JLabel noResults = new JLabel("There were no search results, please search again.");
			noResults.setPreferredSize(new Dimension(150, 30));
			panel.add(noResults);
		}
		
		//check ArrayList of buttons working
		System.out.println(buttons.toString());
		
		//update the GUI
		panel.revalidate();
		panel.repaint();
	}
}
