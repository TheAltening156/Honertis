package fr.honertis.guis.alts;

public class AccountData {
	public String username;
	public String refreshToken;
	
	public AccountData(String username, String refreshToken) {
		this.username = username;
		this.refreshToken = refreshToken;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}