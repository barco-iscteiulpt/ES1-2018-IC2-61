import static org.junit.Assert.*;

import java.util.ArrayList;

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
		assertEquals(1,list.size());
	}
	
	@Test
	public void twitterSearchShouldReturnThreePost() {
		//When searching twitter for "last week" tweets, should return 3 tweets
		GUI gui = new GUI();
		gui.twitter.searchTwitter("", "Last week");
		ArrayList<Status> list = gui.twitter.getFinalTweetsList();
		assertEquals(3,list.size());
	}
	
	@Test
	public void facebookSelectionIdShouldEqualExpandedId() {
		//When "Maltinha" is searched only on facebook, the expanded post ID should be 903964286283340_698505523592834
		GUI gui = new GUI();
		gui.facebook.searchFacebook("Maltinha", "Anytime");
		Post post = (Post) (gui.facebook.getFinalPostsList().get(0));
		String id = post.getId();
		assertEquals("903964286283340_698505523592834", id);
	}
	
	@Test
	public void twitterSelectionIdShouldEqualExpandedId() {
		//When "Teste 1" is searched only on twitter, the expanded post ID should be 1065189887576547330
		GUI gui = new GUI();
		gui.twitter.searchTwitter("Teste 1", "Anytime");
		Status tweet = (Status) (gui.twitter.getFinalTweetsList().get(0));
		long id = tweet.getId();
		assertEquals(1065189887576547330L, id);
	}
	
}
