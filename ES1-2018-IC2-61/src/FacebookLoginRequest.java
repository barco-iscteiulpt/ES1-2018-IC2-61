
public class FacebookLoginRequest extends LoginRequest {

	private String token;
	
	public FacebookLoginRequest(String token) {
		super("Facebook");
		this.token=token;
		
	}

	public String getToken() {
		return token;
	}

}
