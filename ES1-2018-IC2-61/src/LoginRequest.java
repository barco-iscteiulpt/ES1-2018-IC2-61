
public class LoginRequest {
	
	private String service;
	private String conta;
	private String token;
	
	public LoginRequest(String service, String conta, String token) {
		this.service=service;
		this.conta=conta;
		this.token=token;
	}

	public String getService() {
		return service;
	}

	public String getConta() {
		return conta;
	}

	public String getToken() {
		return token;
	}
	
	

}
