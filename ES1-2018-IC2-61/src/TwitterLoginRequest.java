
public class TwitterLoginRequest extends LoginRequest {

	private String token;
	private String tokenSecret;
	
	/**
	 * Constructs a new instance of twitter login request
	 * @param token
	 * 		the token used to authenticate twitter actions
	 * @param tokenSecret
	 * 		the token secret associated with that token
	 */
	public TwitterLoginRequest(String token, String tokenSecret) {
		super("Twitter");
		this.token=token;
		this.tokenSecret=tokenSecret;
		
	}

	/**
	 * Returns the token.
	 * 
	 * @return token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Returns the token secret.
	 * 
	 * @return tokenSecret
	 */
	public String getTokenSecret() {
		return tokenSecret;
	}

}


