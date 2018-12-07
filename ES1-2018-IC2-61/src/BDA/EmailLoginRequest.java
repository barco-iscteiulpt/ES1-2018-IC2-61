package BDA;

public class EmailLoginRequest extends LoginRequest {

	private String account;
	private String password;

	/**
	 * Constructs a new instance of email login request
	 * 
	 * @param account
	 *            the account used to login
	 * @param password
	 *            the password for that account
	 */
	public EmailLoginRequest(String account, String password) {
		super("Email");
		this.account = account;
		this.password = password;

	}

	/**
	 * Returns the account.
	 * 
	 * @return account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Returns the password.
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

}
