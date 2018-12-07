
public class FacebookLoginRequest extends LoginRequest {

	private String token;
	
	/**
	 * Constructs a new instance of facebook login request
	 * @param token
	 * 		the token used to authenticate facebook actions
	 */
	public FacebookLoginRequest(String token) {
		super("Facebook");
		this.token=token;
		
	}

	/**
	 * Returns the token.
	 * 
	 * @return token
	 */
	public String getToken() {
		return token;
	}

}
