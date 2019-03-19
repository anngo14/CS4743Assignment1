package model;

import java.util.Date;

public class Publisher {

	private int id;
	private String name;
	private Date date_added;
	
	public Publisher()
	{
		id = -1;
		name = "";
		date_added = null;
	}
	public Publisher(int id, String name, Date date)
	{
		this.id = id;
		this.name = name;
		this.date_added = date;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setDate_Added(Date date)
	{
		this.date_added = date;
	}
	public int getId()
	{
		return this.id;
	}
	public String getName()
	{
		return this.name;
	}
	public Date getDate_Added()
	{
		return this.date_added;
	}
}
