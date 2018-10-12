import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JComboBox;

public class GUI {

	private JFrame frame;
	private JTextField keywords;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Create the frame. Specify the title, placement, size, closing operation and layout of the frame.
		frame = new JFrame();
		frame.setTitle("Bom Dia Academia!");
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		// Create the top panel. Specify height and layout of the panel.  Add it to the frame.
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(1, 2, 0, 0));
		frame.getContentPane().add(top, BorderLayout.NORTH);
		
		// Create the left side of the top panel. Specify layout and orientation. Add to top panel.
		JPanel top_left = new JPanel();
		top_left.setLayout(new FlowLayout(FlowLayout.LEADING));
		top.add(top_left);
		
		// Get social icons from "resources" folder. Create 3 checkboxes (selected by default). Add them to top-left panel.
		JLabel fb_icon = new JLabel(new ImageIcon("src/resources/facebook.png"));
		JLabel twitter_icon = new JLabel(new ImageIcon("src/resources/twitter.png"));
		JLabel email_icon = new JLabel(new ImageIcon("src/resources/email.png"));
		
		JCheckBox fb_checkbox = new JCheckBox();
		fb_checkbox.setSelected(true);
		JCheckBox twitter_checkbox = new JCheckBox();
		twitter_checkbox.setSelected(true);
		JCheckBox email_checkbox = new JCheckBox();
		email_checkbox.setSelected(true);
		
		top_left.add(fb_checkbox);
		top_left.add(fb_icon);
		top_left.add(twitter_checkbox);
		top_left.add(twitter_icon);
		top_left.add(email_checkbox);
		top_left.add(email_icon);
		
		// Create the right side of the top panel. Specify layout and orientation. Add to top panel.
		JPanel top_right = new JPanel();
		top_right.setLayout(new FlowLayout(FlowLayout.TRAILING));
		top.add(top_right);
		
		// Create period filter and add different options. Add to top-right panel.
		String[] options = {"última hora","últimas 24h","último dia","última semana", "último mês"};
		JComboBox comboBox = new JComboBox(options);
		comboBox.setRenderer(new MyComboBoxRenderer("Defina um período..."));
		comboBox.setSelectedIndex(-1);
		top_right.add(comboBox);
		
		// Define keyword search and create "Search" button. Specify dimensions, margins and icon. Add to top-right panel.
		keywords = new JTextField();
		JButton button = new JButton();
		
		keywords.setPreferredSize(new Dimension(120,24));
		button.setMargin(new Insets(2,8,2,8));
		button.setIcon(new ImageIcon("src/resources/magnifier-tool.png"));
		
		top_right.add(keywords);
		top_right.add(button);

		// Create the center panel. Specify layout. Add to frame.
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(1, 2, 0, 0));
		frame.getContentPane().add(center, BorderLayout.CENTER);

		// Create timeline and article boxes. Add to center panel.
		JList<?> timeline = new JList<Object>();
		JTextArea article = new JTextArea();
		
		center.add(timeline);
		center.add(article);
	}

}
