package authenticate;


public abstract class Authenticator {
	public static final int INVALID_SESSION = 0;
	
	public abstract boolean hasAccess(int sessionId, String f);
	public abstract int loginSha256(String l, String pwHash) throws LoginException;
	public abstract String getUserNameFromSessionId(int sessionId);

	
	

}
