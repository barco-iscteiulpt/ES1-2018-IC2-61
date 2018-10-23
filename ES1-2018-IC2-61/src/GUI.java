import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JComboBox;

public class GUI extends Thread{

	private JFrame frame;
	private JTextField keywords;
	protected String accessToken = "EAAEprSZC8PBABAJyQpTxdEQaXh9dkvPOopFsDUmjJIB3m7n3IT6rxiWOXSwPpYzCJtUm0CH3D6pjzCPoH9ZAYgplLKeRwkQ2ZCcEsZCc4lU9olFarz3Pcz5JJb6zZCzUH94DSidZAbw85xyV3JpsQSx5RZCaWICuel33cJH4TIufR4BPDti62z5NVhaSAM2s54ZD";
	private DefaultListModel listModel;
	private ArrayList<Post> postList;

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
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu ("File");
		JMenu menu2 = new JMenu ("Edit");
		JMenu menu3 = new JMenu ("About");
	
		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu3);
		
		frame.setJMenuBar(menuBar);
		
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
		JButton searchButton = new JButton();
		
		keywords.setPreferredSize(new Dimension(120,24));
		searchButton.setMargin(new Insets(2,8,2,8));
		searchButton.setIcon(new ImageIcon("src/resources/magnifier-tool.png"));
		
		top_right.add(keywords);
		top_right.add(searchButton);

		// Create the center panel. Specify layout. Add to frame.
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(1, 2, 0, 0));
		frame.getContentPane().add(center, BorderLayout.CENTER);

		// Create timeline and article boxes. Add to center panel.
		listModel = new DefaultListModel();
		postList = new ArrayList<>();
		
		JScrollPane scrollPaneTimeline = new JScrollPane();
		JScrollPane scrollPaneArticle = new JScrollPane();
		JList<?> timeline = new JList(listModel);
		scrollPaneTimeline.setViewportView(timeline);
		
		JTextArea article = new JTextArea();
		scrollPaneArticle.setViewportView(article);
		
		timeline.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Post show = postList.get(timeline.getSelectedIndex());
				article.setText(show.getMessage());
			}
		});
		
		center.add(scrollPaneTimeline);
		center.add(article);
		
		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				getFacebookData(keywords.getText());
		}
	});

	}
	
	public void getFacebookData(String info) {
		
		FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		
		Page page = fbClient.fetchObject("listajespeniche", Page.class);
		
		Connection<Post> postFeed = fbClient.fetchConnection(page.getId() + "/feed", Post.class);
		
		for(List<Post> postPage : postFeed) {
			for(Post aPost : postPage) {
				if(aPost.getMessage()!=null && aPost.getMessage().contains(info)) {
					postList.add(aPost);
					System.out.println("-->"+aPost.getMessage());
					
				}
			}
		}
		for(Post post : postList) {
			String header = "Facebook: "+ post.getCreatedTime().toString() + "   " + post.getMessage().substring(0, 30)+"...";
			listModel.addElement(header);	
		}
	}

}
