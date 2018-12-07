package BDA;

public abstract class LoginRequest {

	private String service;


	public LoginRequest(String service) {
		this.service=service;
	}

	/**
	 * Returns the service to request the login.
	 * 
	 * @return service
	 */
	public String getService() {
		return service;
	}

}