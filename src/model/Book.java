package model;

import java.util.Calendar;

public class Book {

	private String title;
	private String isbn;
	private String summary;
	private int yearPublished;
	private int id;
	
	private final String DEFAULT_TITLE = "";
	private final String DEFAULT_ISBN = "";
	private final String DEFAULT_SUMMARY = "";
	private final int DEFAULT_YEAR_PUBLISHED = Calendar.getInstance().get(Calendar.YEAR);
	private final int DEFAULT_ID = -1;
	
	public Book() 
	{
		this.title = DEFAULT_TITLE;
		this.isbn = DEFAULT_ISBN;
		this.summary = DEFAULT_SUMMARY;
		this.yearPublished = DEFAULT_YEAR_PUBLISHED;
		this.id = DEFAULT_ID;
	}
	
	public Book(String title, String isbn, String summary, int yearPublished) 
	{
		this.title = title;
		this.isbn = isbn;
		this.summary = summary;
		this.yearPublished = yearPublished;
	}
	
	public Book(String title, String isbn, String summary, int yearPublished, int id) 
	{
		this.title = title;
		this.isbn = isbn;
		this.summary = summary;
		this.yearPublished = yearPublished;
		this.id = id;
	}
	
	public String getTitle() 
	{
		return title;
	}
	
	public String getISBN() 
	{
		return isbn;
	}
	
	public String getSummary() 
	{
		return summary;
	}
	
	public int getYearPublished() 
	{
		return yearPublished;
	}
	
	public int getId() 
	{
		return id;
	}
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public void setISBN(String isbn) 
	{
		this.isbn = isbn;
	}
	
	public void setSummary(String summary) 
	{
		this.summary = summary;
	}
	
	public void setYear(int yearPublished) 
	{
		this.yearPublished = yearPublished;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String toString() 
	{
		StringBuilder bookInfo = new StringBuilder();
		bookInfo.append("ID: " + id + "\n");
		bookInfo.append("\tTitle: " + title + "\n");
		bookInfo.append("\tISBN: " + isbn + "\n");
		bookInfo.append("\tSummary: " + summary + "\n");
		bookInfo.append("\tYear Published: " + yearPublished);
		return bookInfo.toString();
	}
}
