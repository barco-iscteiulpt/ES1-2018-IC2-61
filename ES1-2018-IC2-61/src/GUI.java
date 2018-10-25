import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JComboBox;

public class GUI extends Thread {

	private JFrame frame;
	private JTextField keywords;
	protected String accessToken = "EAAEprSZC8PBABAJyQpTxdEQaXh9dkvPOopFsDUmjJIB3m7n3IT6rxiWOXSwPpYzCJtUm0CH3D6pjzCPoH9ZAYgplLKeRwkQ2ZCcEsZCc4lU9olFarz3Pcz5JJb6zZCzUH94DSidZAbw85xyV3JpsQSx5RZCaWICuel33cJH4TIufR4BPDti62z5NVhaSAM2s54ZD";
	private DefaultListModel listModel;
	private ArrayList<Post> postList;
	private ArrayList<Status> finalTweetsList;

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

		// Create the frame. Specify the title, placement, size, closing operation and
		// layout of the frame.
		setFrame(new JFrame());
		getFrame().setTitle("Bom Dia Academia!");
		getFrame().setBounds(100, 100, 800, 600);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(new BorderLayout(0, 0));

		// Create the menu bar. Create and add 3 menus.
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("File");
		JMenu menu2 = new JMenu("Edit");
		JMenu menu3 = new JMenu("About");

