import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.types.Comment;
import com.restfb.types.Group;
import com.restfb.types.Post;

public class FacebookHandler {

	
	protected String accessToken2 = "EAAEprSZC8PBABAERNOUaTHT17JMZCnuHwOZBL3EGknDVPGGZAss879cu9c38Of0LFnYZCVZA3iZCcO8KhyQn4J7lsFmsgZArS6TJVGoPOAZBmJMWMH3g3032KV8ajmpdqk5mvlaKBeAZC7ZCkS4N108jMAKkDYvR4DjFdrEq8v5IudGFgZDZD";
	protected String pageToken = "EAAEprSZC8PBABAMGZCJdBrU0KAYnv97FHHIfWuiJ39mt1Br5ZBZBq1XblyomzIhCoHKNi7VzxZBRcgMZA0sEODdnjZCO889RMYZADt72mDp7trfkv3eIGBvKNGxGBYxIXuEvGdXxZAiJZBm5fo27lV0wAp0I9ez8hieGahNLl8fXArNd1uUNB9t5uHU0jQ2HIotiQMRoToIXP72AZDZD";
	
	public ArrayList<Post> finalPostsList;
	//public boolean loggedIn;
	private Config configs = Config.getInstance();
	public String accessToken = configs.getFacebookToken();

	public void login() {
		
		try {
			Desktop.getDesktop().browse(new URL("https://developers.facebook.com/tools/explorer/").toURI());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
//		System.setProperty("webdriver.chrome.driver", "chromedriver");
//		WebDriver driver = new ChromeDriver();
//		driver.get("https://developers.facebook.com");
	}


	/**
	 * Accesses a specific Facebook page, gets its feed and saves every post on a
	 * list. Organizes the list from the most recent posts to the oldest. Chooses
	 * which posts are displayed depending on the JComboBox chosen item.
	 * 
	 * @param info   The keywords used on the JTextField
	 * @param period The time period chosen on the JComboBox
	 */
	@SuppressWarnings("deprecation")
	public void searchFacebook(String info, String period) {
		
		FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		Group group = fbClient.fetchObject("494271834397031", Group.class);
		Connection<Post> postFeed = fbClient.fetchConnection(group.getId() + "/feed", Post.class);

		finalPostsList = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();

		for (List<Post> postPage : postFeed) {
			for (Post post : postPage) {
				if (post.getMessage() != null && post.getMessage().contains(info)) {
					if (period.equals("Anytime")) {
						finalPostsList.add(post);
					}
					if (period.equals("Last hour")) {
						calendar.add(Calendar.HOUR_OF_DAY, -1);
						if (post.getUpdatedTime().after(calendar.getTime())) {
							finalPostsList.add(post);
						}
					}

					if (period.equals("Last day")) {
						calendar.add(Calendar.DAY_OF_MONTH, -1);
						if (post.getUpdatedTime().after(calendar.getTime())) {
							finalPostsList.add(post);
						}
					}

					if (period.equals("Last week")) {
						calendar.add(Calendar.DAY_OF_MONTH, -7);
						if (post.getUpdatedTime().after(calendar.getTime())) {
							finalPostsList.add(post);
							
						}
					}

					if (period.equals("Last month")) {
						calendar.add(Calendar.MONTH, -1);
						if (post.getUpdatedTime().after(calendar.getTime())) {
							finalPostsList.add(post);
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void comment(String postId, String comment) {
		try {
			FacebookClient fbClient = new DefaultFacebookClient(pageToken);
			fbClient.publish(postId+"/comments", Comment.class, Parameter.with("message", comment));
		}
		catch (com.restfb.exception.FacebookOAuthException e) {
			JOptionPane.showMessageDialog(null, "Only Facebook pages can comment.");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void like(String postId) {
		try {
			FacebookClient fbClient = new DefaultFacebookClient(pageToken);
			fbClient.publish(postId+"/likes", Boolean.class);
		}
		catch (com.restfb.exception.FacebookOAuthException e) {
			JOptionPane.showMessageDialog(null, "Only Facebook pages can like.");
		}
	}

	/**
	 * Returns the current Facebook Posts list.
	 * 
	 * @return finalPostsList
	 */
	public ArrayList<Post> getFinalPostsList() {
		return finalPostsList;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
