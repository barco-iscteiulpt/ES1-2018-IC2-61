import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.Comment;
import com.restfb.types.Comments;
import com.restfb.types.FacebookType;
import com.restfb.types.Group;
import com.restfb.types.Page;
import com.restfb.types.Post;

public class FacebookHandler {

	protected String accessToken = "EAAEprSZC8PBABAJyQpTxdEQaXh9dkvPOopFsDUmjJIB3m7n3IT6rxiWOXSwPpYzCJtUm0CH3D6pjzCPoH9ZAYgplLKeRwkQ2ZCcEsZCc4lU9olFarz3Pcz5JJb6zZCzUH94DSidZAbw85xyV3JpsQSx5RZCaWICuel33cJH4TIufR4BPDti62z5NVhaSAM2s54ZD";
	protected String accessToken2 = "EAAEprSZC8PBABAERNOUaTHT17JMZCnuHwOZBL3EGknDVPGGZAss879cu9c38Of0LFnYZCVZA3iZCcO8KhyQn4J7lsFmsgZArS6TJVGoPOAZBmJMWMH3g3032KV8ajmpdqk5mvlaKBeAZC7ZCkS4N108jMAKkDYvR4DjFdrEq8v5IudGFgZDZD";
	protected String pageToken = "EAAEprSZC8PBABAMGZCJdBrU0KAYnv97FHHIfWuiJ39mt1Br5ZBZBq1XblyomzIhCoHKNi7VzxZBRcgMZA0sEODdnjZCO889RMYZADt72mDp7trfkv3eIGBvKNGxGBYxIXuEvGdXxZAiJZBm5fo27lV0wAp0I9ez8hieGahNLl8fXArNd1uUNB9t5uHU0jQ2HIotiQMRoToIXP72AZDZD";
	FacebookClient fbClient = new DefaultFacebookClient(accessToken2);
	Group group = fbClient.fetchObject("494271834397031", Group.class);
//	Page page = fbClient.fetchObject("listajespeniche", Page.class);
//	Connection<Post> postFeed = fbClient.fetchConnection(page.getId() + "/feed", Post.class);
	Connection<Post> postFeed = fbClient.fetchConnection(group.getId() + "/feed", Post.class);
	
	public ArrayList<Post> finalPostsList;

	/**
	 * Accesses a specific Facebook page, gets its feed and saves every post on a
	 * list. Organizes the list from the most recent posts to the oldest. Chooses
	 * which posts are displayed depending on the JComboBox chosen item.
	 * 
	 * @param info   The keywords used on the JTextField
	 * @param period The time period chosen on the JComboBox
	 */
	public void searchFacebook(String info, String period) {

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
						if (post.getCreatedTime().after(calendar.getTime())) {
							finalPostsList.add(post);
						}
					}

					if (period.equals("Last day")) {
						calendar.add(Calendar.DAY_OF_MONTH, -1);
						if (post.getCreatedTime().after(calendar.getTime())) {
							finalPostsList.add(post);
						}
					}

					if (period.equals("Last week")) {
						calendar.add(Calendar.DAY_OF_MONTH, -7);
						if (post.getCreatedTime().after(calendar.getTime())) {
							finalPostsList.add(post);
							
						}
					}

					if (period.equals("Last month")) {
						calendar.add(Calendar.MONTH, -1);
						if (post.getCreatedTime().after(calendar.getTime())) {
							finalPostsList.add(post);
						}
					}
				}
			}
		}
	}
	
	public void comment(String postId, String comment) {
		try {
			FacebookClient fbClient = new DefaultFacebookClient(pageToken);
			fbClient.publish(postId+"/comments", Comment.class, Parameter.with("message", comment));
		}
		catch (com.restfb.exception.FacebookOAuthException e) {
			JOptionPane.showMessageDialog(null, "Only Facebook pages can comment.");
		}
	}
	
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

}
