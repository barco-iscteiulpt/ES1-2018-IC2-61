import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.UIManager;

public class GUI extends Thread {

	private JFrame frame;
	private JTextField keywords;
	public DefaultTableModel tableModel;
	protected JTable timeline;
	protected JTextArea article ;
	public FacebookHandler facebook;
	public TwitterHandler twitter;
	public EmailHandler email;
	private Config configAccounts = new Config();
	private JPanel content;
	private JPanel contentSouth;
	private long tweetId;
	private String postLink;
	private String postId;
	private Message currentEmail;
	private String replyTo;
	private String replyFrom;
	public boolean isClicked=false;

	/**
	 * Create the application.
	 */
	public GUI() {
		addFrameContent();
	}

	@Override
	public void run() {
		addFrameContent();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void addFrameContent() {

		// Create the frame. Specify the title, placement, size, closing operation and
		// layout of the frame.
		frame = new JFrame();
		frame.setTitle("Bom Dia Academia!");
		frame.setBounds(100, 100, 800, 600);
		ImageIcon appIcon = new ImageIcon("src/resources/app-icon.png");
		frame.setIconImage(appIcon.getImage());


		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				tableToXML();
				frame.dispose();
			}
		});

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		facebook = new FacebookHandler();
		twitter = new TwitterHandler();
		email = new EmailHandler();

		// Create the menu bar. Create and add 3 menus.
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("File");
		menu1.setFont(new Font("Arial", Font.PLAIN, 14));
		JMenu menu2 = new JMenu("Edit");
		menu2.setFont(new Font("Arial", Font.PLAIN, 14));
		JMenu menu3 = new JMenu("About");
		menu3.setFont(new Font("Arial", Font.PLAIN, 14));

		// Create menu items and add them to their respective menu.
		JMenuItem item1 = new JMenuItem("Close");
		item1.setFont(new Font("Arial", Font.PLAIN, 14));
		JMenuItem item2 = new JMenuItem("New");
		item2.setFont(new Font("Arial", Font.PLAIN, 14));
		menu1.add(item2);
		menu1.add(item1);
		JMenuItem item3 = new JMenuItem("Configurations");
		menu2.add(item3);
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

		item3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				configFrame();
			}
		});

		frame.setJMenuBar(menuBar);

		// Create the top panel. Specify height and layout of the panel. Add it to the
		// frame.
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(1, 2, 0, 0));
		frame.getContentPane().add(top, BorderLayout.NORTH);

		// Create the left side of the top panel. Specify layout and orientation. Add to
		// top panel.
		JPanel top_left = new JPanel();
		top_left.setBackground(UIManager.getColor("CheckBox.background"));
		top_left.setLayout(new FlowLayout(FlowLayout.LEADING));
		top.add(top_left);

		// Get social icons from "resources" folder. Create 3 checkboxes (selected by
		// default). Add them to top-left panel.
		JLabel fb_icon = new JLabel(new ImageIcon("src/resources/facebook.png"));
		JLabel twitter_icon = new JLabel(new ImageIcon("src/resources/twitter.png"));
		JLabel email_icon = new JLabel(new ImageIcon("src/resources/gmail.png"));

		JCheckBox fb_checkbox = new JCheckBox();
		fb_checkbox.setSelected(false);
		JCheckBox twitter_checkbox = new JCheckBox();
		twitter_checkbox.setSelected(false);
		JCheckBox email_checkbox = new JCheckBox();
		email_checkbox.setSelected(false);

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
		String[] options = { "Anytime", "Last hour", "Last day", "Last week", "Last month" };
		JComboBox comboBox = new JComboBox(options);
		comboBox.setFont(new Font("Arial", Font.PLAIN, 13));
		comboBox.setSelectedIndex(0);
		top_right.add(comboBox);

		// Define keyword search and create "Search" button. Specify dimensions, margins
		// and icon. Add ActionListener. Add to top-right panel.
		keywords = new JTextField();
		JTextField reply = new JTextField();
		reply.setPreferredSize(new Dimension(120, 24));
		reply.setToolTipText("Insert your reply");

		JTextField comment = new JTextField();
		comment.setPreferredSize(new Dimension(120, 24));
		comment.setToolTipText("Insert your comment");

		JTextField emailReply = new JTextField();
		emailReply.setPreferredSize(new Dimension(120, 24));
		emailReply.setToolTipText("Insert your email reply");
		
		JTextField emailSubject = new JTextField();
		emailSubject.setPreferredSize(new Dimension(120, 24));
		emailSubject.setToolTipText("Insert your email subject");

		JButton searchButton = new JButton();

		JButton retweetButton = new JButton();
		retweetButton.setMargin(new Insets(8, 8, 8, 8));
		retweetButton.setIcon(new ImageIcon(GUI.class.getResource("/resources/retweet.png")));

		JButton favoriteButton = new JButton();
		favoriteButton.setMargin(new Insets(8, 8, 8, 8));
		favoriteButton.setIcon(new ImageIcon(GUI.class.getResource("/resources/heart.png")));

		JButton replyButton = new JButton();
		replyButton.setMargin(new Insets(8, 8, 8, 8));
		replyButton.setIcon(new ImageIcon(GUI.class.getResource("/resources/reply.png")));

		JButton likeButton = new JButton();
		likeButton.setMargin(new Insets(8, 8, 8, 8));
		likeButton.setIcon(new ImageIcon(GUI.class.getResource("/resources/like.png")));	

		JButton commentButton = new JButton();
		commentButton.setMargin(new Insets(8, 8, 8, 8));
		commentButton.setIcon(new ImageIcon(GUI.class.getResource("/resources/comment.png")));

		JButton sendEmailButton = new JButton();
		sendEmailButton.setMargin(new Insets(8, 8, 8, 8));
		sendEmailButton.setIcon(new ImageIcon(GUI.class.getResource("/resources/send-button.png")));

		reply.setVisible(false);
		retweetButton.setVisible(false);
		favoriteButton.setVisible(false);
		replyButton.setVisible(false);
		commentButton.setVisible(false);
		likeButton.setVisible(false);
		comment.setVisible(false);
		emailReply.setVisible(false);
		sendEmailButton.setVisible(false);
		emailSubject.setVisible(false);

		keywords.setPreferredSize(new Dimension(120, 24));
		keywords.setToolTipText("Insert keywords");
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
		frame.getContentPane().add(center, BorderLayout.CENTER);

		// Create scrolling panes. Create timeline and article boxes. Add them to the
		// respective panes.
		tableModel = new DefaultTableModel();
		timeline = new JTable(tableModel);
		content = new JPanel();
		content.setSize(content.getWidth(), 100);
		contentSouth = new JPanel();
		content.setLayout(new BorderLayout());
		contentSouth.add(reply);
		contentSouth.add(replyButton);
		contentSouth.add(retweetButton);
		contentSouth.add(favoriteButton);

		contentSouth.add(comment);
		contentSouth.add(commentButton);
		contentSouth.add(likeButton);

		contentSouth.add(emailSubject);
		contentSouth.add(emailReply);
		contentSouth.add(sendEmailButton);

		article = new JTextArea();

		JScrollPane scrollPaneTimeline = new JScrollPane();
		JScrollPane scrollPaneArticle = new JScrollPane();
		scrollPaneTimeline.setViewportView(timeline);
		scrollPaneArticle.setViewportView(article);
		scrollPaneArticle.setPreferredSize(new Dimension(content.getWidth(),460));
		article.setLineWrap(true);
		article.setWrapStyleWord(true);
		content.add(scrollPaneArticle, BorderLayout.NORTH);
		content.add(contentSouth, BorderLayout.SOUTH);


		// Create table columns.
		tableModel.addColumn("Source");
		tableModel.addColumn("Date");
		tableModel.addColumn("Content");
		tableModel.addColumn("Object");
		timeline.getColumnModel().getColumn(3).setWidth(0);
		timeline.getColumnModel().getColumn(3).setMinWidth(0);
		timeline.getColumnModel().getColumn(3).setMaxWidth(0);
		timeline.setFont(new Font("Arial", Font.PLAIN, 12));
		timeline.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 13));

		// Add ListSelectionListener to timeline and ActionListener to the search
		// button.
		timeline.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
					System.out.println("entrei no listener");
					if (timeline.getSelectedRow() >= 0) {

						String text = tableModel.getValueAt(timeline.getSelectedRow(), 2).toString();
						article.setText(text);

						if (tableModel.getValueAt(timeline.getSelectedRow(), 3) instanceof Status) {
							emailReply.setVisible(false);
							sendEmailButton.setVisible(false);
							emailSubject.setVisible(false);
							comment.setVisible(false);
							likeButton.setVisible(false);
							commentButton.setVisible(false);
							reply.setVisible(true);
							replyButton.setVisible(true);
							retweetButton.setVisible(true);
							favoriteButton.setVisible(true);		
							Status tweet = (Status) (tableModel.getValueAt(timeline.getSelectedRow(), 3));
							tweetId = tweet.getId();
						}

						if (tableModel.getValueAt(timeline.getSelectedRow(), 3) instanceof Post) {
							emailReply.setVisible(false);
							sendEmailButton.setVisible(false);
							emailSubject.setVisible(false);
							retweetButton.setVisible(false);
							favoriteButton.setVisible(false);
							reply.setVisible(false);
							replyButton.setVisible(false);
							comment.setVisible(true);
							likeButton.setVisible(true);
							commentButton.setVisible(true);
							Post post = (Post) (tableModel.getValueAt(timeline.getSelectedRow(), 3));
							postLink = "www.facebook.com/"+post.getId();
							postId = post.getId();
						}

						if (tableModel.getValueAt(timeline.getSelectedRow(), 3) instanceof Message) {
							retweetButton.setVisible(false);
							favoriteButton.setVisible(false);
							reply.setVisible(false);
							replyButton.setVisible(false);
							comment.setVisible(false);
							likeButton.setVisible(false);
							commentButton.setVisible(false);
							emailReply.setVisible(true);
							sendEmailButton.setVisible(true);
							emailSubject.setVisible(true);
							currentEmail = (Message) (tableModel.getValueAt(timeline.getSelectedRow(), 3));
							System.out.println(tableModel.getValueAt(timeline.getSelectedRow(), 2));

							try {	
								String aux = InternetAddress.toString(currentEmail.getFrom());
								if(aux.startsWith("E")) {
									int x = aux.indexOf("<");
									int y = aux.indexOf(">");
									String temp = aux.substring(x+1, y);
									aux = temp;
								}
								replyTo = aux;
								replyFrom = email.getUsername();
							} catch (MessagingException e1) {
								e1.printStackTrace();
							}
						}
					}
			}
		});

		retweetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				twitter.retweet(tweetId);
			}
		});

		favoriteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				twitter.favorite(tweetId);
			}
		});

		replyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (reply.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please insert a reply.");
				}else
					twitter.reply(tweetId, reply.getText());
			}
		});

		commentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				facebook.comment(postId, comment.getText());
			}
		});

		likeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				facebook.like(postId);
			}
		});

		sendEmailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				email.sendEmail(replyFrom, replyTo, emailSubject.getText(), emailReply.getText());
			}
		});

		center.add(scrollPaneTimeline);
		center.add(content);

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				retweetButton.setVisible(false);
				favoriteButton.setVisible(false);
				reply.setVisible(false);
				replyButton.setVisible(false);
				comment.setVisible(false);
				likeButton.setVisible(false);
				commentButton.setVisible(false);
				emailReply.setVisible(false);
				sendEmailButton.setVisible(false);
				emailSubject.setVisible(false);
				timeline.clearSelection();
				article.setText(null);
				if (twitter_checkbox.isSelected()) {
					if (twitter.loggedIn) {
						twitter.searchTwitter(keywords.getText(), comboBox.getSelectedItem().toString());
						manageTimeline();
					} 
				}
				if (fb_checkbox.isSelected()) {
					if (facebook.loggedIn) {
						facebook.searchFacebook(keywords.getText(), comboBox.getSelectedItem().toString());
						manageTimeline();
					} 
				}
				if (email_checkbox.isSelected()) {
					if(email.loggedIn) {
						email.searchGmail(keywords.getText(), comboBox.getSelectedItem().toString());
						manageTimeline();
					} 
				}
				if ((twitter_checkbox.isSelected() && !twitter.loggedIn) || (fb_checkbox.isSelected() && !facebook.loggedIn) || (email_checkbox.isSelected() && !email.loggedIn)) {
					JOptionPane.showMessageDialog(null, "Please loggin first (Edit>Configurations).");
				}
				if (timeline.getModel().getRowCount() == 0 && (twitter.loggedIn || facebook.loggedIn || email.loggedIn)) {
					JOptionPane.showMessageDialog(null, "No search results!");
				}
			}
		});

	}
	/**
	 * Convert all elements on the JTable to the XML file. 
	 */

	protected void tableToXML() {
		if(facebook.getFinalPostsList()!=null && twitter.getFinalTweetsList()!=null) {
			for(int i =0; i<facebook.getFinalPostsList().size();i++) {
				configAccounts.write(facebook.getFinalPostsList().get(i));
			}
			for(int i = 0; i<twitter.getFinalTweetsList().size();i++)
				configAccounts.write(twitter.getFinalTweetsList().get(i));
		}
	}

	/**
	 * Initializes the contents of the configuration frame and its adaptation to the XML file.
	 */

	protected void configFrame() {
		JFrame config = new JFrame();
		config.getContentPane().setLayout(new GridLayout(5, 1));
		JLabel fb_icon = new JLabel(new ImageIcon("src/resources/facebook_big.png"));
		JLabel twitter_icon = new JLabel(new ImageIcon("src/resources/twitter_big.png"));
		JLabel email_icon = new JLabel(new ImageIcon("src/resources/gmail_big.png"));

		// Line 1
		JPanel panel1;
		JLabel labelFb;
		JLabel fbAccount = new JLabel();
		JButton actionFb;

		System.out.println("Facebook: " + configAccounts.isLoggedFacebook());
		if (!configAccounts.isLoggedFacebook()) {
			panel1 = new JPanel();
			panel1.setLayout(new BorderLayout());
			panel1.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			labelFb = new JLabel("Facebook");
			labelFb.setHorizontalAlignment(JLabel.CENTER);
			actionFb = new JButton("Login");
			actionFb.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					JTextField token = new JTextField(20);
					JLabel label = new JLabel("      Get token > Get user token");
					JPanel dialog = new JPanel();
					JPanel aux = new JPanel();
//					aux.add(Box.createVerticalStrut(15));
					aux.setLayout(new GridLayout(2,1));
					dialog.add(Box.createHorizontalStrut(15));
					dialog.add(new JLabel("Token: "));
					dialog.add(token);
					aux.add(label);
					aux.add(dialog);
					facebook.login();
					
					int result = JOptionPane.showConfirmDialog(null, aux, "Please enter account info",
							JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
//						configAccounts.write(new LoginRequest("Facebook", conta.getText(), token.getText()));
						configAccounts.read("Facebook");
						config.dispose();
						configFrame();
						facebook.setAccessToken(token.getText());
						facebook.loggedIn = true;
					}
				}
			});

		} else {
			facebook.loggedIn = false;
			panel1 = new JPanel();
			panel1.setLayout(new BorderLayout());
			panel1.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			labelFb = new JLabel("Facebook");
			labelFb.setHorizontalAlignment(JLabel.CENTER);
			fbAccount = new JLabel(configAccounts.getFacebookAccount());
			actionFb = new JButton("Logout");
			actionFb.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					configAccounts.delete("Facebook");
					configAccounts.read("Facebook");
					System.out.println(configAccounts.getFacebookAccount());
					config.dispose();
					configFrame();
				}
			});
		}

		// Line 2
		JPanel panel2;
		JLabel labelTw;
		JLabel twAccount = new JLabel();
		JButton actionTw;

		if (!configAccounts.isLoggedTwitter()) {
			panel2 = new JPanel();
			panel2.setLayout(new BorderLayout());
			panel2.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			labelTw = new JLabel("Twitter");
			labelTw.setHorizontalAlignment(JLabel.CENTER);
			actionTw = new JButton("Login");
			actionTw.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					JTextField pin = new JTextField(20);
					JPanel dialog = new JPanel();
					dialog.add(Box.createHorizontalStrut(15));
					dialog.add(new JLabel("PIN: "));
					dialog.add(pin);
					twitter.open();

					int result = JOptionPane.showConfirmDialog(null, dialog, "Please enter account info",
							JOptionPane.OK_CANCEL_OPTION);

					if (result == JOptionPane.OK_OPTION) {
					//	configAccounts.write(new LoginRequest("Twitter", pin.getText(), username.getText()));
						configAccounts.read("Twitter");
						config.dispose();
						configFrame();
						twitter.login(pin.getText());
					}
					
				}
			});

		} else {
			panel2 = new JPanel();
			panel2.setLayout(new BorderLayout());
			panel2.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			labelTw = new JLabel("Twitter");
			labelTw.setHorizontalAlignment(JLabel.CENTER);
			twAccount = new JLabel(configAccounts.getTwitterAccount());
			actionTw = new JButton("Logout");
			actionTw.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					twitter.loggedIn = false;
					twitter.setAuthAccessToken(null);
					twitter.setAuthAccessTokenSecret(null);
					twitter.setRequestToken(null);
					configAccounts.delete("Twitter");
					configAccounts.read("Twitter");
					config.dispose();
					System.out.println(twitter.getRequestToken());
					configFrame();
				}
			});
		}


		// Line 3
		JPanel panel3;
		JLabel labelEm;
		JLabel emAccount = new JLabel();
		JButton actionEm;

		if (!configAccounts.isLoggedEmail()) {
			panel3 = new JPanel();
			panel3.setLayout(new BorderLayout());
			panel3.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			labelEm = new JLabel("Email");
			labelEm.setHorizontalAlignment(JLabel.CENTER);
			actionEm = new JButton("Login");
			actionEm.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					JTextField username = new JTextField(20);
					JPasswordField password = new JPasswordField(20);
					JPanel dialog = new JPanel();
					dialog.add(new JLabel("Email: "));
					dialog.add(username);
					dialog.add(Box.createHorizontalStrut(15));
					dialog.add(new JLabel("Password: "));
					dialog.add(password);
					password.setEchoChar('*');

					int result = JOptionPane.showConfirmDialog(null, dialog, "Please enter account info",
							JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						configAccounts.write(new LoginRequest("Email", username.getText(), password.getText()));
						configAccounts.read("Email");
						config.dispose();
						configFrame();
						email.login(username.getText(), password.getText());
					}
				}
			});

		} else {
			panel3 = new JPanel();
			panel3.setLayout(new BorderLayout());
			panel3.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			labelEm = new JLabel("Email");
			labelEm.setHorizontalAlignment(JLabel.CENTER);
			emAccount = new JLabel(configAccounts.getEmailAccount());
			actionEm = new JButton("Logout");
			actionEm.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					configAccounts.delete("Email");
					configAccounts.read("Email");
					config.dispose();
					configFrame();
				}
			});
		}


		panel1.add(labelFb, BorderLayout.WEST);
		panel1.add(fbAccount, BorderLayout.CENTER);
		panel1.add(actionFb, BorderLayout.EAST);
		panel1.add(fb_icon, BorderLayout.WEST);
		panel2.add(labelTw, BorderLayout.WEST);
		panel2.add(twAccount, BorderLayout.CENTER);
		panel2.add(actionTw, BorderLayout.EAST);
		panel2.add(twitter_icon, BorderLayout.WEST);
		panel3.add(labelEm, BorderLayout.WEST);
		panel3.add(emAccount, BorderLayout.CENTER);
		panel3.add(actionEm, BorderLayout.EAST);
		panel3.add(email_icon, BorderLayout.WEST);



		config.getContentPane().add(panel1);
		config.getContentPane().add(new JSeparator(JSeparator.HORIZONTAL));
		config.getContentPane().add(panel2);
		config.getContentPane().add(new JSeparator(JSeparator.HORIZONTAL));
		config.getContentPane().add(panel3);

		config.setBounds(100,100, 400, 300);
		config.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		config.setVisible(true);

	}
	
	/**
	 * Gets content from both Facebook and Twitter lists and adds them to the table.
	 */
	private void manageTimeline() {

		ArrayList<Post> postsList = facebook.getFinalPostsList();
		ArrayList<Status> tweetsList = twitter.getFinalTweetsList();
		ArrayList<Message> emailsList = email.getFinalEmailsList();
		tableModel.setRowCount(0);
		timeline.clearSelection();

		if (postsList != null) {
			for (Post p : postsList) {
				tableModel.addRow(new Object[]{"Facebook", p.getUpdatedTime(), p.getMessage(),p});
			}
		}

		if (tweetsList != null) {
			for (Status t : tweetsList) {
				tableModel.addRow(new Object[]{"Twitter", t.getCreatedAt(), t.getText(), t});
			}
		}

		if (emailsList != null) {
			for (Message m : emailsList) {
				try {
					tableModel.addRow(new Object[]{"Email", m.getSentDate(), m.getSubject(), m});
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}

		//		sortTable();
	}

	/**
	 * (unused) Organizes the table chronologically 
	 */

	private void sortTable() {

		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel> (tableModel);
		sorter.setComparator(1, Comparator.naturalOrder());
		timeline.setRowSorter(sorter);		

		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
	}

	/**
	 * Returns the current timeline.
	 * 
	 * @return timeline
	 */
	public JTable getTimeline() {
		return timeline;
	}

	/**
	 * Returns the current frame.
	 * 
	 * @return frame
	 */
	public JFrame getFrame() {
		return frame;
	}


	/**
	 * Returns the current keywords.
	 * 
	 * @return keywords.getText()
	 */
	public String getKeywords() {
		return keywords.getText();
	}


	/**
	 * Returns the current tableModel.
	 * 
	 * @return tableModel
	 */
	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	/**
	 * Sets the specified frame to the GUI object.
	 * 
	 * @param frame the current frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setFont(new Font("Arial", Font.PLAIN, 12));
	}



}
