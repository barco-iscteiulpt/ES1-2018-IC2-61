import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.SearchResource;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterHandler {

	private String authConsumerKey = "0I3XKOkznUjpuwdDOSkLvcSpg";
	private String authConsumerSecret = "zy8i9meaxzK5Rn05rKIsJwWvclJPfdpmtfnE5UuJvHxsW6oZ0G";
//	private String authAccessToken = "325579017-itc5klbFYmBcGvHUaZaUz0sCD29J7GVfuMiw5ZCg";
//	private String authAccessTokenSecret = "Wz55x8BoTY8wdU5zwQCBI45520ic5JjLi9VCHXBArg5JT";
	private String authAccessToken;
	private String authAccessTokenSecret;

	public ArrayList<Status> finalTweetsList;

	public void login() {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(authConsumerKey)
		.setOAuthConsumerSecret(authConsumerSecret);

		try {


			TwitterFactory twitterFactory = new TwitterFactory(cb.build());
			Twitter twitter = twitterFactory.getInstance();
			RequestToken requestToken = twitter.getOAuthRequestToken();
			
			System.out.println("got request token");
			System.out.println("request token: "+ requestToken.getToken());
			System.out.println("request token secret: "+ requestToken.getTokenSecret());
			System.out.println("--------");
			
			AccessToken accessToken = null;
			BufferedReader br = new BufferedReader(new InputStreamReader (System.in));
			
			while(accessToken==null) {
				
				System.out.println("open the url");
				System.out.println(requestToken.getAuthorizationURL());
				System.out.println("enter the pin");
				String pin = br.readLine();
				
				if (pin.length()>0) {
					accessToken = twitter.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = twitter.getOAuthAccessToken(requestToken);
				}
			}
			
			System.out.println("got access token");
			authAccessToken = accessToken.getToken();
			authAccessTokenSecret = accessToken.getTokenSecret();

		} catch (TwitterException e) {
			System.out.println("failed to get timeline");
		} catch (IOException e1) {
			System.out.println("failed to read the system input");
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

		finalTweetsList = new ArrayList<>();

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(authConsumerKey)
		.setOAuthConsumerSecret(authConsumerSecret)
		.setOAuthAccessToken(authAccessToken)
		.setOAuthAccessTokenSecret(authAccessTokenSecret);

		TwitterFactory twitterFactory = new TwitterFactory(cb.build());
		Twitter twitter = twitterFactory.getInstance();

		Query query = new Query("Andreecarvalh0");
		query.setCount(100);
		QueryResult searchResult = null;

		try {

			searchResult = ((SearchResource) twitter).search(query);
			List<Status> tweetsList = searchResult.getTweets();
			Iterator<Status> listIterator = tweetsList.iterator();
			Calendar calendar = Calendar.getInstance();

			while (listIterator.hasNext()) {
				Status tweet = (Status) listIterator.next();
				if (tweet.getUser().getScreenName().equals("Andreecarvalh0") && tweet.getText().contains(info)) {
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


	/**
	 * Retweets the specified Status in its parameter.
	 * 
	 * @param tweetId number of the tweet to retweet
	 */
	public void retweet(long tweetId) {

		try {
			TwitterFactory factory = new TwitterFactory();
			Twitter twitter = factory.getInstance();
			twitter.setOAuthConsumer(authConsumerKey, authConsumerSecret);
			AccessToken accessToken = new AccessToken(authAccessToken, authAccessTokenSecret);
			twitter.setOAuthAccessToken(accessToken);
			twitter.retweetStatus(tweetId);
		} catch (TwitterException e) {
			JOptionPane.showMessageDialog(null, "You have already retweeted this tweet!");
		}

	}


	/**
	 * Favorites the specified Status in its parameter.
	 * 	 
	 * @param tweetId number of the tweet to favorite
	 */
	public void favorite(long tweetId) {

		try {
			TwitterFactory factory = new TwitterFactory();
			Twitter twitter = factory.getInstance();
			twitter.setOAuthConsumer(authConsumerKey, authConsumerSecret);
			AccessToken accessToken = new AccessToken(authAccessToken, authAccessTokenSecret);
			twitter.setOAuthAccessToken(accessToken);
			Status status = twitter.createFavorite(tweetId);
		} catch (TwitterException e) {
			JOptionPane.showMessageDialog(null, "You have already favorited this tweet!");
		}

	}

	/**
	 * Replies to the specified Status in its first parameter with the text specified as second parameter.
	 * 
	 * @param tweetId number of the tweet to reply
	 * @param reply actual text to create a reply
	 */
	public void reply(long tweetId, String reply) {
		try {
			TwitterFactory factory = new TwitterFactory();
			Twitter twitter = factory.getInstance();
			twitter.setOAuthConsumer(authConsumerKey, authConsumerSecret);
			AccessToken accessToken = new AccessToken(authAccessToken, authAccessTokenSecret);
			twitter.setOAuthAccessToken(accessToken);
			Status b = twitter.showStatus(tweetId);
			StatusUpdate statusUpdate = new StatusUpdate("@"+b.getUser().getScreenName()+" "+reply);
			statusUpdate.setInReplyToStatusId(tweetId);
			Status status = twitter.updateStatus(statusUpdate);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Returns the current Twitter tweets list.
	 * 
	 * @return finalTweetsList
	 */
	public ArrayList<Status> getFinalTweetsList() {
		return this.finalTweetsList;
	}

}
