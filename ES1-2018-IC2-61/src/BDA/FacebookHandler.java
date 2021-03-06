package BDA;

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
import com.restfb.types.Comment;
import com.restfb.types.Group;
import com.restfb.types.Post;

public class FacebookHandler {

	protected String pageToken = "EAAEprSZC8PBABAIJMzvVkTBeQpVaV2L8rmONckQKZB3dZABDZC94gSiDrbGZCCZCwK5X1hZBya69zpJ0nmyZBpaZBOUDV2NJiYm2PqMnS5fcow43HrOkbJTs2FUTR5eOLJ504RuPuq3hza0WGtIXMJvWcoG5AfgXXfGI2701jaLcmUgZDZD";

	public ArrayList<Post> finalPostsList;
	private Config configs = Config.getInstance();
	public String accessToken = configs.getFacebookToken();
	public boolean loggedIn;

	/**
	 * Opens the browser on the Facebook developers page which allows them to
	 * retrieve their User Access Token.
	 */
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

	}

	/**
	 * Accesses a specific Facebook page, gets its feed and saves every post on
	 * a list. Organizes the list from the most recent posts to the oldest.
	 * Chooses which posts are displayed depending on the JComboBox chosen item.
	 * 
	 * @param info
	 *            The keywords used on the JTextField
	 * @param period
	 *            The time period chosen on the JComboBox
	 */
	@SuppressWarnings("deprecation")
	public void searchFacebook(String info, String period) {

		try {

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
		} catch (com.restfb.exception.FacebookNetworkException e1) {
			JOptionPane.showMessageDialog(null, "No internet connection.");
		}
	}

	/**
	 * Publishes the specified comment on the specified post on a page. Only
	 * pages are allowed to do this, otherwise it will display a dialog.
	 * 
	 * @param postId
	 *            the post where the user will comment
	 * @param comment
	 *            the text to be in the comment
	 */
	@SuppressWarnings("deprecation")
	public void comment(String postId, String comment) {
		try {
			FacebookClient fbClient = new DefaultFacebookClient(pageToken);
			fbClient.publish(postId + "/comments", Comment.class, Parameter.with("message", comment));
			JOptionPane.showMessageDialog(null, "Comment sent successfully.");
		} catch (com.restfb.exception.FacebookOAuthException e) {
			JOptionPane.showMessageDialog(null, "Only Facebook pages can comment.");
		} catch (com.restfb.exception.FacebookNetworkException e1) {
			JOptionPane.showMessageDialog(null, "No internet connection.");
		}
	}

	/**
	 * Likes the specified post on a page. Only pages are allowed to do this,
	 * otherwise it will display a dialog.
	 * 
	 * @param postId
	 *            the post the user will like
	 */
	@SuppressWarnings("deprecation")
	public void like(String postId) {
		try {
			FacebookClient fbClient = new DefaultFacebookClient(pageToken);
			fbClient.publish(postId + "/likes", Boolean.class);
			JOptionPane.showMessageDialog(null, "Post liked.");
		} catch (com.restfb.exception.FacebookOAuthException e) {
			JOptionPane.showMessageDialog(null, "Only Facebook pages can like.");
		} catch (com.restfb.exception.FacebookNetworkException e1) {
			JOptionPane.showMessageDialog(null, "No internet connection.");
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

	/**
	 * Sets the specified access token
	 * 
	 * @param accessToken
	 *            the current access token
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
