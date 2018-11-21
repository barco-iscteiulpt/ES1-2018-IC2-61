
public class LoginRequest {
	
	private String service;
	private String conta;
	private String token;
	
	public LoginRequest(String service, String conta, String token) {
		this.service=service;
		this.conta=conta;
		this.token=token;
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
	 * @return conta
	 */
	public String getConta() {
		return conta;
	}


	/**
	 * Returns the token used.
	 * 
	 * @return token
	 */
	public String getToken() {
		return token;
	}
	
	

}
