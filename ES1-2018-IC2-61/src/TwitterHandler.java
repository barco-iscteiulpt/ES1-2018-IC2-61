import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.SearchResource;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterHandler {

	public ArrayList<Status> finalTweetsList;

	/**
	 * Accesses a specific Twitter page, gets its tweets and saves each one on a
	 * list. Organizes the list from the most recent tweets to the oldest. Chooses
	 * which tweets are displayed depending on the JComboBox chosen item.
	 *
	 * @param info   The keywords used on the JTextField
	 * @param period The time period chosen on the JComboBox
	 */
	public void searchTwitter(String info, String period) {

		finalTweetsList = new ArrayList<>();

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("0I3XKOkznUjpuwdDOSkLvcSpg")
				.setOAuthConsumerSecret("zy8i9meaxzK5Rn05rKIsJwWvclJPfdpmtfnE5UuJvHxsW6oZ0G")
				.setOAuthAccessToken("325579017-itc5klbFYmBcGvHUaZaUz0sCD29J7GVfuMiw5ZCg")
				.setOAuthAccessTokenSecret("Wz55x8BoTY8wdU5zwQCBI45520ic5JjLi9VCHXBArg5JT");

		TwitterFactory twitterFactory = new TwitterFactory(cb.build());
		Twitter twitter = twitterFactory.getInstance();

		Query query = new Query("marianasilvamss");
		query.setCount(100);
		QueryResult searchResult = null;

		try {

			searchResult = ((SearchResource) twitter).search(query);
			List<Status> tweetsList = searchResult.getTweets();
			Iterator<Status> listIterator = tweetsList.iterator();
			Calendar calendar = Calendar.getInstance();

			while (listIterator.hasNext()) {
				Status tweet = (Status) listIterator.next();
				if (tweet.getUser().getScreenName().equals("marianasilvamss") && tweet.getText().contains(info)) {
					if (period.equals("Anytime")) {
						finalTweetsList.add(tweet);
					}
					if (period.equals("Last hour")) {
						calendar.add(Calendar.HOUR_OF_DAY, -1);
						if (tweet.getCreatedAt().after(calendar.getTime())) {
							finalTweetsList.add(tweet);
						}
					}

					if (period.equals("Last day")) {
						calendar.add(Calendar.DAY_OF_MONTH, -1);
						if (tweet.getCreatedAt().after(calendar.getTime())) {
							finalTweetsList.add(tweet);
						}
					}

					if (period.equals("Last week")) {
						calendar.add(Calendar.DAY_OF_MONTH, -7);
						if (tweet.getCreatedAt().after(calendar.getTime())) {
							finalTweetsList.add(tweet);
						}
					}

					if (period.equals("Last month")) {
						calendar.add(Calendar.MONTH, -1);
						if (tweet.getCreatedAt().after(calendar.getTime())) {
							finalTweetsList.add(tweet);
						}
					}
				}
			}

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Status> getFinalTweetsList() {
		return this.finalTweetsList;
	}

}
