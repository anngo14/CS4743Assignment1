package authenticate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import authenticate.LoginException;


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
		boolean out = false;
		String result = "";
		CallableStatement call = null;
		try {
			call = connection.prepareCall("{ call accessRole(?)}");
			call.setInt(1, sessionId);
			ResultSet rs = call.executeQuery();
			while(rs.next())
			{
				result = rs.getString("output");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] functions = result.split("/");
		for(int i = 0; i < functions.length; i++)
		{
			if(functions[i].compareTo(f) == 0)
			{
				out = true;
			}
		}
		return out;
	}

	@Override
	public int loginSha256(String l, String pwHash) throws LoginException {
		int id = 0;
		CallableStatement call = null;
		try {
			call = connection.prepareCall("{ call checkUser(?,?)}");
			call.setString(1, l);
			call.setString(2, pwHash);
			
			ResultSet rs = call.executeQuery();
			while(rs.next())
			{
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(id == -1)
		{
			throw new LoginException("Authentication failed");
		}
		return id;
	}

	@Override
	public String getUserNameFromSessionId(int sessionId) {
		String output = "";
		CallableStatement call = null;
		try {
			call = connection.prepareCall("{ call sessionUser(?)}");
			call.setInt(1, sessionId);
			ResultSet rs = call.executeQuery();
			while(rs.next())
			{
				output = rs.getString("user");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
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
