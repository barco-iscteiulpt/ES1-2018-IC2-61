import java.awt.Desktop;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
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
	private String authAccessToken;
	private boolean connected = true;
	private String authAccessTokenSecret;

	ConfigurationBuilder cbLogin = new ConfigurationBuilder().setDebugEnabled(true).setOAuthConsumerKey(authConsumerKey)
			.setOAuthConsumerSecret(authConsumerSecret);
	TwitterFactory twitterFactory = new TwitterFactory(cbLogin.build());
	Twitter twitter = twitterFactory.getInstance();
	RequestToken requestToken;

	public ArrayList<Status> finalTweetsList;
	public boolean loggedIn;
	public String loginTwitter;

	public void open() {	
		try {
			System.out.println("Token no inicio do open(): " + requestToken);

			requestToken = twitter.getOAuthRequestToken();
			setLoginTwitter(requestToken.getAuthorizationURL());
	        Desktop.getDesktop().browse(new URL(loginTwitter).toURI());
	        
			setLoginTwitter(requestToken.getAuthorizationURL()); 
			Desktop.getDesktop().browse(new URL(loginTwitter).toURI());

		} catch (TwitterException e) {
			JOptionPane.showMessageDialog(null, "Incorrect PIN.");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	
	public RequestToken getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}

	public void login(String pin) {
		try {
			AccessToken accessToken = null;
			while(accessToken==null) {
				if (pin.length()>0) {
					accessToken = twitter.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = twitter.getOAuthAccessToken(requestToken);
				}
			}
			authAccessToken = accessToken.getToken();
			authAccessTokenSecret = accessToken.getTokenSecret();
			this.loggedIn = true;
		} catch (TwitterException e) {
			JOptionPane.showMessageDialog(null, "Incorrect PIN.");
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
			checkConnection();
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
			checkConnection();
			System.out.println(connected);
			if(connected) {
				JOptionPane.showMessageDialog(null, "You have already retweeted this tweet!");
			}
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
			twitter.createFavorite(tweetId);
		} catch (TwitterException e) {
			checkConnection();
			if(connected) 
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
			twitter.updateStatus(statusUpdate);
		} catch (TwitterException e) {
			checkConnection();
		}

	}
	
	public void checkConnection() {
		  try {
		        final URL url = new URL("http://www.google.com");
		        final URLConnection connection = url.openConnection();
		        connection.connect();
		        connection.getInputStream().close();
		        if(!connected)
		        	connected = true;
		    } catch (MalformedURLException e) {
		    } catch (IOException e) {
		    	JOptionPane.showMessageDialog(null, "No internet connection.");
		    	connected = false; 
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

	public void setLoginTwitter(String logginTwitter) {
		this.loginTwitter = logginTwitter;
	}
	
	public String getAuthAccessToken() {
		return authAccessToken;
	}

	public void setAuthAccessToken(String authAccessToken) {
		this.authAccessToken = authAccessToken;
	}

	public String getAuthAccessTokenSecret() {
		return authAccessTokenSecret;
	}

	public void setAuthAccessTokenSecret(String authAccessTokenSecret) {
		this.authAccessTokenSecret = authAccessTokenSecret;
	}

}
