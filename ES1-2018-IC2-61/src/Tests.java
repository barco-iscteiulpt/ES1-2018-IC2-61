import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.restfb.types.Post;

import twitter4j.Status;

public class Tests {

	@Test
	public void facebookSearchShouldReturnOnePost(){
		//When "Maltinha" is searched only on facebook, it should return only one post
		GUI gui = new GUI();
		gui.searchFacebook("Maltinha", "Anytime");
		ArrayList<Post> list = gui.getFinalPostsList();
		assertEquals(1,list.size());
	}
	
	@Test
	public void twitterSearchShouldReturnOnePost(){
		//When searching twitter for "last week" tweets, should return 3 tweets
		GUI gui = new GUI();
		gui.searchTwitter("", "Last week");
		ArrayList<Status> list = gui.getFinalTweetsList();
		assertEquals(3,list);
	}
	
}
