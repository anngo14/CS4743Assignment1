package model;

public class User {

	private String name;
	private String hPass;
	private String role;
	
	public User()
	{
		name = "";
		hPass = "";
		role = "";
	}
	
	public User(String n, String h, String r)
	{
		name = n;
		hPass = h;
		role = r;
	}
	
	public void setName(String n)
	{
		name = n;
	}
	public void setHash(String h)
	{
		hPass = h;
	}
	public void setRole(String r)
	{
		role = r;
	}
	public String getName()
	{
		return name;
	}
	public String getHash()
	{
		return hPass;
	}
	public String getRole()
	{
		return role;
	}
	public String toString()
	{
		String out = "User: " + name + "\nHashed Password: " + hPass + "\nRole: " + role;
		return out;
	}
}
