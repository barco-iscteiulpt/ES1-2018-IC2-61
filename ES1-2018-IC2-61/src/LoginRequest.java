
public class LoginRequest {
	
	private String service;
	private String accessToken;
	private String accessTokenSecret;
	
	public LoginRequest(String service, String conta, String token) {
		this.service=service;
		this.accessToken=conta;
		this.accessTokenSecret=token;
	}

	/**
	 * Returns the service to request the login.
	 * 
	 * @return service
	 */
	public String getService() {
		return service;
	}

	/**
	 * Returns the account used.
	 * 
	 * @return accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}


	/**
	 * Returns the token used.
	 * 
	 * @return accessTokenSecret
	 */
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}
	
	

}
