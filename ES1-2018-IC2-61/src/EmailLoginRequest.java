
public class EmailLoginRequest extends LoginRequest {

	private String account;
	private String password;
	
	public EmailLoginRequest(String account, String password) {
		super("Email");
		this.account=account;
		this.password=password;
		
	}

	public String getAccount() {
		return account;
	}

	public String getPassword() {
		return password;
	}

}
