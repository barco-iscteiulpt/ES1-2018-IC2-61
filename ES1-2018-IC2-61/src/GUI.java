import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.UIManager;

public class GUI extends Thread {

	private JFrame frame;
	private JTextField keywords;
	public DefaultTableModel tableModel;
	protected JTable timeline;
	public FacebookHandler facebook;
	public TwitterHandler twitter;
	private Config configAccounts = new Config();

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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		facebook = new FacebookHandler();
		twitter = new TwitterHandler();

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
		frame.getContentPane().add(center, BorderLayout.CENTER);

		// Create scrolling panes. Create timeline and article boxes. Add them to the
		// respective panes.
		tableModel = new DefaultTableModel();
		timeline = new JTable(tableModel);
		JTextArea article = new JTextArea();

		JScrollPane scrollPaneTimeline = new JScrollPane();
		JScrollPane scrollPaneArticle = new JScrollPane();
		scrollPaneTimeline.setViewportView(timeline);
		scrollPaneArticle.setViewportView(article);
		article.setLineWrap(true);
		article.setWrapStyleWord(true);

		// Create table columns.
		tableModel.addColumn("Source");
		tableModel.addColumn("Date");
		tableModel.addColumn("Content");
		timeline.setFont(new Font("Arial", Font.PLAIN, 12));
		timeline.getTableHeader().setFont(new Font("Arial", Font.ITALIC, 13));

		// Add ListSelectionListener to timeline and ActionListener to the search
		// button.
		timeline.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (timeline.getSelectedRow() >= 0) {
					String text = tableModel.getValueAt(timeline.getSelectedRow(), 2).toString();
					article.setText(text);
				}
			}
		});

		center.add(scrollPaneTimeline);
		center.add(scrollPaneArticle);

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeline.clearSelection();
				article.setText(null);
				if (twitter_checkbox.isSelected()) {
					twitter.searchTwitter(keywords.getText(), comboBox.getSelectedItem().toString());
					manageTimeline();
					twitter.retweet();
					twitter.favorite();
//					twitter.reply();
				}
				if (fb_checkbox.isSelected()) {
					facebook.searchFacebook(keywords.getText(), comboBox.getSelectedItem().toString());
					manageTimeline();
				}
				if (timeline.getModel().getRowCount() == 0) {
					JOptionPane.showMessageDialog(null, "No search results!");
				}
			}
		});

	}

	protected void configFrame() {
		JFrame config = new JFrame();
		config.setLayout(new GridLayout(5, 1));
		JLabel fb_icon = new JLabel(new ImageIcon("src/resources/facebook_big.png"));
		JLabel twitter_icon = new JLabel(new ImageIcon("src/resources/twitter_big.png"));
		JLabel email_icon = new JLabel(new ImageIcon("src/resources/gmail_big.png"));

		// Line 1
		JPanel panel1;
		JLabel labelFb;
		JLabel fbAccount = new JLabel();
		JButton actionFb;

		if (configAccounts.getFacebookAccount() == null) {
			panel1 = new JPanel();
			panel1.setLayout(new BorderLayout());
			panel1.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			labelFb = new JLabel("Facebook");
			labelFb.setHorizontalAlignment(JLabel.CENTER);
			actionFb = new JButton("Login");
			actionFb.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					JTextField conta = new JTextField(20);
					JTextField token = new JTextField(20);
					JPanel dialog = new JPanel();
					dialog.add(new JLabel("Conta: "));
					dialog.add(conta);
					dialog.add(Box.createHorizontalStrut(15));
					dialog.add(new JLabel("Token: "));
					dialog.add(token);

					int result = JOptionPane.showConfirmDialog(null, dialog, "Please enter account info",
							JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						configAccounts.write("Facebook", conta.getText(), token.getText());
						configAccounts.read("Facebook");
						config.dispose();
						configFrame();
					}
				}
			});

		} else {
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

		if (configAccounts.getTwitterAccount() == null) {
			panel2 = new JPanel();
			panel2.setLayout(new BorderLayout());
			panel2.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			labelTw = new JLabel("Twitter");
			labelTw.setHorizontalAlignment(JLabel.CENTER);
			actionTw = new JButton("Login");
			actionTw.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					JTextField conta = new JTextField(20);
					JTextField token = new JTextField(20);
					JPanel dialog = new JPanel();
					dialog.add(new JLabel("Conta: "));
					dialog.add(conta);
					dialog.add(Box.createHorizontalStrut(15));
					dialog.add(new JLabel("Token: "));
					dialog.add(token);

					int result = JOptionPane.showConfirmDialog(null, dialog, "Please enter account info",
							JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						configAccounts.write("Twitter", conta.getText(), token.getText());
						configAccounts.read("Twitter");
						config.dispose();
						configFrame();
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
					configAccounts.delete("Twitter");
					configAccounts.read("Twitter");
					config.dispose();
					configFrame();
				}
			});
		}
		

		// Line 3
		JPanel panel3;
		JLabel labelEm;
		JLabel emAccount = new JLabel();
		JButton actionEm;

		if (configAccounts.getEmailAccount() == null) {
			panel3 = new JPanel();
			panel3.setLayout(new BorderLayout());
			panel3.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
			labelEm = new JLabel("Email");
			labelEm.setHorizontalAlignment(JLabel.CENTER);
			actionEm = new JButton("Login");
			actionEm.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					JTextField conta = new JTextField(20);
					JTextField token = new JTextField(20);
					JPanel dialog = new JPanel();
					dialog.add(new JLabel("Conta: "));
					dialog.add(conta);
					dialog.add(Box.createHorizontalStrut(15));
					dialog.add(new JLabel("Token: "));
					dialog.add(token);

					int result = JOptionPane.showConfirmDialog(null, dialog, "Please enter account info",
							JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						configAccounts.write("Email", conta.getText(), token.getText());
						configAccounts.read("Email");
						config.dispose();
						configFrame();
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
		
		

		config.add(panel1);
		config.add(new JSeparator(JSeparator.HORIZONTAL));
		config.add(panel2);
		config.add(new JSeparator(JSeparator.HORIZONTAL));
		config.add(panel3);

		config.setBounds(100,100, 400, 300);
		config.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		config.setVisible(true);

	}

	private void manageTimeline() {

		ArrayList<Post> postsList = facebook.getFinalPostsList();
		ArrayList<Status> tweetsList = twitter.getFinalTweetsList();
		tableModel.setRowCount(0);
		timeline.clearSelection();

		if (postsList != null) {
			for (Post p : postsList) {
				tableModel.addRow(new Object[]{"Facebook", p.getCreatedTime(), p.getMessage()});

			}
		}

		if (tweetsList != null) {
			for (Status t : tweetsList) {
				tableModel.addRow(new Object[]{"Twitter", t.getCreatedAt(), t.getText()+"\n"+"\n"+"ID: "+t.getId()});
			}
		}
		sortTable();
	}

	private void sortTable() {
		
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel> (tableModel);
		sorter.setComparator(1, Comparator.naturalOrder());
		timeline.setRowSorter(sorter);

//		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
//		sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
	}

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

	public String getKeywords() {
		return keywords.getText();
	}

	public DefaultTableModel getTableModel() {
		return this.tableModel;
	}

	/**
	 * Sets the specified frame to the GUI object.
	 * 
	 * @param frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setFont(new Font("Arial", Font.PLAIN, 12));
	}

}
