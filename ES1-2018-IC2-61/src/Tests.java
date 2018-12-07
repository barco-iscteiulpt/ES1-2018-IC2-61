import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.swing.event.ListSelectionListener;

import org.junit.Test;

import com.restfb.types.Post;

import twitter4j.Status;

public class Tests {

	@Test
	public void facebookSearchShouldReturnOnePost() {
		//When "Maltinha" is searched only on facebook, it should return only one post
		GUI gui = new GUI();
		gui.facebook.searchFacebook("Maltinha", "Anytime");
		ArrayList<Post> list = gui.facebook.getFinalPostsList();
		assertEquals(0,list.size());
	}
	
	@Test
	public void twitterSearchShouldReturnThreePost() {
		//When searching twitter for "last week" tweets, should return 10 tweets
		GUI gui = new GUI();
		gui.twitter.searchTwitter("", "Last week");
		ArrayList<Status> list = gui.twitter.getFinalTweetsList();
		assertEquals(2,list.size());
	}
	
	@Test
	public void facebookSelectionIdShouldEqualExpandedId() {
		//When "Maltinha" is searched only on facebook, the expanded post ID should be 903964286283340_698505523592834
		GUI gui = new GUI();
		gui.facebook.searchFacebook("Teste 1", "Anytime");
		Post post = (Post) (gui.facebook.getFinalPostsList().get(0));
		String id = post.getId();
		assertEquals("494271834397031_494276877729860", id);
	}
	
	@Test
	public void twitterSelectionIdShouldEqualExpandedId() {
		//When "Teste 1" is searched only on twitter, the expanded post ID should be 1065189887576547330
		GUI gui = new GUI();
		gui.twitter.open();
		gui.twitter.searchTwitter("Teste 2", "Anytime");
		Status tweet = (Status) (gui.twitter.getFinalTweetsList().get(0));
		long id = tweet.getId();
		assertEquals(1069625307659939841L, id);
	}
	
	@Test
	public void twitterTest() {
		GUI gui = new GUI();
		gui.getConfigAccounts().write(new TwitterLoginRequest(gui.getConfigAccounts().getTwitterToken(), gui.getConfigAccounts().getTwitterTokenSecret()));
		gui.twitter.searchTwitter("Teste 2", "Anytime");
		Status tweet = (Status) (gui.twitter.getFinalTweetsList().get(0));
		long id = tweet.getId();
		gui.twitter.retweet(id);
		gui.twitter.favorite(id);
		gui.twitter.reply(id, "reply test");
		gui.twitter.searchTwitter("Teste 2", "Last hour");
		gui.twitter.searchTwitter("Teste 2", "Last day");
		gui.twitter.searchTwitter("Teste 2", "Last week");
		gui.twitter.searchTwitter("Teste 2", "Last month");
		gui.twitter.checkConnection();
	}
	
	@Test
	public void facebookTest()	{
		GUI gui = new GUI();
		gui.getConfigAccounts().write(new FacebookLoginRequest(gui.getConfigAccounts().getFacebookToken()));
		gui.facebook.searchFacebook("Teste 1", "Anytime");
		Post post = (Post) (gui.facebook.getFinalPostsList().get(0));
		String id = post.getId();
		gui.facebook.comment(id, "comment test");
		gui.facebook.like(id);
		gui.facebook.searchFacebook("Teste 1", "Last hour");
		gui.facebook.searchFacebook("Teste 1", "Last day");
		gui.facebook.searchFacebook("Teste 1", "Last week");
		gui.facebook.searchFacebook("Teste 1", "Last month");
	}
	
	@Test
	public void emailTest()	{
		GUI gui = new GUI();
		gui.email.login("projetoes61@gmail.com", "rumoao20");
		gui.getConfigAccounts().write(new EmailLoginRequest("projetoes61@gmail.com", "rumoao20"));
		gui.getConfigAccounts().delete("Email");
		gui.email.login("projetoes61@gmail.com", "rumoao20");
		gui.getConfigAccounts().write(new EmailLoginRequest("projetoes61@gmail.com", "rumoao20"));
		gui.email.searchGmail("", "Anytime");
		gui.email.searchGmail("", "Last hour");
		gui.email.searchGmail("", "Last day");
		gui.email.searchGmail("", "Last week");
		gui.email.searchGmail("", "Last month");
		gui.email.sendEmail("projetoes61@gmail.com", "projetoes61@gmail.com", "subject", "body");
		gui.email.checkConnection();
	}
	
	@Test
	public void configTest() {
		GUI gui = new GUI();
		gui.getConfigAccounts().loadLastSearch("Post");
		gui.getConfigAccounts().loadLastSearch("Status");
		gui.getConfigAccounts().loadLastSearch("Message");
		gui.facebook.searchFacebook("", "Anytime");
		gui.twitter.searchTwitter("", "Anytime");
		gui.email.login("projetoes61@gmail.com", "rumoao20");
		gui.email.searchGmail("", "Anytime");
		gui.manageTimeline();
		gui.tableToXML();
		gui.getConfigAccounts().write(gui.facebook.getFinalPostsList().get(0));
		gui.getConfigAccounts().write(gui.twitter.getFinalTweetsList().get(0));
		gui.getConfigAccounts().write(gui.email.getFinalEmailsList().get(0));
		gui.manageLastTimeline();
	}
	
	@Test
	public void guiTestElse() {
		GUI gui = new GUI();
		gui.configFrame();
	}
	
	@Test
	public void guiTestIf() {
		GUI gui = new GUI();
		gui.getConfigAccounts().setLoggedFacebook(false);
		gui.getConfigAccounts().setLoggedTwitter(false);
		gui.getConfigAccounts().setLoggedEmail(false);
		gui.configFrame();
	}
}
