
public class TwitterLoginRequest extends LoginRequest {

	private String token;
	private String tokenSecret;
	
	public TwitterLoginRequest(String token, String tokenSecret) {
		super("Twitter");
		this.token=token;
		this.tokenSecret=tokenSecret;
		
	}

	public String getToken() {
		return token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

}


