package authenticate;

import java.sql.Connection;

import com.mysql.jdbc.CallableStatement;

public class AuthenticatorProc extends Authenticator{

	private static AuthenticatorProc instance = null;
	private Connection connection;
	
	public AuthenticatorProc()
	{
	}
	
	public static AuthenticatorProc getInstance()
	{
		if(instance == null)
		{
			instance = new AuthenticatorProc();
		}
		return instance;
	}
	@Override
	public boolean hasAccess(int sessionId, String f) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int loginSha256(String l, String pwHash) throws LoginException {
		CallableStatement call;
		return 0;
	}

	@Override
	public void logout(int sessionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUserNameFromSessionId(int sessionId) {
		// TODO Auto-generated method stub
		return null;
	}
	public Connection getConnection()
	{
		return connection;
	}
	
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}

}