		// Create menu items and add them to their respective menu.
		JMenuItem item1 = new JMenuItem("Close");
		JMenuItem item2 = new JMenuItem("New");
		menu1.add(item2);
		menu1.add(item1);
		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu3);

		// Add ActionListeners to all menu items.
		item1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getFrame().dispose();
			}
		});

		item2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		getFrame().setJMenuBar(menuBar);

		// Create the top panel. Specify height and layout of the panel. Add it to the
		// frame.
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(1, 2, 0, 0));
		getFrame().getContentPane().add(top, BorderLayout.NORTH);

		// Create the left side of the top panel. Specify layout and orientation. Add to
		// top panel.
		JPanel top_left = new JPanel();
		top_left.setLayout(new FlowLayout(FlowLayout.LEADING));
		top.add(top_left);

		// Get social icons from "resources" folder. Create 3 checkboxes (selected by
		// default). Add them to top-left panel.
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

		// Create the right side of the top panel. Specify layout and orientation. Add
		// to top panel.
		JPanel top_right = new JPanel();
		top_right.setLayout(new FlowLayout(FlowLayout.TRAILING));
		top.add(top_right);

		// Create period filter and add different options. Add to top-right panel.
		String[] options = { "Choose time...", "Last hour", "Last day", "Last week", "Last month" };
		JComboBox comboBox = new JComboBox(options);
		comboBox.setSelectedIndex(0);
		top_right.add(comboBox);

		// Define keyword search and create "Search" button. Specify dimensions, margins
		// and icon. Add ActionListener. Add to top-right panel.
		keywords = new JTextField();
		JButton searchButton = new JButton();

		keywords.setPreferredSize(new Dimension(120, 24));
		searchButton.setMargin(new Insets(2, 8, 2, 8));
		searchButton.setIcon(new ImageIcon("src/resources/magnifier-tool.png"));

		keywords.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchButton.doClick();
			}
		});

		top_right.add(keywords);
		top_right.add(searchButton);

		// Create the center panel. Specify layout. Add to frame.
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(1, 2, 0, 0));
		getFrame().getContentPane().add(center, BorderLayout.CENTER);

		// Create scrolling panes. Create timeline and article boxes. Add them to the
		// respective panes.
		listModel = new DefaultListModel();
		postList = new ArrayList<>();
		finalTweetsList = new ArrayList<>();

		JScrollPane scrollPaneTimeline = new JScrollPane();
		JScrollPane scrollPaneArticle = new JScrollPane();
		JList<?> timeline = new JList(listModel);
		scrollPaneTimeline.setViewportView(timeline);

		JTextArea article = new JTextArea();
		scrollPaneArticle.setViewportView(article);
		article.setLineWrap(true);
		article.setWrapStyleWord(true);

		// Add ListSelectionListener to timeline and ActionListener to the search
		// button.
		timeline.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Post show = postList.get(timeline.getSelectedIndex());
				article.setText(show.getMessage());
			}
		});

		center.add(scrollPaneTimeline);
		center.add(article);

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (twitter_checkbox.isSelected())
					searchTwitter(keywords.getText(), comboBox.getSelectedItem().toString());
				if (fb_checkbox.isSelected())
					getFacebookData(keywords.getText(), comboBox.getSelectedItem().toString());
			}
		});

	}

	/**
	 * Accesses a specific Facebook page, gets its feed and saves every post on a
	 * list. Organizes the list from the most recent posts to the oldest. Chooses
	 * which posts are displayed depending on the JComboBox chosen item.
	 * 
	 * @param info   The keywords used on the JTextField
	 * @param period The time period chosen on the JComboBox
	 */
	public void getFacebookData(String info, String period) {

		FacebookClient fbClient = new DefaultFacebookClient(accessToken);

		Page page = fbClient.fetchObject("listajespeniche", Page.class);

		Connection<Post> postFeed = fbClient.fetchConnection(page.getId() + "/feed", Post.class);

		for (List<Post> postPage : postFeed) {
			for (Post aPost : postPage) {
				if (aPost.getMessage() != null && aPost.getMessage().contains(info)) {
					postList.add(aPost);
				}
			}
		}

		Collections.sort(postList, new Comparator<Post>() {
			public int compare(Post o1, Post o2) {
				return o2.getCreatedTime().compareTo(o1.getCreatedTime());
			}
		});

		for (Post p : postList) {

			Calendar calendar = Calendar.getInstance();

			if (period.equals("Choose time...")) {
				String headerPost = "Facebook: " + p.getCreatedTime().toString() + "   "
						+ p.getMessage().substring(0, 30) + "...";
				listModel.addElement(headerPost);
			}

			if (period.equals("Last hour")) {
				calendar.add(Calendar.HOUR_OF_DAY, -1);
				if (p.getCreatedTime().after(calendar.getTime())) {
					String headerPost = "Facebook: " + p.getCreatedTime().toString() + "   "
							+ p.getMessage().substring(0, 30) + "...";
					listModel.addElement(headerPost);
				}
			}

			if (period.equals("Last day")) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				if (p.getCreatedTime().after(calendar.getTime())) {
					String headerPost = "Facebook: " + p.getCreatedTime().toString() + "   "
							+ p.getMessage().substring(0, 30) + "...";
					listModel.addElement(headerPost);
				}
			}

			if (period.equals("Last week")) {
				calendar.add(Calendar.DAY_OF_MONTH, -7);
				if (p.getCreatedTime().after(calendar.getTime())) {
					String headerPost = "Facebook: " + p.getCreatedTime().toString() + "   "
							+ p.getMessage().substring(0, 30) + "...";
					listModel.addElement(headerPost);
				}
			}

			if (period.equals("Last month")) {
				calendar.add(Calendar.MONTH, -1);
				if (p.getCreatedTime().after(calendar.getTime())) {
					String headerPost = "Facebook: " + p.getCreatedTime().toString() + "   "
							+ p.getMessage().substring(0, 30) + "...";
					listModel.addElement(headerPost);
				}
			}

		}
	}
	
	/**
	 * Accesses a specific Twitter page, gets its tweets and saves each one on a
	 * list. Organizes the list from the most recent tweets to the oldest. Chooses
	 * which tweets are displayed depending on the JComboBox chosen item.
	 *
	 * @param info   The keywords used on the JTextField
	 * @param period The time period chosen on the JComboBox
	 */
	public void searchTwitter(String info, String period) {

		info = keywords.getText();

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("0I3XKOkznUjpuwdDOSkLvcSpg")
				.setOAuthConsumerSecret("zy8i9meaxzK5Rn05rKIsJwWvclJPfdpmtfnE5UuJvHxsW6oZ0G")
				.setOAuthAccessToken("325579017-itc5klbFYmBcGvHUaZaUz0sCD29J7GVfuMiw5ZCg")
				.setOAuthAccessTokenSecret("Wz55x8BoTY8wdU5zwQCBI45520ic5JjLi9VCHXBArg5JT");

		TwitterFactory twitterFactory = new TwitterFactory(cb.build());
		Twitter twitter = twitterFactory.getInstance();

		Query query = new Query("ISCTEIUL");
		query.setCount(1000);
		QueryResult searchResult = null;

		try {

			searchResult = twitter.search(query);

			List<Status> tweetsList = searchResult.getTweets();
			Iterator<Status> listIterator = tweetsList.iterator();

			while (listIterator.hasNext()) {
				Status tweet = (Status) listIterator.next();
				if (tweet.getUser().getScreenName().equals("ISCTEIUL")) {
					if (tweet.getText().contains(info)) {
						// System.out.println(tweet.getUser().getName() + ": " + tweet.getText());
						finalTweetsList.add(tweet);
					}
				}
			}

			Collections.sort(finalTweetsList, new Comparator<Status>() {
				public int compare(Status o1, Status o2) {
					return o2.getCreatedAt().compareTo(o1.getCreatedAt());
				}
			});

			for (Status s : finalTweetsList) {

				Calendar calendar = Calendar.getInstance();

				if (period.equals("Choose time...")) {
					String headerTweet = "Twitter: " + s.getCreatedAt().toString() + "   "
							+ s.getText().substring(0, 30) + "...";
					listModel.addElement(headerTweet);
					System.out.println(s.getUser().getName() + ": " + s.getText());
				}

				if (period.equals("Last hour")) {
					calendar.add(Calendar.HOUR_OF_DAY, -1);
					if (s.getCreatedAt().after(calendar.getTime())) {
						String headerTweet = "Twitter: " + s.getCreatedAt().toString() + "   "
								+ s.getText().substring(0, 30) + "...";
						listModel.addElement(headerTweet);
					}
				}

				if (period.equals("Last day")) {
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					if (s.getCreatedAt().after(calendar.getTime())) {
						String headerTweet = "Twitter: " + s.getCreatedAt().toString() + "   "
								+ s.getText().substring(0, 30) + "...";
						listModel.addElement(headerTweet);
					}
				}

				if (period.equals("Last week")) {
					calendar.add(Calendar.DAY_OF_MONTH, -7);
					if (s.getCreatedAt().after(calendar.getTime())) {
						String headerTweet = "Twitter: " + s.getCreatedAt().toString() + "   "
								+ s.getText().substring(0, 30) + "...";
						listModel.addElement(headerTweet);
					}
				}

				if (period.equals("Last month")) {
					calendar.add(Calendar.MONTH, -1);
					if (s.getCreatedAt().after(calendar.getTime())) {
						String headerTweet = "Twitter: " + s.getCreatedAt().toString() + "   "
								+ s.getText().substring(0, 30) + "...";
						listModel.addElement(headerTweet);
					}
				}

			}

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Returns the current frame.
	 * @return frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Sets the specified frame to the GUI object.
	 * @param frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

}
