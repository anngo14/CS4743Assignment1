package model;

import java.util.Date;

public class AuditTrailEntry {
	
	private int id;
	private Date dateAdded;
	private String message;
	
	public AuditTrailEntry()
	{
		id = -1;
		dateAdded = null;
		message = "";
	}
	public AuditTrailEntry(int id, Date date, String msg)
	{
		this.id = id;
		this.dateAdded = date;
		this.message = msg;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setDate(Date date)
	{
		this.dateAdded = date;
	}
	public void setMessage(String msg)
	{
		message = msg;
	}
	public int getId()
	{
		return id;
	}
	public Date getDate()
	{
		return dateAdded;
	}
	public String getMessage()
	{
		return message;
	}
	public String toString()
	{
		String output = "ID: " + this.id + "\n" + "Date: " + this.dateAdded + "\n" + "Message: " + this.message;
		return output + "\n";
	}
}
