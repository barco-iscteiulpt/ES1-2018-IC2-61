import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Page;
import com.restfb.types.Post;

public class FacebookHandler {

	protected String accessToken = "EAAEprSZC8PBABAJyQpTxdEQaXh9dkvPOopFsDUmjJIB3m7n3IT6rxiWOXSwPpYzCJtUm0CH3D6pjzCPoH9ZAYgplLKeRwkQ2ZCcEsZCc4lU9olFarz3Pcz5JJb6zZCzUH94DSidZAbw85xyV3JpsQSx5RZCaWICuel33cJH4TIufR4BPDti62z5NVhaSAM2s54ZD";
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
		FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		Page page = fbClient.fetchObject("listajespeniche", Page.class);
		Connection<Post> postFeed = fbClient.fetchConnection(page.getId() + "/feed", Post.class);
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

	public ArrayList<Post> getFinalPostsList() {
		return finalPostsList;
	}

}
